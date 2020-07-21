package com.example.demo.Repository;

import com.example.demo.Model.Library;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("libraryDAO")
public interface LibraryRepository extends Neo4jRepository<Library, Long> {

    List<Library> findAll();

}
