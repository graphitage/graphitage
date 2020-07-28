package com.example.demo.Repository;

import com.example.demo.Model.Dataset;
import com.example.demo.Model.Library;
import com.example.demo.Model.Paper;
import com.example.demo.Model.Reader;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("paperDAO")
public interface PaperRepository extends Neo4jRepository<Paper, String>{

    List<Paper> findAll();

    Optional<Paper> findById(String paperID);

    boolean existsById(String paperId);

    Paper save(Paper paper); // it adds a paper to database

    void deleteById(String paperId);

    @Query("MATCH (p:Paper)-[r:HAS_LIBRARY]->(n:Library) WHERE p.paperId = $0 AND ID(n) = $1 delete r")
    void deleteLibraryRelationship(String paperId, Long libraryId);

    @Query("MATCH (p:Paper)-[r:HAS_READER]->(n:Reader) WHERE p.paperId = $0 AND ID(n) = $1 delete r")
    void deleteReaderRelationship(String paperId, Long readerId);

    @Query("MATCH (p:Paper) WHERE p.paperId = $0 RETURN p.keywords")
    List<String> findKeywordsById(String paperId);

    @Query("MATCH (p:Paper)-[r:HAS_DATASET]-(n) WHERE p.paperId = $0 RETURN n")
    List<Dataset> findDatasetsById(String paperId);

    @Query("MATCH (p:Paper)-[r:HAS_LIBRARY]-(n) WHERE p.paperId = $0 RETURN n")
    List<Library> getLibrariesById(String paperId);

    @Query("MATCH (p:Paper)-[r:HAS_READER]-(n) WHERE p.paperId = $0 RETURN n")
    List<Reader> getReadersById(String paperId);

    @Query("MATCH (p:Paper)-[r:RELATED_WITH]-(n) WHERE p.paperId = $0 RETURN n")
    List<Paper> getRelatedWorksByID(String paperId);

    @Query("MATCH (p:Paper)-[r:RELATED_WITH]-(n) WHERE p.keywords n.keywords RETURN n")
    List<Paper> getRelatedPapersByKeyword(String paperId);

    @Query("MATCH (p:Paper) WHERE toLower(p.title) CONTAINS toLower($0) return p")
    List<Paper> paperSearchWithTitle(String title);

    @Query("MATCH (p:Paper) WHERE p.publishDate > $0 return p")
    List<Paper> paperSearchWithPublishDate(String publishDate);

    @Query("MATCH (p:Paper) WHERE any(keyword in p.keywords WHERE toLower(keyword) CONTAINS toLower($0)) return p")
    List<Paper> paperSearchWithKeyword(String keyword);

    @Query("MATCH (p:Paper)-[r:HAS_READER]->(n) WHERE n.readerName = $0 return p")
    List<Paper> paperSearchWithReaderName(String readerName);

    @Query("MATCH (p:Paper)-[r:HAS_DATASET]->(n) WHERE n.datasetName = $0 return p")
    List<Paper> paperSearchWithDatasetName(String datasetName);

    @Query("MATCH (p:Paper)-[r:HAS_LIBRARY]->(n) WHERE n.libraryName = $0 return p")
    List<Paper> paperSearchWithLibraryName(String libraryName);


}
