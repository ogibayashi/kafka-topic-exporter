package jp.gr.java_conf.ogibayashi.prometheus;

import io.prometheus.client.Collector;
import java.util.HashMap;
import java.util.Map;

public final class MetricUtil {
    public static Map<String, String> getLabelMapFromSample(Collector.MetricFamilySamples.Sample sample)  {
        Map<String, String> ret = new HashMap();
        
        for (int i = 0; i < sample.labelNames.size() && i < sample.labelValues.size(); i++) {
            ret.put(sample.labelNames.get(i), sample.labelValues.get(i));
        }
        return ret;
    }
}
