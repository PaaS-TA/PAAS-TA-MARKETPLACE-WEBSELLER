package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Profile;
import org.openpaas.paasta.marketplace.web.seller.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

        String testUer = "sjchoi@bluedigm.com" ; //User user = SecurityUtils.getUser();

        for(Profile profiles : profileList) {
            if(testUer.equals(profiles.getId())) {
                count++;
                profileId = profiles.getId();
            }
        }
        if(count > 0){
            return "redirect:/profiles/update/" + profileId;        // (1) 프로필 상세 페이지
        }
        return "redirect:/profiles/create";                         // (2) 프로필 등록
    }


    @GetMapping(value = "/{id}")
    public String getProfile(Model model, @PathVariable String id) {
        log.info(">> getProfile :: " + profileService.getProfile(id));
        model.addAttribute("profiles", profileService.getProfile(id));
        return "contents/profile-detail";
    }

    @GetMapping(value = "/create")
    public String getProfilesCreatePage(Model model, @ModelAttribute Profile profiles) {
        model.addAttribute("types", Profile.Type.values());
        model.addAttribute("profiles", profileService.getProfileList(""));
        return "contents/profile-create";
    }

    @GetMapping(value = "/update/{id}")
    public String getProfilesUpdatePage(Model model, @PathVariable String id) {
        model.addAttribute("types", Profile.Type.values());
        model.addAttribute("profiles", profileService.getProfile(id));
        return "contents/profile-update";
    }


    @PostMapping
    public String createProfile(@Valid Profile profile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {      // BindingResult.hasErrors : 에러가 있는지 검사
            return "contents/profile-create"; // (1)프로필 등록 페이지
        }
        log.info(">> createProfile {}", profile);

        Profile newProfile = profileService.createProfiles(profile);
        System.out.println(">> newProfile:: " + newProfile.getName());

        return "redirect:/profiles/{id}";    // (2)프로필 상세 페이지 return "contents/profile-detail";

    }


    @PutMapping(value = "/{id}")
    @ResponseBody
    public Profile updateProfiles(@PathVariable String id, @Valid @RequestBody Profile profile){
        log.info(">> updateProfiles " + profile.toString());
        return profileService.updateProfiles(id, profile);
    }


}
