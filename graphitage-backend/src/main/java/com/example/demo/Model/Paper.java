package com.example.demo.Model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.lang.reflect.Method;
import java.security.cert.PKIXParameters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@JsonPropertyOrder({"paperId", "paperIdType", "title"})
@Getter
@Setter
@NodeEntity("Paper")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class Paper extends PaperDetails implements Comparable<Paper> {

    @Id
    @Property("paperId")
    private String paperId;

    @Property("paperIdType")
    private String paperIdType;

    @Property("title")
    private String title;

    @JsonFormat(pattern = "yyyy")
    @JsonProperty("year")
    @Property("publishDate")
    private Date publishDate;


    @Property("keywords")
    private List<String> keywords;


    @Property("linkOfPaper")
    private String linkOfPaper;

    @Property("authors")
    private String authors;

    @JsonIgnoreProperties({"papers", "paper"})
    @Relationship(type = "PREPROCESSING")
    @JsonProperty("datasets")
    private List<Preprocessing> datasets = new ArrayList<>();

    @JsonProperty(value = "libraries")
    @Relationship(type = "HAS_LIBRARY")
    @JsonIgnoreProperties({"papers"})
    private List<Library> libraries = new ArrayList<>();

    @Setter
    @JsonProperty(value = "relatedWorks")
    @Relationship(type = "RELATED_WITH")
    @JsonIgnoreProperties({"relatedWorks"})
    private List<Paper> relatedWorks = new ArrayList<>();

    @JsonProperty(value = "reader")
    @Relationship(type = "HAS_READER")
    @JsonIgnoreProperties({"papers"})
    private List<Reader> readers = new ArrayList<>();


    public Paper() {
        super();
    }

    public Paper(String paperId, String paperIdType, String title, Date publishDate, List<String> keywords, String abstractOfPaper, List<String> targets, List<String> problems, List<String> summaries, List<String> components, List<String> applicationDomains, List<String> highlights, List<String> contributions, List<String> cons, List<String> pros, List<String> futureWorks, List<String> evaluationMetrics, String linkOfPaper, String authors, List<String> constraints, List<String> notes, List<String> comments) {
        super(abstractOfPaper, targets, problems, summaries, components, applicationDomains,
                highlights, contributions, cons, pros, futureWorks, evaluationMetrics,
                constraints, notes, comments);
        this.paperId = paperId;
        this.paperIdType = paperIdType;
        this.title = title;
        this.publishDate = publishDate;
        this.keywords = keywords;
        this.linkOfPaper = linkOfPaper;
        this.authors = authors;
    }

    public Paper(String paperId, String paperIdType, String authors, List<String> keywords,
                 String title, String abstractOfPaper, String linkOfPaper, Date publishDate) {//semantic icin
        super.setAbstractOfPaper(abstractOfPaper);
        this.paperId = paperId;
        this.paperIdType = paperIdType;
        this.title = title;
        this.keywords = keywords;
        this.authors = authors;
        this.linkOfPaper = linkOfPaper;
        this.publishDate = publishDate;
    }

    public Paper(String paperId, String paperIdType, String title) {//semantic referans icin
        this.paperId = paperId;
        this.paperIdType = paperIdType;
        this.title = title;
    }

    @Override
    public int compareTo(Paper o) {
        return this.getPaperId().compareTo(o.getPaperId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paper paper = (Paper) o;
        return paperId.equals(paper.paperId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperId);
    }

    /*
    * The non-null properties of the updatePaper are updated on the old paper.
    */
    public void merge(Paper updatePaper, List<String> withoutProperties) {

        Method methods[] = Paper.class.getMethods();
        /* All methods of the class have been traversed. */
        for (Method method : methods) {

            if (method.getName().startsWith("get")) {

                boolean isContinue = true;
                for (String withoutProperty : withoutProperties) {
                    if (method.getName().contains(withoutProperty)) {
                        isContinue = false;
                    }
                }
                /* Setter and getter functions of property not included in the withoutProperty list were determined. */
                if (isContinue) {
                    String getterMethodName = method.getName();
                    String setterMethodName = getterMethodName.replaceFirst("get", "set");

                    try {
                        /* The setter and receiver functions of the feature have been gotten. */
                        Method getterMethod = Paper.class.getMethod(getterMethodName, (Class<?>) null);
                        Method setterMethod = Paper.class.getMethod(setterMethodName, method.getReturnType());

                        /* If the feature to be updated is not null, the updating was performed with the setter function. */
                        Object value = getterMethod.invoke(updatePaper, (Object[]) null);
                        if (value != null) {
                            setterMethod.invoke(this, value);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }

    }
}
