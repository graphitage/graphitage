package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@NodeEntity
public class Reader {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    @Property("id")
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Getter
    @Setter
    @Property("readerName")
    @JsonProperty("readerName")
    private String readerName;

    @Getter
    @Setter
    @Relationship(value = "HAS_READER", direction = INCOMING)
    @JsonProperty(value = "papers", access = JsonProperty.Access.WRITE_ONLY)
    private List<Paper> papers = new ArrayList<>();

    public Reader(String readerName) {
        this.readerName = readerName;
    }

}
