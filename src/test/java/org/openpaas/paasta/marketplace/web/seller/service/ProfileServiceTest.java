package org.openpaas.paasta.marketplace.web.seller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Profile;
import org.openpaas.paasta.marketplace.api.domain.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

@SuppressWarnings({ "unchecked" })
public class ProfileServiceTest extends AbstractMockTest {

    ProfileService profileService;

    @Mock
    Authentication authentication;

    @Mock
    OAuth2User principal;

    @Mock
    ResponseEntity<CustomPage<Profile>> profilePageResponse;

    @Mock
    CustomPage<Profile> profileCustomPage;

    boolean profileIdNull;

    boolean createProfileError;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        profileService = new ProfileService(paasApiRest);

        profileIdNull = false;
        createProfileError = false;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProfileList() {
        List<Profile> profileList = new ArrayList<>();
        Profile profile1 = profile("user-01");
        profileList.add(profile1);
        Profile profile2 = profile("user-02");
        profileList.add(profile2);

        when(paasApiRest.exchange(startsWith("/profiles/page"), eq(HttpMethod.GET), eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(profilePageResponse);

        when(profilePageResponse.getBody()).thenReturn(profileCustomPage);
        when(profileCustomPage.getContent()).thenReturn(profileList);

        CustomPage<Profile> result = profileService.getProfileList("?nameLike=user");
        assertEquals(profileList, result.getContent());
    }

    @Test
    public void user() {
        User user = user(userId);
        Map<String, Object> map = new TreeMap<>();
        map.put(user.getId(), user);

        when(paasApiRest.getForObject(eq("/users"), eq(Map.class))).thenReturn(map);

        List<?> result = profileService.user();
        assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void userError() {
        when(paasApiRest.getForObject(eq("/users"), eq(Map.class))).thenThrow(new IllegalStateException());

        profileService.user();
    }

    @Test
    public void createProfiles() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, Object> attributes = new TreeMap<>();
        attributes.put("user_name", userId);
        Profile profile1 = profile("user-01");
        if (profileIdNull) {
            profile1.setId(null);
        }

        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getAttributes()).thenReturn(attributes);
        when(paasApiRest.postForObject(eq("/profiles"), any(Profile.class), eq(Profile.class))).thenReturn(profile1);
        if (createProfileError) {
            when(paasApiRest.postForObject(eq("/profiles"), any(Profile.class), eq(Profile.class)))
                    .thenThrow(new IllegalStateException());
        }

        Profile result = profileService.createProfiles(profile1);
        assertEquals(profile1, result);
    }

    @Test
    public void createProfilesProfileIdNull() {
        profileIdNull = true;

        createProfiles();
    }

    @Test(expected = RuntimeException.class)
    public void createProfilesError() {
        createProfileError = true;

        createProfiles();
    }

    @Test
    public void getProfile() {
        Profile profile1 = profile("user-01");

        when(paasApiRest.getForObject(eq("/profiles/" + userId), eq(Profile.class))).thenReturn(profile1);

        Profile result = profileService.getProfile(userId);
        assertEquals(profile1, result);
    }

    @Test
    public void updateProfile() {
        Profile profile1 = profile("user-01");

        doNothing().when(paasApiRest).put(eq("/profiles/" + userId), eq(Profile.class));
        when(paasApiRest.getForObject(eq("/profiles/" + userId), eq(Profile.class))).thenReturn(profile1);

        Profile result = profileService.updateProfiles(userId, profile1);
        assertEquals(profile1, result);
    }

}
