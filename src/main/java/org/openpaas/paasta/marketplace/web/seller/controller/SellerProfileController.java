package org.openpaas.paasta.marketplace.web.seller.controller;

import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.common.Constants;
import org.openpaas.paasta.marketplace.web.seller.model.SellerProfile;
import org.openpaas.paasta.marketplace.web.seller.service.SellerProfileService;
import org.openpaas.paasta.marketplace.web.seller.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * 판매자 프로필 Controller
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */
@RestController
public class SellerProfileController {

    private static final String VIEW_URL = "/profile";

    @Autowired
    private CommonService commonService;

    @Autowired
    private SellerProfileService sellerProfileService;

    /**
     * 프로필 등록 페이지 이동
     *
     * @param httpServletRequest the httpServletRequest
     * @return ModelAndView
     */
    @GetMapping(value = Constants.URI_SELLER_PROFILE)
    public ModelAndView getProfileCreatePage(HttpServletRequest httpServletRequest){
        return commonService.setPathVariables(httpServletRequest, VIEW_URL + "/createProfile", new ModelAndView());
    }

    /**
     * 프로필 등록
     *
     * @param sellerProfile the seller profile
     * @return SellerProfile
     */
    @PostMapping(value = Constants.URI_MARKET_API_PROFILE)
    private SellerProfile createProfile(@RequestBody SellerProfile sellerProfile){
    	String userId = SecurityUtils.getUserId();
    	sellerProfile.setSellerId(userId);
    	sellerProfile.setCreateId(userId);
    	sellerProfile.setUpdateId(userId);

    	return sellerProfileService.createProfile(sellerProfile);
    }


    /**
     * 프로필 상세 조회 페이지 이동
     *
     * @param httpServletRequest the httpServletRequest
     * @return ModelAndView
     */
    @GetMapping(value = Constants.URI_SELLER_PROFILE + "/{id}")
    public ModelAndView getProfilePage(HttpServletRequest httpServletRequest, @PathVariable(value = "id") Long id){
        return commonService.setPathVariables(httpServletRequest, VIEW_URL + "/getProfile", new ModelAndView());
    }


    /**
     * 프로필 상세 조회
     *
     * @param id the id
     * @return SellerProfile
     */
    @GetMapping(value = Constants.URI_MARKET_API_PROFILE + "/{id}")
    private SellerProfile getProfile(@PathVariable Long id){
        return sellerProfileService.getProfile(id);
    }


//    @GetMapping(value = Constants.URI_MARKET_API_PROFILE + "/{sellerId}")
//    private SellerProfile getProfileBySellerId(@PathVariable String sellerId){
//        return sellerProfileService.getProfileByUserId(sellerId);
//    }


    /**
     * 프로필 수정 페이지 이동
     *
     * @param httpServletRequest the httpServletRequest
     * @return ModelAndView
     */
    @GetMapping(value = Constants.URI_SELLER_PROFILE + "/{id}/update")
    public ModelAndView getProfileUpdatePage(HttpServletRequest httpServletRequest, @PathVariable Long id){
        return commonService.setPathVariables(httpServletRequest, VIEW_URL + "/updateProfile", new ModelAndView());
    }


    /**
     * 프로필 수정
     *
     * @param id the id
     * @param sellerProfile the seller profile
     */
    @PutMapping(value = Constants.URI_MARKET_API_PROFILE + "/{id}")
    private void updateProfile(@PathVariable String id, @RequestBody SellerProfile sellerProfile){
        sellerProfileService.updateProfile(id, sellerProfile);
    }
}
