package org.baeldung.web.controller.general;

import java.util.HashMap;
import java.util.Map;

import org.baeldung.web.metric.IMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class MetricController {

    @Autowired
    private IMetricService metricService;

    //

    @RequestMapping(value = "/metric", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, HashMap<Integer, Integer>> getMetric() {
        return metricService.getFullMetric();
    }

    @RequestMapping(value = "/status-metric", method = RequestMethod.GET)
    @ResponseBody
    public Map<Integer, Integer> getStatusMetric() {
        return metricService.getStatusMetric();
    }

    @RequestMapping(value = "/metric-graph-data", method = RequestMethod.GET)
    @ResponseBody
    public Object[][] getMetricGraphData() {
        final Object[][] result = metricService.getGraphData();
        for (int i = 1; i < result[0].length; i++) {
            result[0][i] = result[0][i].toString();
        }
        return result;
    }

}
