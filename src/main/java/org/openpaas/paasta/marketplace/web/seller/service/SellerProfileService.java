package org.openpaas.paasta.marketplace.web.seller.service;

import org.openpaas.paasta.marketplace.web.seller.model.SellerProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */

@Service
public class SellerProfileService {

    @Resource(name = "marketApiRest")
    RestTemplate marketApiRest;

    public SellerProfile createProfile(SellerProfile sellerProfile) {
        return marketApiRest.postForObject("/profile", sellerProfile, SellerProfile.class);
    }
}
