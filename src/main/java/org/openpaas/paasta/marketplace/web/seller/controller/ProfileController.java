package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Profile;
import org.openpaas.paasta.marketplace.web.seller.service.ProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@Controller
@RequestMapping(value = "/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 사용자 정보 메인페이지로 이동한다.
     *
     * @return
     */
    @GetMapping(value = "/page")
    //@ResponseBody
    public String getProfile() {
        log.info("> into getProfile");
        int count = 0;
        String profileId = null;

        CustomPage<Profile> profileList = profileService.getProfileList("?page=0&size=10&sort=id");

        Authentication finalAuth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User principal = (OAuth2User) finalAuth.getPrincipal();
        String user = (String) principal.getAttributes().get("user_name");

        log.info(">> user :: " + user);

        for(Profile profiles : profileList) {
            if(user.equals(profiles.getId())) {
                count++;
                profileId = profiles.getId();
            }
        }
        if(count > 0){
            return "redirect:/profiles/" + profileId;               // (1) 프로필 상세 페이지
        }
        return "redirect:/profiles/create";                         // (2) 프로필 등록 페이지
    }



    @GetMapping(value = "/{id}")
    public String getProfile(Model model, @PathVariable String id) {
        log.info(">> getProfile :: " + profileService.getProfile(id));
        model.addAttribute("profiles", profileService.getProfile(id));
        return "contents/profile-detail";
    }


    @GetMapping(value = "/create")
    public String getProfilesCreatePage(Model model, @ModelAttribute Profile profile) {
        model.addAttribute("types", Profile.Type.values());
        return "contents/profile-create";
    }


    @GetMapping(value = "/update/{id}")
    public String getProfilesUpdatePage(Model model, @PathVariable String id) {
        model.addAttribute("types", Profile.Type.values());
        model.addAttribute("profiles", profileService.getProfile(id));
        return "contents/profile-update";
    }


    @PostMapping
    public String createProfile(@Valid Profile profile) {
        profileService.createProfiles(profile);
        return "redirect:/profiles/page";
    }


    @PutMapping(value = "/{id}")
    @ResponseBody
    public Profile updateProfiles(@PathVariable String id, @Valid @RequestBody Profile profile){
        log.info(">> updateProfiles " + profile.toString());
        return profileService.updateProfiles(id, profile);
    }


}
