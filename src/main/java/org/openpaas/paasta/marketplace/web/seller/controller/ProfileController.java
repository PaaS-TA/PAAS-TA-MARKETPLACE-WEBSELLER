package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 프로필 관리 Controller
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-08-02
 */
@Slf4j
@Controller
@RequestMapping(value = "/profile")
@RequiredArgsConstructor
public class ProfileController {

    /**
     * 프로필 등록 페이지 이동
     *
     * @return String
     */
    @GetMapping(value = "/create")
    public String getSoftwareUseStatus() {
        return "contents/profile-create";
    }
}
