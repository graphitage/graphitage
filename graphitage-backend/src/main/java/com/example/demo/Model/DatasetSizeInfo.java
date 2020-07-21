package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;


import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NoArgsConstructor
@NodeEntity("DatasetSizeInfo")
public class DatasetSizeInfo {

    @Id
    @GeneratedValue
    @Getter
    @Property("id")
    @JsonProperty("id")
    private Long id;

    @Getter @Setter
    @JsonProperty("type")
    @Property("type")
    private String type;

    @Getter @Setter
    @JsonProperty("size")
    @Property("size")
    private String size;

    @Getter @Setter
    @Relationship(value = "SIZE_INFO", direction = INCOMING)
    @JsonIgnoreProperties("size_info")
    private List<Dataset> datasets = new ArrayList<>();

    public DatasetSizeInfo(String type, String size) {
        this.type = type;
        this.size = size;
    }
}
