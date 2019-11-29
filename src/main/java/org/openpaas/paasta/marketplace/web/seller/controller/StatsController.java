package org.openpaas.paasta.marketplace.web.seller.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Instance;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.openpaas.paasta.marketplace.api.domain.SoftwareSpecification;
import org.openpaas.paasta.marketplace.api.domain.Yn;
import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.service.SoftwareService;
import org.openpaas.paasta.marketplace.web.seller.service.StatsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/stats")
@RequiredArgsConstructor
public class StatsController {

    private final SoftwareService softwareService;
    private final StatsService statsService;
    private final CommonService commonService;

    /**
     * 상품 현황 메인페이지로 이동한다.
     *
     * @return ModelAndView(Spring 클래스)
     */
    @GetMapping(value = "/softwares")
    public String getSoftwareStatsMain(Model model, HttpServletRequest httpServletRequest) {
        model.addAttribute("categories", softwareService.getCategories());
        model.addAttribute("spec", new SoftwareSpecification());
        return "contents/software-status";
    }


    /**
     * 상품별 현황 상세 페이지
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping(value = "/softwares/{id}")
    public String getSoftwareStats(Model model, @PathVariable Long id, HttpServletRequest httpServletRequest) {
        model.addAttribute("software", softwareService.getSoftware(id));

        // 단일 상품에 대한 총 사용자 수
        List<Long> idIn = new ArrayList<>();
        idIn.add(id);

        Map<Long, Long> result = statsService.getCountsOfInsts(idIn);
        Iterator iter = result.keySet().iterator();
        Object usedSwCount = null;

        if(result.size() > 0) {
            while(iter.hasNext()){
                Object key = iter.next();
                usedSwCount = result.get(key);
            }
        }else {
            usedSwCount = 0;
        }

        // 단일 상품 판매량
        model.addAttribute("personalSoldInstanceCount", statsService.soldInstanceCountOfSw(id));

        //사용량 추이
        Map  countsOfInstsProvider =  statsService.countsOfInstsProviderMonthly(idIn);
        model.addAttribute("totalCountInstsProviderInfo", commonService.getJsonStringFromMap(countsOfInstsProvider));

        model.addAttribute("usedSwCountSum", usedSwCount);
        model.addAttribute("instanceUsingUserSum", statsService.getCountOfUsersUsing());

        //총 사용 누적 금액
        model.addAttribute("softwareUsagePriceTotal", statsService.getSoftwareUsagePriceTotal(id));

        return "contents/software-statusdetail";
    }

    /**
     * 상품별 현황 상세 페이지(판매 현황 목록 조회)
     *
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/instances")
    @ResponseBody
    public CustomPage<Instance> getInstanceListBySwId(HttpServletRequest httpServletRequest) {
        return statsService.getInstanceListBySwId(commonService.setParameters(httpServletRequest));
    }


    /**
     * 요금 목록 조회
     *
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ResponseBody
    public CustomPage<Software> getSoftwareList(HttpServletRequest httpServletRequest){
        return statsService.getSoftwareList(commonService.setParameters(httpServletRequest));
    }


    /**
     * 요금 목록 페이지 이동
     *
     * @param model
     * @param oauth2User
     * @param httpSession
     * @param spec
     * @param authentication
     * @return
     */
    @GetMapping(value = "/softwares/my")
    public String getStatsSoftwaresMy(Model model, @AuthenticationPrincipal OAuth2User oauth2User, HttpSession httpSession, SoftwareSpecification spec, Authentication authentication) {
        model.addAttribute("categories", statsService.getCategories());
        model.addAttribute("status", Software.Status.values());
        model.addAttribute("spec", new SoftwareSpecification());
        model.addAttribute("types", Software.Type.values());
        model.addAttribute("yns", Yn.values());

        return "contents/software-charge";
    }
    
    /**
     * Seller 요금통계 정보조회
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/statsSoftwareSellPrice")
    @ResponseBody
    public Map<String,Object> getStatsSoftwareSellPriceList(HttpServletRequest httpServletRequest, @RequestParam(name="size") Integer size) {
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	
    	// 요금통계 리스트 조회
    	resultMap.put("statsSoftwareSellPriceList", statsService.getStatsSoftwareSellPricList(commonService.setParameters(httpServletRequest)));
    	
    	// 요금통계 TotalCount 조회
    	resultMap.put("totalElements", statsService.getStatsSoftwareSellPriceTotalCount(commonService.setParameters(httpServletRequest)));
    	resultMap.put("size", size);
    	
    	return resultMap;
    }

    /**
     * 상품현황의 Total 카운트 조회 (판매상품/사용자)
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/softwares/statsInfo")
    @ResponseBody
    public Map<String,Object> softwareStatsInfo(HttpServletRequest httpServletRequest) {
    	Map<String,Object> resultMap = new HashMap<String,Object>();

    	// 현재 사용중인 상품 카운트
    	resultMap.put("countOfInstsUsing", statsService.getCountOfInstsUsing(commonService.setParameters(httpServletRequest)));
    	
    	// 현재 상품을 사용중인 User 카운트
    	resultMap.put("countOfUsersUsing", statsService.getCountOfUsersUsing(commonService.setParameters(httpServletRequest)));

        return resultMap;
    }
    
    /**
     * 상품별현황 > 사용앱, 사용추이 차트 데이터 조회
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/softwares/chart/useAppTransitionInfo")
    @ResponseBody
    public Map<String,Object> useAppTransitionInfo(HttpServletRequest httpServletRequest) {
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	// 사용앱 차트 데이터 조회
    	resultMap.put("statsUseAppList", statsService.getStatsUseApp(commonService.setParameters(httpServletRequest)));
    	// 사용추이 차트 데이터 조회
    	resultMap.put("statsUseTransitionList", statsService.getStatsUseTransition(commonService.setParameters(httpServletRequest)));
    	return resultMap;
    }
}
