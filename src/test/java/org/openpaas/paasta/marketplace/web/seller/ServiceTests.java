package org.openpaas.paasta.marketplace.web.seller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openpaas.paasta.marketplace.web.seller.service.ProfileServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
        // @formatter:off
        ProfileServiceTest.class,
        // @formatter:on
})
public class ServiceTests {
}
