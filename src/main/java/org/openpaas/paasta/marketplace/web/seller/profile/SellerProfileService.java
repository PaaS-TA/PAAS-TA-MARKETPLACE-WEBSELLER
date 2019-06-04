package org.openpaas.paasta.marketplace.web.seller.profile;

import org.openpaas.paasta.marketplace.web.seller.common.RestTemplateService;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * 판매자 프로필 Service
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */
@Service
public class SellerProfileService {

    @Autowired
    private RestTemplateService marketApiRest;

    /**
     * 판매자 프로필 상세 조회
     *
     * @param id the id
     * @return SellerProfile
     */
    public SellerProfile getProfile(Long id) {
        return marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_SELLER_PROFILE + "/" + id, null, HttpMethod.GET, null, SellerProfile.class);
    }

    /**
     * 판매자 프로필 등록
     *
     * @param sellerProfile the seller profile
     * @return SellerProfile
     * @throws Exception 
     */
    public SellerProfile createProfile(SellerProfile sellerProfile) {
    	return marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_SELLER_PROFILE, null, HttpMethod.POST, sellerProfile, SellerProfile.class);
    }

    /**
     * 판매자 프로필 수정
     *
     * @param sellerProfile the seller profile
     */
    public SellerProfile updateProfile(Long id, SellerProfile sellerProfile) {
        return marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_SELLER_PROFILE + "/" + id, null, HttpMethod.PUT, sellerProfile, SellerProfile.class);
    }

}
