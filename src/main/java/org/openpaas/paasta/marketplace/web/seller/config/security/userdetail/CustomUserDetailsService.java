package org.openpaas.paasta.marketplace.web.seller.config.security.userdetail;

import org.openpaas.paasta.marketplace.web.seller.security.SsoAuthenticationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomUserDetails service 클래스.
 *
 * @author hrjin
 * @version 1.0
 * @since 2019.03.22
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private String token;

    public void setToken(String token){
        this.token = token;
    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserBySsoAuthenticationDetails(SsoAuthenticationDetails ssoAuthenticationDetails) {
        List role = new ArrayList();

        LOGGER.info("username : "+ssoAuthenticationDetails.getUserId());
        LOGGER.info("uaaId : "+ssoAuthenticationDetails.getId());
        LOGGER.info("token : "+ssoAuthenticationDetails.getAccessToken());
        LOGGER.info("Instance id : "+ssoAuthenticationDetails.getManagingServiceInstance());

        String username = ssoAuthenticationDetails.getUserId();
        String uaaid = ssoAuthenticationDetails.getId();
        String token = ssoAuthenticationDetails.getAccessToken().toString();
        //String serviceInstanceId = ssoAuthenticationDetails.getManagingServiceInstance();

        role.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        User user = new User(username, "N/A", role);
        //user.setServiceInstanceId(ssoAuthenticationDetails.getManagingServiceInstance());
        //user.setOrganizationGuid(organization_guid);
        //user.setSpaceGuid(space_guid);
        return user;
    }


}
