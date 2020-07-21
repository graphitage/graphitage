package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@NodeEntity("Library")
public class Library {

    @Id
    @GeneratedValue
    @Getter
    @Property("id")
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Getter @Setter
    @Property("libraryName")
    @JsonProperty(value = "name")
    private String libraryName;

    @Getter @Setter
    @Property("libraryLink")
    @JsonProperty(value = "link")
    private String libraryLink;

    @Getter @Setter
    @Relationship(type = "HAS_LIBRARY", direction = INCOMING)
    @JsonProperty(value = "papers", access = JsonProperty.Access.WRITE_ONLY)
    private List<Paper> papers = new ArrayList<>();


    public Library(String libraryName, String libraryLink) {
        this.libraryName = libraryName;
        this.libraryLink = libraryLink;
    }
}
