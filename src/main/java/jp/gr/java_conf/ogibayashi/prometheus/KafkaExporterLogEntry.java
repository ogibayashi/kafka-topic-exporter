package jp.gr.java_conf.ogibayashi.prometheus;

import java.util.Map;
import lombok.Data;
import lombok.NonNull;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@EqualsAndHashCode(exclude={"value"})
public class KafkaExporterLogEntry {
    @NonNull private String name;
    @NonNull private long value;
    private Map<String,String> labels;
}
