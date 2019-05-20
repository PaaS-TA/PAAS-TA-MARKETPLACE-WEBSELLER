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
    private RestTemplate marketApiRest;

    /**
     * GroupCode로 단위코드 목록 조회
     *
     * @param groupCode
     * @return List<CustomCode>
     */
    public List<CustomCode> getUnitCodeListByGroupCode(String groupCode) {
        return marketApiRest.getForObject("/customCode/" + groupCode, List.class);
    }

}
