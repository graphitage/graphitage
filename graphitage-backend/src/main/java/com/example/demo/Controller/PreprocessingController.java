package com.example.demo.Controller;

import com.example.demo.Service.PreprocessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
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
