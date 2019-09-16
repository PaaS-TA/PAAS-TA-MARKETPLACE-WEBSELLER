package org.openpaas.paasta.marketplace.web.seller.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.Category;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.openpaas.paasta.marketplace.api.domain.SoftwareHistory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SoftwareService {

    private final RestTemplate paasApiRest;

    @SneakyThrows
    public Software createSoftware(Software software) {
        return paasApiRest.postForObject("/softwares", software, Software.class);
    }

    @SneakyThrows
    public List<Category> getCategories() {
        return paasApiRest.getForObject("/categories", List.class);
    }


    public CustomPage<Software> getSoftwareList(String queryParamString) {
        ResponseEntity<CustomPage<Software>> responseEntity = paasApiRest.exchange("/softwares/my/page" + queryParamString, HttpMethod.GET, null, new ParameterizedTypeReference<CustomPage<Software>>() {});
        CustomPage<Software> customPage = responseEntity.getBody();
        return customPage;
    }


    public Software getSoftware(Long id) {
        String url = UriComponentsBuilder.newInstance().path("/softwares/{id}")
                .build()
                .expand(id)
                .toString();

        return paasApiRest.getForObject(url, Software.class);
    }


    public Software updateSoftware(Long id, Software software) {
        String url = UriComponentsBuilder.newInstance().path("/softwares/{id}")
                .build()
                .expand(id)
                .toString();

        log.info("updateSoftware url :: " + url + " Software " + software.toString());

        paasApiRest.put(url, software);
        return software;
    }


    public List<SoftwareHistory> getHistoryList(Long id, String queryParamString) {
        return paasApiRest.getForObject("/softwares/" + id + "/histories" + queryParamString, List.class);
    }

}
