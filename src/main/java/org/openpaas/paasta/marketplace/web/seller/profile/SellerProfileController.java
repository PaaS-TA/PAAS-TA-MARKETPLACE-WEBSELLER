package org.openpaas.paasta.marketplace.web.seller.profile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.openpaas.paasta.marketplace.web.seller.util.DateUtils;
import org.openpaas.paasta.marketplace.web.seller.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;


/**
 * 판매자 프로필 Controller
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */
@RestController
@Slf4j
public class SellerProfileController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private SellerProfileService sellerProfileService;

    /**
     * 프로필 목록 조회 화면
     * 
     * @param httpServletRequest
     * @param id
     * @return
     */
    @GetMapping(value = SellerConstants.URI_WEB_SELLER_PROFILE_LIST)
    public ModelAndView getProfileListPage(HttpServletRequest httpServletRequest) {
    	// 화면 변수 처리
    	return commonService.setPathVariables(httpServletRequest, SellerConstants.URI_VIEW_PROFILE + "/getlistProfile", new ModelAndView());
    }

    /**
     * 프로필 목록 조회
     *
     * @param id the id
     * @return SellerProfile
     */
    @GetMapping(value = SellerConstants.URI_CTRL_SELLER_PROFILE_LIST)
    private List<SellerProfile> getProfileList() {
    	List<SellerProfile> profileList = sellerProfileService.getSellerProfileList();
    	for (SellerProfile profile : profileList) {
	        profile.setStrCreateDate(DateUtils.getConvertDate(profile.getCreateDate(), DateUtils.FORMAT_1));
	        profile.setStrUpdateDate(DateUtils.getConvertDate(profile.getCreateDate(), DateUtils.FORMAT_1));
    	}

		return profileList;
    }

    /**
     * 프로필 상세 조회
     *
     * @param httpServletRequest the httpServletRequest
     * @return ModelAndView
     */
    @GetMapping(value = SellerConstants.URI_WEB_SELLER_PROFILE_DETAIL)
    public ModelAndView getProfilePage(HttpServletRequest httpServletRequest, @PathVariable(value = "id") Long id) {
    	// 화면 변수 처리
    	return commonService.setPathVariables(httpServletRequest, SellerConstants.URI_VIEW_PROFILE + "/getProfile", new ModelAndView());
    }


    /**
     * 프로필 상세 조회
     *
     * @param id the id
     * @return SellerProfile
     */
    @GetMapping(value = SellerConstants.URI_CTRL_SELLER_PROFILE_DETAIL)
    private SellerProfile getProfile(@PathVariable Long id) {
    	SellerProfile seller = sellerProfileService.getProfile(id);
		String createdDate = DateUtils.getConvertDate(seller.getCreateDate(), DateUtils.FORMAT_1);
        String updatedDate = DateUtils.getConvertDate(seller.getCreateDate(), DateUtils.FORMAT_1);
		log.info("createdDate: " + createdDate);
        log.info("updatedDate: " + updatedDate);
		seller.setStrCreateDate(createdDate);
		seller.setStrUpdateDate(updatedDate);

		return seller;
    }

    /**
     * 프로필 등록 페이지 이동
     *
     * @param httpServletRequest the httpServletRequest
     * @return ModelAndView
     */
    @GetMapping(value = SellerConstants.URI_WEB_SELLER_PROFILE_CREATE)
    public ModelAndView getProfileCreatePage(HttpServletRequest httpServletRequest){
    	log.info("create view");
    	return commonService.setPathVariables(httpServletRequest, SellerConstants.URI_VIEW_PROFILE + "/createProfile", new ModelAndView());
    }
    
    /**
     * 프로필 등록
     *
     * @param sellerProfile the seller profile
     * @return SellerProfile
     */
    @PostMapping(value = SellerConstants.URI_WEB_SELLER_PROFILE_CREATE)
    private SellerProfile createProfile(@RequestBody SellerProfile sellerProfile) throws Exception {
    	String userId = SecurityUtils.getUserId();
    	sellerProfile.setSellerId(userId);
    	sellerProfile.setCreateId(userId);
    	sellerProfile.setUpdateId(userId);
    	
    	return sellerProfileService.createProfile(sellerProfile);
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
    @GetMapping(value = SellerConstants.URI_WEB_SELLER_PROFILE_UPDATE)
    public ModelAndView getProfileUpdatePage(HttpServletRequest httpServletRequest, @PathVariable Long id){
        return commonService.setPathVariables(httpServletRequest, SellerConstants.URI_VIEW_PROFILE + "/updateProfile", new ModelAndView());
    }

    /**
     * 프로필 수정
     *
     * @param id the id
     * @param sellerProfile the seller profile
     */
    @PutMapping(value = SellerConstants.URI_WEB_SELLER_PROFILE_UPDATE)
    private SellerProfile updateProfile(@PathVariable Long id, @RequestBody SellerProfile sellerProfile){
        String userId = SecurityUtils.getUserId();
        sellerProfile.setUpdateId(userId);

        return sellerProfileService.updateProfile(id, sellerProfile);
    }
}
