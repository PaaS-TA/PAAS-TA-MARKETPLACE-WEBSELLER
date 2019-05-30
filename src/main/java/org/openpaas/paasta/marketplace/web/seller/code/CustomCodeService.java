package org.openpaas.paasta.marketplace.web.seller.code;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.RestTemplateService;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * Custom Code Service
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-08
 */
@Service
public class CustomCodeService {

    @Autowired
    private RestTemplateService marketApiRest;

    /**
     * GroupCode로 단위코드 목록 조회
     *
     * @param groupCode
     * @return CustomCodeList
     */
    public List<CustomCode> getUnitCodeListByGroupCode(String groupCode) {
    	CustomCodeList codeList = marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_WEB_CUSTOM_CODE + "/" + groupCode, HttpMethod.GET, null, CustomCodeList.class);
    	return codeList.getItems();
    }

}
