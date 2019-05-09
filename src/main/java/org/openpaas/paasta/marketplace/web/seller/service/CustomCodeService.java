package org.openpaas.paasta.marketplace.web.seller.service;

import org.openpaas.paasta.marketplace.web.seller.model.CustomCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * Custom Code Service
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-08
 */
@Service
public class CustomCodeService {

    @Resource(name = "marketApiRest")
    RestTemplate marketApiRest;

    /**
     * Group Type Name 으로 Group Code 목록 조회
     *
     * @param groupTypeName the group type name
     * @return List<CustomCode>
     */
    public List<CustomCode> getGroupCodeListByGroupName(String groupTypeName) {
        return marketApiRest.getForObject("/customCode/" + groupTypeName, List.class);
    }
}
