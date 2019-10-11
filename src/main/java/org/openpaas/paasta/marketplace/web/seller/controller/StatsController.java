package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.*;
import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.service.SoftwareService;
import org.openpaas.paasta.marketplace.web.seller.service.StatsService;
import org.openpaas.paasta.marketplace.web.seller.util.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Slf4j
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
        CustomPage<Software> software = softwareService.getSoftwareList(commonService.setParameters(httpServletRequest));

        if(software.getTotalElements() > 0) {
            List<Long> idIn = new ArrayList<>();

            for (Software s:software.getContent()) {
                idIn.add(s.getId());
            }

            Map<Long, Long> result = statsService.getCountsOfInsts(idIn);
            Map newResult = new HashMap();

            for (Long id:idIn) {
                String mapId = "" + id;
                if(result.get(mapId) != null){
                    newResult.put(mapId, result.get(mapId));
                }else{
                    newResult.put(mapId, 0);
                }
            }

            // 판매량
            model.addAttribute("soldInstanceCount", statsService.soldInstanceByProvider(idIn));

            //사용량 추이
            Map  countsOfInstsProvider =  statsService.countsOfInstsProviderMonthly(idIn);
            model.addAttribute("totalCountInstsProviderInfo", commonService.getJsonStringFromMap(countsOfInstsProvider));

            model.addAttribute("instanceUserCount", commonService.getJsonStringFromMap(newResult));
        } else {
            model.addAttribute("soldInstanceCount", null);
            model.addAttribute("totalCountInstsProviderInfo", null);
            model.addAttribute("instanceUserCount", null);
        }

        model.addAttribute("categories", softwareService.getCategories());
        model.addAttribute("spec", new SoftwareSpecification());

        // 판매 상품
        model.addAttribute("soldSwCount", statsService.countOfSoldSw(SecurityUtils.getUser().getAttributes().get("user_name").toString()));

        // 판매량
        model.addAttribute("instanceCountSum", statsService.getCountOfInstsUsing());

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
        CustomPage<Software> software = softwareService.getSoftwareList("");

        if(software.getTotalElements() > 0) {
            List<Long> idIn = new ArrayList<>();
            for (Software s:software.getContent()) {
                idIn.add(s.getId());
            }

            model.addAttribute("soldInstanceCount", statsService.soldInstanceByProvider(idIn));
        } else {
            model.addAttribute("soldInstanceCount", null);
        }


        model.addAttribute("categories", statsService.getCategories());
        model.addAttribute("status", Software.Status.values());
        model.addAttribute("spec", new SoftwareSpecification());
        model.addAttribute("types", Software.Type.values());
        model.addAttribute("yns", Yn.values());

        return "contents/software-charge";
    }

}
