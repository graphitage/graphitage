package com.example.demo.Model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.*;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;
import static org.neo4j.ogm.annotation.Relationship.OUTGOING;

@JsonPropertyOrder({"datasetId", "datasetName", "additionalInfo", "sizeInfo"})
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@NodeEntity("Dataset")
public class Dataset {

    @Id
    @Getter
    @Property("datasetId")
    @JsonProperty("datasetId")
    private Long datasetId;

    @Getter @Setter
    @Property("datasetName")
    @JsonProperty("datasetName")
    private String datasetName;

    @Getter @Setter
    @Property("additionalInfo")
    @JsonProperty("additionalInfo")
    private List<String> additionalInfo = new ArrayList<>();

    @Getter @Setter
    @JsonIgnoreProperties({"datasets", "dataset"})
    @JsonProperty("papers")
    @Relationship(value = "PREPROCESSING", direction = INCOMING)
    private List<Preprocessing> papers = new ArrayList<>();

    @Getter @Setter
    @Relationship(value = "SIZE_INFO")
    @JsonIgnoreProperties("datasets")
    @JsonProperty("sizeInfo")
    private List<DatasetSizeInfo> sizeInfo = new ArrayList<>();

    public Dataset(String datasetName, List<String> additionalInfo) {
        this.datasetName = datasetName;
        this.additionalInfo = additionalInfo;
    }
}
