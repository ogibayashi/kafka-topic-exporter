package jp.gr.java_conf.ogibayashi.prometheus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prometheus.client.Collector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

public class KafkaCollector extends Collector {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaCollector.class);

    private ObjectMapper mapper = new ObjectMapper();
    private List<MetricFamilySamples> mfsList = new ArrayList<MetricFamilySamples>();
    private Map<String, MetricFamilySamples> metricFamilySamplesMap =
        new HashMap<>();    
    
    public KafkaCollector() {
    }
    
    public void add(String topic, String recordValue) {
        LOG.debug("add: {}, {}", topic, recordValue);
        try {          
            KafkaExporterLogEntry record = mapper.readValue(recordValue, KafkaExporterLogEntry.class);
            String metricName = topic.replaceAll("\\.","_") + "_" + record.getName();

            ArrayList<String> labelNames = new ArrayList<String>();
            ArrayList<String> labelValues = new ArrayList<String>();
            if (record.getLabels() != null) {
                for(Map.Entry<String, String> entry: record.getLabels().entrySet()){
                    labelNames.add(entry.getKey());
                    labelValues.add(entry.getValue());
                }
            }
            MetricFamilySamples mfs = metricFamilySamplesMap.get(metricName);
            MetricFamilySamples newMfs;

            // TODO: refine the code
            MetricFamilySamples.Sample sampleToAdd = new MetricFamilySamples.Sample(metricName, labelNames, labelValues, record.getValue());
            if(mfs == null) {
                newMfs = new MetricFamilySamples(metricName, Type.GAUGE, "",
                                                 Collections.singletonList(sampleToAdd));
            }
            else {
                List<MetricFamilySamples.Sample> samples = new ArrayList(mfs.samples);
                MetricFamilySamples.Sample toRemove = null;
                for(MetricFamilySamples.Sample s: samples) {
                    if (MetricUtil.getLabelMapFromSample(s).equals(record.getLabels())) {
                        toRemove = s;
                    }
                }
                samples.remove(toRemove);
                samples.add(sampleToAdd);
                newMfs = new MetricFamilySamples(metricName, Type.GAUGE, "", samples);
            }
            
            metricFamilySamplesMap.put(metricName, newMfs);
        }
        catch(JsonMappingException e){
            LOG.warn("Invalid record: " + recordValue);
        }
        catch(Exception e){
            LOG.error("Error happened in adding record to the collector", e);
        }
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfsList = new ArrayList<MetricFamilySamples>();
        mfsList.addAll(metricFamilySamplesMap.values());

        return mfsList;
    }
}
