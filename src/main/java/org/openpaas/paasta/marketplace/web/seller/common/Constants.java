package org.openpaas.paasta.marketplace.web.seller.common;

/**
 * Constants 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2019.04.17
 */
public class Constants {

    // COMMON
    public static final String RESULT_STATUS_SUCCESS = "SUCCESS";
    public static final String RESULT_STATUS_FAIL = "FAIL";


    public static final String TARGET_CF_API = "cfApi";
    public static final String TARGET_MARKET_API = "marketApi";

    public static final String MARKET_BASE_URL = "/";
    public static final String MARKET_INIT_URL = "/seller";

    public static final String BUSINESS_GROUP_TYPE = "BUSINESS_TYPE";


    // Market WEB SELLER URI
    public static final String URI_SELLER_PROFILE = "/seller/profile";


    // Market API URI
    public static final String URI_MARKET_API_PROFILE = "/api/seller/profile";
    public static final String URI_MARKET_API_CODE = "/api/seller/customCode";


    private Constants() {
        throw new IllegalStateException();
    }

}
