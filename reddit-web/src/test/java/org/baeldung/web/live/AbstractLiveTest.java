package org.baeldung.web.live;

import org.baeldung.reddit.util.Dto;

public abstract class AbstractLiveTest<D extends Dto> extends AbstractBaseLiveTest {

    protected abstract D newDto() throws Exception;

}
