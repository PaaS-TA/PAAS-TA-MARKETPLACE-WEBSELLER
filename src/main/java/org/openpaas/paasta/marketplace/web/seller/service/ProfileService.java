package org.openpaas.paasta.marketplace.web.seller.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    @Autowired
    private final RestTemplate paasApiRest;

    @SneakyThrows
    public List getProfiles() {
        Map map  = paasApiRest.getForObject("/profiles", Map.class);
        System.out.println(map.toString());
        return null;
    }

    public Profile updateProfiles(String id) {
        String url = UriComponentsBuilder.newInstance().path("/profiles/{id}")
                .build()
                .expand(id)
                .toString();

        return paasApiRest.getForObject(url, Profile.class);
    }
     /*

    //Page::Profile
    public CustomPage<Profile> getProfileList(String queryParamString) {
        ResponseEntity<CustomPage<Profile>> responseEntity = paasApiRest.exchange("/profiles/list" + queryParamString, HttpMethod.GET, null, new ParameterizedTypeReference<CustomPage<Profile>>() {});
        CustomPage<Profile> customPage = responseEntity.getBody();
        return customPage;
    }
    */

}
