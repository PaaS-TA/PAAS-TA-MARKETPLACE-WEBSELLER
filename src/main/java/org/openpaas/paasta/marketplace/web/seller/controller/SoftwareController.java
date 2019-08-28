package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.openpaas.paasta.marketplace.api.domain.SoftwareSpecification;
import org.openpaas.paasta.marketplace.api.domain.Yn;
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
import java.io.IOException;
import java.net.URLDecoder;

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

        software.setIcon(iconFile.getOriginalFilename());
        software.setApp(productFile.getOriginalFilename());
        software.setManifest(environmentFile.getOriginalFilename());
        software.setAppPath(URLDecoder.decode(swiftOSService.putObject(productFile).getFileURL(), "UTF-8"));
        software.setIconPath(URLDecoder.decode(swiftOSService.putObject(iconFile).getFileURL(), "UTF-8"));
        software.setManifestPath(URLDecoder.decode(swiftOSService.putObject(environmentFile).getFileURL(), "UTF-8"));
        log.info(software.toString());

        Software newSoftware = softwareService.createSoftware(software);
        return "redirect:/softwares/list";
    }


    /**
     * 상품 현황 리스트 조회 페이지 이동
     *
     * @return String
     */
    @GetMapping(value = "/report")
    public String getSoftwareUseStatus() {
        return "contents/software-report";
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
    @PutMapping(value = "/{id}")
    @ResponseBody
    public Software updateSoftware(@PathVariable Long id, @Valid @RequestBody Software software) {
        log.info(">> updateSoftware " + software.toString());
        Software updateSoftware = softwareService.getSoftware(id);
        updateSoftware.setName(software.getName());
        updateSoftware.setCategory(software.getCategory());
        updateSoftware.setApp(software.getApp());
        updateSoftware.setManifest(software.getManifest());
        updateSoftware.setScreenshotList(software.getScreenshotList());
        updateSoftware.setSummary(software.getSummary());
        updateSoftware.setDescription(software.getDescription());
        updateSoftware.setInUse(software.getInUse());
        updateSoftware.setType(software.getType());
        updateSoftware.setPricePerDay(software.getPricePerDay());
        updateSoftware.setVersion(software.getVersion());
        return softwareService.updateSoftware(id, software);
    }



}
