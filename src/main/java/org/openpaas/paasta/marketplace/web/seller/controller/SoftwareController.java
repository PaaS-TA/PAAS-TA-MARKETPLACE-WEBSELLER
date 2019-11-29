package org.openpaas.paasta.marketplace.web.seller.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Profile;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.openpaas.paasta.marketplace.api.domain.SoftwareHistory;
import org.openpaas.paasta.marketplace.api.domain.SoftwarePlan;
import org.openpaas.paasta.marketplace.api.domain.SoftwareSpecification;
import org.openpaas.paasta.marketplace.api.domain.Yn;
import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.service.ProfileService;
import org.openpaas.paasta.marketplace.web.seller.service.SoftwareService;
import org.openpaas.paasta.marketplace.web.seller.storageApi.store.swift.SwiftOSService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/softwares")
@Slf4j
@RequiredArgsConstructor
public class SoftwareController {

    private final SoftwareService softwareService;
    private final ProfileService profileService;
    private final CommonService commonService;
    private final SwiftOSService swiftOSService;
    /**
     * 판매자의 상품 목록 조회 페이지 이동
     *
     * @param model
     * @param oauth2User
     * @param httpSession
     * @param spec
     * @param authentication
     * @return
     */
    @GetMapping(value = "/list")
    public String getSoftwares(Model model, @AuthenticationPrincipal OAuth2User oauth2User, HttpSession httpSession, SoftwareSpecification spec, Authentication authentication) {
        model.addAttribute("categories", softwareService.getCategories());
        model.addAttribute("spec", new SoftwareSpecification());
        model.addAttribute("yns", Yn.values());
        model.addAttribute("status", Software.Status.values());
        model.addAttribute("statusApprove", Software.Status.Approval);

        return "contents/software-list";
    }


    /**
     * 판매자의 상품 목록 조회
     *
     * @param httpServletRequest the httpServletRequest
     * @return CustomPage<Software>
     */
    @GetMapping
    @ResponseBody
    public CustomPage<Software> getSoftwareList(HttpServletRequest httpServletRequest){
        return softwareService.getSoftwareList(commonService.setParameters(httpServletRequest));
    }


    /**
     * 판매자 상품 등록 페이지 이동
     *
     * @param model
     * @param httpSession
     * @param software
     * @return
     */
    @GetMapping(value = "/create")
    public String createSoftwareHtml(Model model, HttpSession httpSession, @ModelAttribute Software software) {
        model.addAttribute("types", Software.Type.values());
        model.addAttribute("yns", Yn.values());
        model.addAttribute("categories", softwareService.getCategories());

        int count = 0;
        int statusCount = 0;

        CustomPage<Profile> profileList = profileService.getProfileList("?page=0&size=10&sort=id");
        Authentication finalAuth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User principal = (OAuth2User) finalAuth.getPrincipal();
        String user = (String) principal.getAttributes().get("user_name");

        for(Profile profiles : profileList) {
            if(user.equals(profiles.getId())) {
                count++;
                if(Profile.Status.Approval == profiles.getStatus()) {
                	statusCount++;
                }
            }
        }
        if(count > 0){
        	if(statusCount == 0)
        		return "redirect:/profiles/" + user;				// (1) 프로필 상세 페이지
            return "contents/software-create";                      // (2) 상품 등록 페이지
        }

        return "redirect:/profiles/create";                         // (3) 프로필 등록 페이지

    }


    /**
     * 판매자 상품 등록
     *
     * @param software
     * @param bindingResult
     * @param screenshots
     * @param iconFile
     * @param productFile
     * @param environmentFile
     * @return
     */
    @PostMapping
    public String createSoftware(@Valid Software software, BindingResult bindingResult, @RequestParam(value = "screenshots") MultipartFile[] screenshots, @RequestParam(value = "iconFile") MultipartFile iconFile,
                                 @RequestParam(value = "productFile") MultipartFile productFile, @RequestParam(value = "environmentFile") MultipartFile environmentFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "contents/software-create";
        }

        software.setIcon(iconFile.getOriginalFilename());
        software.setApp(productFile.getOriginalFilename());
        software.setManifest(environmentFile.getOriginalFilename());

        List<String> screenshotList = new ArrayList<>();
        for(int i = 0; i < screenshots.length; i++) {
            screenshotList.add(URLDecoder.decode(swiftOSService.putObject(screenshots[i]).getFileURL(), "UTF-8"));
        }
        software.setScreenshotList(screenshotList);

        software.setAppPath(URLDecoder.decode(swiftOSService.putObject(productFile).getFileURL(), "UTF-8"));
        software.setIconPath(URLDecoder.decode(swiftOSService.putObject(iconFile).getFileURL(), "UTF-8"));
        software.setManifestPath(URLDecoder.decode(swiftOSService.putObject(environmentFile).getFileURL(), "UTF-8"));

        //Add SoftwarePlan
        List<SoftwarePlan> softwarePlanList = new ArrayList<>();
        for(int i = 0; i < software.getSoftwarePlanList().size(); i++) {
        	SoftwarePlan temp = software.getSoftwarePlanList().get(i);
        	if (StringUtils.isNotBlank(temp.getName()) && StringUtils.isNotBlank(temp.getApplyMonth())) {
        		softwarePlanList.add(temp);
        	}
        }
        software.setSoftwarePlanList(softwarePlanList);

        Software newSoftware = softwareService.createSoftware(software);
        return "redirect:/softwares/list";
    }


    /**
     * 판매자의 상품 상세 조회
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public String getSoftware(Model model, @PathVariable Long id) {
        model.addAttribute("software", softwareService.getSoftware(id));
        model.addAttribute("softwarePlanList", softwareService.getSoftwarePlanList(id));
        return "contents/software-detail";
    }


    /**
     * 판매자가 등록한 상품 수정 페이지 이동
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/update")
    public String updateSoftwareHtml(Model model, @PathVariable Long id) {
        model.addAttribute("software", softwareService.getSoftware(id));
        model.addAttribute("spec", new SoftwareSpecification());
        model.addAttribute("yns", Yn.values());
        model.addAttribute("types", Software.Type.values());
        model.addAttribute("status", Software.Status.values());
        model.addAttribute("categories", softwareService.getCategories());
        model.addAttribute("softwarePlanList", softwareService.getSoftwarePlanList(id));
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        model.addAttribute("currYearMonth", format.format(new Date()));
        
        return "contents/software-update";
    }


    /**
     * 판매자 상품[파일] DB 수정
     */
    @PutMapping(value = "/file/{id}/{fileType}")
    @ResponseBody
    public Software updateFile(@PathVariable Long id, @PathVariable String fileType, @RequestBody Map<String, String> params) throws IOException {
        Software software = softwareService.getSoftware(id);
        String finalUrl = null;
        String fileName = null;
        
        if(params.get("fileName") != null) {
            fileName = params.get("fileName");
        }
        
        if (params.get("filePath") != null) {
        	finalUrl = URLDecoder.decode(params.get("filePath"), "UTF-8");
        }

        // 스크린샷 DB 업데이트
        if(fileType.equals("screenShots")) {
            // 기존 스크린 샷 리스트
            List<String> originShotList = software.getScreenshotList();

            // 새로 넣어줄 스크린 샷 리스트
            List<String> screenshotPackList = new ArrayList<>();

            if(originShotList.size() > 0) {
                screenshotPackList.addAll(originShotList);
                screenshotPackList.add(finalUrl);
            } else {
                screenshotPackList.add(finalUrl);
            }

            software.setScreenshotList(screenshotPackList);
        }


        // icon DB 업데이트
        if(fileType.equals("icon")) {
            software.setIcon(fileName);
            software.setIconPath(finalUrl);
        }

        // 상품 파일 DB 업데이트
        if(fileType.equals("prodFile")) {
            software.setApp(fileName);
            software.setAppPath(finalUrl);
        }

        // 환경 파일 DB 업데이트
        if(fileType.equals("envFile")) {
            software.setManifest(fileName);
            software.setManifestPath(finalUrl);
        }
        
        // 업데이트 History Description
        if (params.get("historyDescription") != null) {
        	software.setHistoryDescription(params.get("historyDescription"));
        }

        return softwareService.updateSoftware(id, software);
    }


    /**
     * 판매자 상품[파일 - 스크린샷] DB 삭제
     */
    @DeleteMapping(value = "/file/{id}/screenShots/{imgPath}")
    @ResponseBody
    public Software deleteScreenshotList(@PathVariable Long id, @PathVariable String imgPath, @RequestBody Map<String, String> params) {

        Software software = softwareService.getSoftware(id);

        // 기존 스크린 샷 리스트
        List<String> originShotList = software.getScreenshotList();

        // 새로 넣어줄 스크린 샷 리스트
        List<String> screenshotPackList = new ArrayList<>();

        // 어차피 삭제되는 애는 한 개씩
        if(originShotList.size() > 0) {
            for(int i = 0; i < originShotList.size(); i++){
                if(!originShotList.get(i).contains(imgPath)) {
                    screenshotPackList.add(originShotList.get(i));
                }
            }
        }
        
        String historyDescription = params.get("historyDescription") != null ? params.get("historyDescription") : "스크린샷 삭제";
        software.setScreenshotList(screenshotPackList);
        software.setHistoryDescription(historyDescription);

        return softwareService.updateSoftware(id, software);
    }


    /**
     * 상품 수정이력 조회
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/{id}/histories")
    @ResponseBody
    public List<SoftwareHistory> getHistoryList(@NotNull @PathVariable Long id, HttpServletRequest httpServletRequest) {
        return softwareService.getHistoryList(id, commonService.setParameters(httpServletRequest));
    }

    /**
     * 판매자가 등록한 상품 수정
     *
     * @param id
     * @param software
     * @return
     * @throws IOException
     */
    @PutMapping(value = "/{id}")
    @ResponseBody
    public Software updateSoftware(@PathVariable Long id, @RequestParam(name ="softwarePlaneOriginalList") String softwarePlaneOriginalList, @RequestBody Software software) throws IOException {
        log.info(">> updateSoftware" + software.toString());

        // 이미 파일 관련은 처리된 상태
        Software newSoftware = softwareService.getSoftware(id);
        newSoftware.setInUse(software.getInUse());
        newSoftware.setName(software.getName());
        newSoftware.setCategory(software.getCategory());
        newSoftware.setSummary(software.getSummary());
        newSoftware.setDescription(software.getDescription());
        newSoftware.setType(software.getType());
        newSoftware.setPricePerMonth(software.getPricePerMonth());
        newSoftware.setVersion(software.getVersion());
        newSoftware.setHistoryDescription(software.getHistoryDescription());
        newSoftware.setSoftwarePlanList(software.getSoftwarePlanList());

        return softwareService.updateSoftware(id,softwarePlaneOriginalList,newSoftware);
    }

    @DeleteMapping("/plan/{id}")
    @ResponseBody
    public List<SoftwarePlan> deleteCategory(@PathVariable Long id){
        softwareService.deleteSwpId(id);
        return softwareService.getSoftwarePlanList(id);
    }

    /**
     * 상품 판매가격 수정이력 조회
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/plan/{id}/histories")
    @ResponseBody
    public List<SoftwarePlan> getPlanHistoryList(@NotNull @PathVariable Long id, HttpServletRequest httpServletRequest) {
        return softwareService.getPlanHistoryList(id, commonService.setParameters(httpServletRequest));
    }

    @GetMapping(value = "/plan/{id}/applyMonth")
    @ResponseBody
    public List<SoftwarePlan> getApplyMonth(@NotNull @PathVariable Long id,@RequestParam (name="applyMonth") String applyMonth) {
        return softwareService.getApplyMonth(id, applyMonth);
    }

    /**
     * 판매자의 상품 목록 조회
     *
     * @param httpServletRequest the httpServletRequest
     * @return CustomPage<Software>
     */
    @GetMapping("/with/instanceCount")
    @ResponseBody
    public Map<String,Object> getSoftwareWithInstanceCountList(HttpServletRequest httpServletRequest){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	
    	// 소프트웨어 리스트 조회
    	CustomPage<Software> softwarePage = softwareService.getSoftwareList(commonService.setParameters(httpServletRequest));
    	List<Software> softwareList = softwarePage.getContent();
    	resultMap.put("softwarePage", softwarePage);
    	
    	if (CollectionUtils.isEmpty(softwareList)) {
    		return resultMap;
    	}
    	
    	List<Long> softwareIdList = new ArrayList<Long>();
    	for (Software info : softwareList) {
    		softwareIdList.add(info.getId());
    	}
    	
    	// 판매된 소프트웨어의 카운트정보 조회
    	Map<String,Object> instanceCountMap = softwareService.getSoftwareInstanceCountMap(softwareIdList);
    	resultMap.put("instanceCountMap", instanceCountMap);
    	
        return resultMap;
    }

}
