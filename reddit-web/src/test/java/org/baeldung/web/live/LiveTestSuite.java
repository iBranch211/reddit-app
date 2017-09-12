package org.baeldung.web.live;

import org.baeldung.web.FallbackLiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ // @formatter:off
    FallbackLiveTest.class,
    MyFeedLiveTest.class,
    ResubmitOptionsLiveTest.class,
    ScheduledPostLiveTest.class,
    UserPreferenceLiveTest.class
}) // @formatter:on
public class LiveTestSuite {
}