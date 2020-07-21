package com.example.demo.Repository;

import com.example.demo.Model.Reader;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("readerDAO")
public interface ReaderRepository extends Neo4jRepository<Reader, Long> {

    List<Reader> findAll();

}
