package com.example.demo.Service;

import com.example.demo.Model.Preprocessing;
import com.example.demo.Repository.PreprocessingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PreprocessingService {


    private final PreprocessingRepository preprocessingRepository;

    public PreprocessingService(@Qualifier("preprocessingDAO") PreprocessingRepository preprocessingRepository) {
        this.preprocessingRepository = preprocessingRepository;
    }

    public List<String> getPreprocessingSteps(Long preprocessingId) {
        Optional<Preprocessing> paperPreprocessing = preprocessingRepository.findById(preprocessingId);
        if(paperPreprocessing.isPresent()){
            return paperPreprocessing.get().getPreprocessingSteps();
        }
        else{
            return new ArrayList<>();
        }

    }

}
