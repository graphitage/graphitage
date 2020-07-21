package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.Property;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaperDetails {

    @Property("abstractOfPaper")
    private String abstractOfPaper;

    @Property("targets")
    private List<String> targets;

    @Property("problems")
    private List<String> problems;

    @Property("summaries")
    private List<String> summaries;

    @Property("components")
    private List<String> components;

    @Property("applicationDomains")
    private List<String> applicationDomains;

    @Property("highlights")
    private List<String> highlights;

    @Property("contributions")
    private List<String> contributions;

    @Property("cons")
    private List<String> cons;

    @Property("pros")
    private List<String> pros;

    @Property("futureWorks")
    private List<String> futureWorks;

    @JsonProperty("evaluations")
    @Property("evaluationMetrics")
    private List<String> evaluationMetrics;

    @Property("constraints")
    private List<String> constraints;

    @Property("notes")
    private List<String> notes;

    @Property("comments")
    private List<String> comments;
}
