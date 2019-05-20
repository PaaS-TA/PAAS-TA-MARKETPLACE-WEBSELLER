package org.openpaas.paasta.marketplace.web.seller.controller;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.Constants;
import org.openpaas.paasta.marketplace.web.seller.model.CustomCode;
import org.openpaas.paasta.marketplace.web.seller.service.CustomCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Custom Code Controller
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-08
 */
@RestController
public class CustomCodeController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCodeController.class);

    @Autowired
    private CustomCodeService customCodeService;

    /**
     * GroupCode로 단위코드 목록 조회
     *
     * @param groupCode
     * @return List<CustomCode>
     */
    @GetMapping(value = Constants.URI_MARKET_API_CODE + "/{groupCode}")
    public List<CustomCode> getUnitCodeListByGroupCode(@PathVariable String groupCode){
        return customCodeService.getUnitCodeListByGroupCode(groupCode);
    }

}
