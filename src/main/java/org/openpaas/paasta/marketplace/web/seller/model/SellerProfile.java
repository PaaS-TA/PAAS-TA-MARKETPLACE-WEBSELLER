package org.openpaas.paasta.marketplace.web.seller.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 판매자 프로필 모델
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SellerProfile extends BaseModel {

    private String id;
    private String sellerName;
    private String businessType;
    private String managerName;
    private String email;
    private String homepageUrl;

//    private List<CustomCode> businessTypeList;

}
