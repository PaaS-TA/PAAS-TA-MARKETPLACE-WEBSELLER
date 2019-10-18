package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        log.info(">> user :: " + user);

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

        log.info(">> createSoftware" + software.toString());

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
        log.info(software.toString());

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

        return "contents/software-update";
    }


    /**
     * 판매자 상품[파일] DB 수정
     */
    @PutMapping(value = "/file/{id}/{fileType}")
    @ResponseBody
    public Software updateFile(@PathVariable Long id, @PathVariable String  fileType, @RequestBody Map<String, String> params) {
        Software software = softwareService.getSoftware(id);
        String finalUrl = params.get("filePath");
        String fileName = params.get("fileName");

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

        return softwareService.updateSoftware(id, software);
    }


    /**
     * 판매자 상품[파일 - 스크린샷] DB 삭제
     */
    @DeleteMapping(value = "/file/{id}/screenShots/{imgPath}")
    @ResponseBody
    public Software deleteScreenshotList(@PathVariable Long id, @PathVariable String imgPath) {

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

        software.setScreenshotList(screenshotPackList);

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
    public Software updateSoftware(@PathVariable Long id, @RequestBody Software software) throws IOException {
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

        log.info(newSoftware.toString());
        return softwareService.updateSoftware(id,newSoftware);
    }



}
