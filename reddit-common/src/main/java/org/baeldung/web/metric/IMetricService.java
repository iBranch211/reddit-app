package org.baeldung.web.metric;

import java.util.HashMap;
import java.util.Map;

public interface IMetricService {

    void increaseCount(final String request, final int status);

    Map<String, HashMap<Integer, Integer>> getFullMetric();

    Map<Integer, Integer> getStatusMetric();

    Object[][] getGraphData();
}
