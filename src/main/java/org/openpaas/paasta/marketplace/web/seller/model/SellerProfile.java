package org.openpaas.paasta.marketplace.web.seller.model;

import lombok.Data;

import java.util.List;

/**
 * 판매자 프로필 모델
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */
@Data
public class SellerProfile extends AbstractEntity{

    private Long id;
    private String userId;
    private String sellerName;
    private String businessType;
    private List<CustomCode> businessTypeList;
    private String managerName;
    private String email;
    private String homepageUrl;

}
