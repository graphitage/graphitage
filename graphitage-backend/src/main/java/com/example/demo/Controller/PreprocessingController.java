package com.example.demo.Controller;

import com.example.demo.Service.PreprocessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api.graphitage.com/preprocessing")
public class PreprocessingController {

    private final PreprocessingService preprocessingService;

    @Autowired
    public PreprocessingController(PreprocessingService preprocessingService) {
        this.preprocessingService = preprocessingService;
    }

    @GetMapping("{preprocessingId}/getProcessingSteps")
    public List<String> getPreprocessingSteps(@PathVariable("preprocessingId") Long preprocessingId)
    {
        return preprocessingService.getPreprocessingSteps(preprocessingId);
    }

}
