package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.*;
import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.service.StatsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping(value = "/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final CommonService commonService;

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
        log.info(">> stats Init" );
        model.addAttribute("categories", statsService.getCategories());
        model.addAttribute("status", Software.Status.values());
        model.addAttribute("spec", new SoftwareSpecification());
        model.addAttribute("types", Software.Type.values());
        model.addAttribute("yns", Yn.values());
        return "contents/priceCalculation";
    }

}
