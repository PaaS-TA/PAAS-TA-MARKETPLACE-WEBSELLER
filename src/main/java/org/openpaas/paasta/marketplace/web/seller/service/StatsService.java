package org.openpaas.paasta.marketplace.web.seller.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.Category;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Instance;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    @Autowired
    private final RestTemplate paasApiRest;

    @SneakyThrows
    public List<Category> getCategories() {
        return paasApiRest.getForObject("/categories", List.class);
    }


    /**
     * 사용자 수 목록 조회
     *
     * @param idIn
     * @return
     */
    public Map<Long, Long> getCountsOfInsts(List<Long> idIn) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance().path("/stats/instances/my/counts/ids");
        for (Long id : idIn) {
            builder.queryParam("idIn", id);
        }
        String url = builder.buildAndExpand().toUriString();

        return paasApiRest.getForObject(url, Map.class);
    }

    /**
     * 과거 사용량 추이 조회
     *
     * @return
     */
    public Map<String, Object> countsOfInstsProviderMonthly(List<Long> idIn) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance().path("/stats/instances/my/counts/months");
        for (Long id : idIn) {
            builder.queryParam("idIn", id);
        }
        String url = builder.buildAndExpand().toUriString();

        return paasApiRest.getForObject(url, Map.class);
    }

    /**
     * 판매된 상품 총 개수 조회
     *
     * @return
     */
    public long getCountOfInstsUsing() {
        return paasApiRest.getForObject("/stats/instances/my/counts/sum", long.class);
    }

    /**
     * 상품 판매 현황 목록 조회
     *
     * @param queryString
     * @return
     */
    public CustomPage<Instance> getInstanceListBySwId(String queryString) {
        ResponseEntity<CustomPage<Instance>> responseEntity = paasApiRest.exchange("/instances/page" + queryString, HttpMethod.GET, null, new ParameterizedTypeReference<CustomPage<Instance>>() {});
        CustomPage<Instance> customPage = responseEntity.getBody();

        System.out.println("getContent ::: " + customPage.getContent());
        System.out.println("getTotalElements ::: " + customPage.getTotalElements());
        return customPage;
    }

    /**
     * 총 사용자 수 조회
     *
     * @return
     */
    public long getCountOfUsersUsing() {
        return paasApiRest.getForObject("/stats/users/my/counts/sum", long.class);
    }

    public CustomPage<Software> getSoftwareList(String queryParamString) {
        ResponseEntity<CustomPage<Software>> responseEntity = paasApiRest.exchange(" /stats/softwares/my" + queryParamString, HttpMethod.GET, null, new ParameterizedTypeReference<CustomPage<Software>>() {});
        CustomPage<Software> customPage = responseEntity.getBody();

        System.out.println("getContent ::: " + customPage.getContent());
        System.out.println("getTotalElements ::: " + customPage.getTotalElements());
        return customPage;
    }

    /**
     * 인스턴스(상품) 조회
     *
     * @return
     */
    public Instance getInstance(Long id) {
        String url = UriComponentsBuilder.newInstance().path("/instances/{id}")
                .build()
                .expand(id)
                .toString();

        return paasApiRest.getForObject(url, Instance.class);
    }


    /**
     * 판매자의 상품별 총 판매량(사용 + 중지)
     *
     * @param idIn
     * @return
     */
    public Map<Long, Object> soldInstanceByProvider(List<Long> idIn) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance().path("/stats/instances/sold/count");
        for (Long id : idIn) {
            builder.queryParam("idIn", id);
        }
        String url = builder.buildAndExpand().toUriString();

        return paasApiRest.getForObject(url, Map.class);
    }
}
