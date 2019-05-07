package org.openpaas.paasta.marketplace.web.seller.controller;

import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.common.Constants;
import org.openpaas.paasta.marketplace.web.seller.model.SellerProfile;
import org.openpaas.paasta.marketplace.web.seller.service.SellerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */

@RestController
public class SellerProfileController {

    private static final String VIEW_URL = "/profile";

    @Autowired
    CommonService commonService;

    @Autowired
    SellerProfileService sellerProfileService;

    @GetMapping(value = Constants.URI_SELLER_PROFILE)
    public ModelAndView getProfile(HttpServletRequest httpServletRequest){
        return commonService.setPathVariables(httpServletRequest, VIEW_URL + "/createProfile", new ModelAndView());
    }

    @PostMapping(value = Constants.URI_MARKET_API)
    private SellerProfile createProfile(@RequestBody SellerProfile sellerProfile){
        return sellerProfileService.createProfile(sellerProfile);
    }
}
