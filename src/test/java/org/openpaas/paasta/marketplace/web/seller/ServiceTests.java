package org.openpaas.paasta.marketplace.web.seller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openpaas.paasta.marketplace.web.seller.service.ProfileServiceTest;
import org.openpaas.paasta.marketplace.web.seller.service.SoftwareServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
        // @formatter:off
        ProfileServiceTest.class,
        SoftwareServiceTest.class,
        // @formatter:on
})
public class ServiceTests {
}
