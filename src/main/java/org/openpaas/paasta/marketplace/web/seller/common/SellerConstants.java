package org.openpaas.paasta.marketplace.web.seller.common;

/**
 * Constants 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2019.04.17
 */
public class SellerConstants {

	// common
    public static final String RESULT_STATUS_SUCCESS = "SUCCESS";
    public static final String RESULT_STATUS_FAIL = "FAIL";

    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String CF_AUTHORIZATION_HEADER_KEY = "cf-Authorization";
    public static final String CONTENT_TYPE = "Content-Type";

    public static final String TARGET_API_CF = "cfApi";
    public static final String TARGET_API_MARKET = "marketApi";

    // general data
    public static final String GROUP_CODE_BUSINESS_TYPE = "BUSINESS_TYPE";

    public static final String URI_DB_CUSTOM_CODE_LIST = "/db/customCode/{groupCode}";
    public static final String URI_DB_CATEGORY_LIST = "/db/category/list";
    public static final String URI_DB_CATEGORY_DETAIL = "/db/category/{id}/detail";

    // cf api uri
    public static final String MARKET_SELLER_URL = "/seller";

    // market api uri
    public static final String URI_API_BASE = "/api";
    public static final String URI_API_CUSTOM_CODE = "/api/customCode";
    public static final String URI_API_CATEGORY = "/api/category";
    public static final String URI_API_SELLER_PROFILE = "/api/seller/profile";

    // market web seller page uri
    public static final String URI_WEB_SELLER_PROFILE_DETAIL = "/seller/profile/{id}/detail";
    public static final String URI_WEB_SELLER_PROFILE_CREATE = "/seller/profile/create";
    public static final String URI_WEB_SELLER_PROFILE_UPDATE = "/seller/profile/{id}/update";

    // market web seller DB uri
    public static final String URI_DB_SELLER_PROFILE_DETAIL = "/db/seller/profile/{id}/detail";

    // market web seller view file uri
    public static final String URI_VIEW_PROFILE = "/profile";

    
    private SellerConstants() {
        throw new IllegalStateException();
    }

}
