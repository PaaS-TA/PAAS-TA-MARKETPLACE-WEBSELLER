package org.openpaas.paasta.marketplace.web.seller.storageApi.root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;


@Controller
public class RootController {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////
    @Value("${objectStorage.swift.tenantName}")
    String tenantName;
    @Value("${objectStorage.swift.username}")
    String username;
    @Value("${objectStorage.swift.authUrl}")
    String authUrl;

    @Value("${objectStorage.swift.authMethod}")
    String authMethod;
    @Value("${objectStorage.swift.container}")
    String container;
    @Value("${objectStorage.swift.preferredRegion}")
    String preferredRegion;

}
