package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@RelationshipEntity(type = "PREPROCESSING")
public class Preprocessing {

    @Id
    @GeneratedValue
    @JsonProperty(value = "preprocessingId", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Getter @Setter
    @JsonProperty("preprocessingSteps")
    private List<String> preprocessingSteps = new ArrayList<>();

    @Getter @Setter
    @StartNode
    @JsonProperty(value = "paper")
    private Paper paper;

    @Getter @Setter
    @EndNode
    @JsonProperty(value = "dataset")
    private Dataset dataset;

    public Preprocessing(List<String> preprocessingSteps, Paper paper, Dataset dataset) {
        this.preprocessingSteps = preprocessingSteps;
        this.paper = paper;
        this.dataset = dataset;
    }
}
