package org.openpaas.paasta.marketplace.web.seller.service;

import org.openpaas.paasta.marketplace.web.seller.common.Constants;
import org.openpaas.paasta.marketplace.web.seller.model.ResultModel;
import org.openpaas.paasta.marketplace.web.seller.model.SellerProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 판매자 프로필 Service
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */
@Service
public class SellerProfileService {

    @Resource(name = "marketApiRest")
    RestTemplate marketApiRest;

    /**
     * 판매자 프로필 등록
     *
     * @param sellerProfile the seller profile
     * @return SellerProfile
     */
    public SellerProfile createProfile(SellerProfile sellerProfile) {
        return marketApiRest.postForObject("/profile", sellerProfile, SellerProfile.class);
    }


    /**
     * 판매자 프로필 상세 조회
     *
     * @param id the id
     * @return SellerProfile
     */
    public SellerProfile getProfile(Long id) {
        return marketApiRest.getForObject("/profile/" + id, SellerProfile.class);
    }


    /**
     * 판매자 프로필 수정
     *
     * @param sellerProfile the seller profile
     */
    public ResultModel updateProfile(Long id, SellerProfile sellerProfile) {
        ResultModel result = new ResultModel();

        try{
            marketApiRest.put("/profile/" + id, sellerProfile);
            result.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        }catch (Exception e){
            result.setResultCode(Constants.RESULT_STATUS_FAIL);
            result.setResultMessage(e.getMessage());
        }

        return result;
    }

//    public SellerProfile getProfileByUserId(String userId) {
//        return marketApiRest.getForObject("/profile/user/" + userId, SellerProfile.class);
//    }
}
