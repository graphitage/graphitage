package com.example.demo.Repository;

import com.example.demo.Model.Reader;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("readerDAO")
public interface ReaderRepository extends Neo4jRepository<Reader, Long> {

    List<Reader> findAll();

    Optional<Reader> findById(Long readerId);

    @Query("MATCH (r:Reader) WHERE toLower(r.readerName) = toLower($0) RETURN r")
    Optional<Reader> findByName(String readerName);
}
