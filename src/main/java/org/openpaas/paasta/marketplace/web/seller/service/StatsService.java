package org.openpaas.paasta.marketplace.web.seller.service;

import com.sun.org.glassfish.external.statistics.Stats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    @Autowired
    private final RestTemplate paasApiRest;

    public CustomPage<Stats> getStatsList(String queryParamString) {
        ResponseEntity<CustomPage<Stats>> responseEntity = paasApiRest.exchange("/profiles/page" + queryParamString, HttpMethod.GET, null, new ParameterizedTypeReference<CustomPage<Stats>>() {});
        CustomPage<Stats> customPage = responseEntity.getBody();
        return customPage;
    }

}
