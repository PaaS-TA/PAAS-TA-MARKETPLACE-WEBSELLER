package org.openpaas.paasta.marketplace.web.seller.controller;

import org.openpaas.paasta.marketplace.web.seller.common.Constants;
import org.openpaas.paasta.marketplace.web.seller.model.CustomCode;
import org.openpaas.paasta.marketplace.web.seller.service.CustomCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Custom Code Controller
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-08
 */
@RestController
public class CustomCodeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCodeController.class);

    @Autowired
    private CustomCodeService customCodeService;

    /**
     * Group Type Name 으로 Group Code 목록 조회
     *
     * @param groupTypeName the group type name
     * @return List<CustomCode>
     */
    @GetMapping(value = Constants.URI_MARKET_API_CODE + "/{groupTypeName}")
    public List<CustomCode> getGroupCodeListByGroupName(@PathVariable String groupTypeName){
        return customCodeService.getGroupCodeListByGroupName(groupTypeName);
    }
}
