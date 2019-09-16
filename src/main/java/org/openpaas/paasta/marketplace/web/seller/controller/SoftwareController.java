package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.*;
import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.service.SoftwareService;
import org.openpaas.paasta.marketplace.web.seller.storageApi.store.swift.SwiftOSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

@Controller
@RequestMapping(value = "/softwares")
@Slf4j
@RequiredArgsConstructor
public class SoftwareController {

    private final SoftwareService softwareService;
    private final CommonService commonService;

    @Autowired
    SwiftOSService swiftOSService;
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
//        httpSession.setAttribute("yourName", oauth2User.getAttributes().get("user_name"));
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
        return "contents/software-create";
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
     * 판매자가 등록한 상품 수정
     *
     * @param software
     * @param id
     * @return
     */
//    @PutMapping(value = "/{id}")
//    @ResponseBody
//    public List updateSoftware(@Valid @RequestBody Software software, @PathVariable Long id, @RequestParam(value = "screenshots") MultipartFile[] screenshots) throws IOException {
//        log.info(">> updateSoftware " + software.toString());
//        List<String> screenshotList = new ArrayList<>();
//        for(int i = 0; i < screenshots.length; i++) {
//            screenshotList.add(URLDecoder.decode(swiftOSService.putObject(screenshots[i]).getFileURL(), "UTF-8"));
//        }
//        software.setScreenshotList(screenshotList);
//        return software.getScreenshotList();
//    }



    /**
     * 판매자 상품[파일] DB 수정
     */
    @PutMapping(value = "/file/{id}/{fileType}/{imgPath}")
    @ResponseBody
    public Software updateFile(@PathVariable Long id, @PathVariable String  fileType, @PathVariable String imgPath, @RequestParam(name = "iconFileName", required = false) String iconFileName) {
        Software software = softwareService.getSoftware(id);
        String finalUrl = "http://15.164.0.24:10008/v1/AUTH_955647b847dd483d9ce3aa7828fc6ed5/marketplace-container/" + imgPath;

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
            software.setIcon(iconFileName);
            software.setIconPath(finalUrl);
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

        String finalUrl = "http://15.164.0.24:10008/v1/AUTH_955647b847dd483d9ce3aa7828fc6ed5/marketplace-container/" + imgPath;


        // 어차피 삭제되는 애는 한 개씩
        if(originShotList.size() > 0) {
            for(int i = 0; i < originShotList.size(); i++){
                if(!originShotList.get(i).equals(finalUrl)) {
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

    //**************PUT(POST)**************

//    @GetMapping(value = "/{id}/update")
//    public String updateSoftwareHtml(Model model, @PathVariable Long id) {
//        model.addAttribute("software", softwareService.getSoftware(id));
//        model.addAttribute("spec", new SoftwareSpecification());
//        model.addAttribute("yns", Yn.values());
//        model.addAttribute("types", Software.Type.values());
//        model.addAttribute("status", Software.Status.values());
//        model.addAttribute("categories", softwareService.getCategories());
//
//        return "contents/software-modifiy";
//    }
//
//    @PostMapping(value = "/{id}")
//    public String modifiySoftware(@PathVariable Long id, @Valid Software software, BindingResult bindingResult, @RequestParam(value = "screenshots") MultipartFile[] screenshots, @RequestParam(value = "iconFile") MultipartFile iconFile,
//                                 @RequestParam(value = "productFile") MultipartFile productFile, @RequestParam(value = "environmentFile") MultipartFile environmentFile) throws IOException {
//
//        if (bindingResult.hasErrors()) {
//            return "contents/software-modifiy";
//        }
//
//        log.info(">> updateSoftware" + software.toString());
//
//        software.setIcon(iconFile.getOriginalFilename());
//        software.setApp(productFile.getOriginalFilename());
//        software.setManifest(environmentFile.getOriginalFilename());
//
//        List<String> screenshotList = new ArrayList<>();
//        for(int i = 0; i < screenshots.length; i++) {
//            screenshotList.add(URLDecoder.decode(swiftOSService.putObject(screenshots[i]).getFileURL(), "UTF-8"));
//        }
//        software.setScreenshotList(screenshotList);
//
//        software.setAppPath(URLDecoder.decode(swiftOSService.putObject(productFile).getFileURL(), "UTF-8"));
//        software.setIconPath(URLDecoder.decode(swiftOSService.putObject(iconFile).getFileURL(), "UTF-8"));
//        software.setManifestPath(URLDecoder.decode(swiftOSService.putObject(environmentFile).getFileURL(), "UTF-8"));
//        log.info(software.toString());
//
//        Software newSoftware = softwareService.updateSoftware(id,software);
//        return "redirect:/softwares/{id}";
//    }

    @PutMapping(value = "/{id}")
    public Software updateSoftware(@PathVariable Long id, @Valid Software software) throws IOException {
        log.info(">> updateSoftware" + software.toString());

        software.setIcon(software.getIcon());
        software.setApp(software.getApp());
        software.setManifest(software.getManifest());

        log.info(software.toString());
        return softwareService.updateSoftware(id,software);
    }



}
