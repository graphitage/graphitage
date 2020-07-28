package com.example.demo.Repository;

import com.example.demo.Model.Preprocessing;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("preprocessingDAO")
public interface PreprocessingRepository extends Neo4jRepository<Preprocessing, Long> {

    @Override
    Optional<Preprocessing> findById(Long aLong, int i);

    @Override
    Iterable<Preprocessing> findAll();

    @Override
    void delete(Preprocessing preprocessing);

    @Override
    void deleteById(Long aLong);
}
