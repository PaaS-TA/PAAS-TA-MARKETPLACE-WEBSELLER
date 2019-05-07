package org.openpaas.paasta.marketplace.web.seller.model;

import lombok.Data;

/**
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-07
 */

@Data
public class SellerProfile extends AbstractEntity{

    private Long id;
    private String sellerName;
    private GroupType groupType;
    private String managerName;
    private String email;
    private String homepageUrl;

    public enum GroupType {
        publicEnterprise, enterprise, individual ,etc
    }
}
