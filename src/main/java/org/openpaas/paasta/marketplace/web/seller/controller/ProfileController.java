package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.web.seller.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping(value = "/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping(value = "/{id}")
    public String updateProfiles(Model model, @PathVariable String id) {
        System.out.println("profileService.getProfiles(id)" + profileService.updateProfiles(id));

        model.addAttribute("profile", profileService.updateProfiles(id));

        return "contents/profile-update";
    }

    /*
    @PostMapping
    public Profile createProfile(@Valid Profile profiles, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "contents/software-create";
        }

        Profile newProfile = profileService.createProfiles(profiles);
        System.out.println(::: create ::: " + newProfile.getName());
        return "redirect:/profiles";
    }

    @PutMapping(value = "/{id}")
    public String updateProfiles(Model model, @PathVariable String id) {
        model.addAttribute("profiles", profileService.updateProfiles(id));
        return "contents/profile-update";
    }
    * */



}
