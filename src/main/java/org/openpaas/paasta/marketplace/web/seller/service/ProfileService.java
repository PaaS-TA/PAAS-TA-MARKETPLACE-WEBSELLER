package org.openpaas.paasta.marketplace.web.seller.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Profile;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.openpaas.paasta.marketplace.api.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
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
public class ProfileService {

    @Autowired
    private final RestTemplate paasApiRest;

    //CustomPage<Profile> :: 리스트
    public CustomPage<Profile> getProfileList(String queryParamString) {
        ResponseEntity<CustomPage<Profile>> responseEntity = paasApiRest.exchange("/profiles/page" + queryParamString, HttpMethod.GET, null, new ParameterizedTypeReference<CustomPage<Profile>>() {});
        CustomPage<Profile> customPage = responseEntity.getBody();
        return customPage;
    }


    @SneakyThrows
    public List user() {
        log.info("profileService.users :: " + User.class.toString());
        Map map  = paasApiRest.getForObject("/users", Map.class);
        System.out.println(map.toString());
        return null;
    }


    @SneakyThrows
    public Profile createProfiles(Profile profile) {
        log.info("createProfiles.profiles :: " + Profile.class.toString());
        return paasApiRest.postForObject("/profiles", profile, Profile.class);
    }


    public Profile getProfile(String id) {
        String url = UriComponentsBuilder.newInstance().path("/profiles/{id}")
                .build()
                .expand(id)
                .toString();

        log.info("getProfile url :: " + url + " profile " + Profile.class);
        return paasApiRest.getForObject(url, Profile.class);
    }


    public Profile updateProfiles(String id, Profile profile) {
        String url = UriComponentsBuilder.newInstance().path("/profiles/{id}")
                .build()
                .expand(id)
                .toString();

        log.info("getProfile url :: " + url + " profile " + profile);
        paasApiRest.put(url, profile);
        return getProfile(id);

    }


}
