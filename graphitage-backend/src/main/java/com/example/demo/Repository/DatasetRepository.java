package com.example.demo.Repository;

import com.example.demo.Model.Dataset;
import com.example.demo.Model.Library;
import com.example.demo.Model.Paper;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("datasetDAO")
public interface DatasetRepository extends Neo4jRepository<Dataset, Long> {

    Dataset save(Dataset dataset);

    List<Dataset> findAll();

    Optional<Dataset> findById(Long datasetId);

    @Query("MATCH (p:Paper)-[r]->(d:Dataset) WHERE d.datasetId = $0 return p")
    List<Paper> getPapersByDatasetId(Long datasetId);
}