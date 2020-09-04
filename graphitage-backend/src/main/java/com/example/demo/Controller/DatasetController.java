package com.example.demo.Controller;

import com.example.demo.Model.Dataset;
import com.example.demo.Model.Paper;
import com.example.demo.Service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api.graphitage.com/datasets")
public class DatasetController {

    private final DatasetService datasetService;

    @Autowired
    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @GetMapping
    public List<Dataset> getAllDatasets(){
        return datasetService.getAllDatasets();
    }

    @PostMapping
    public void addDataset(@RequestBody Dataset dataset){
        datasetService.addDataset(dataset);
    }

    @GetMapping("{datasetId}/papers")
    public List<Paper> getPapersByDatasetId(Long datasetId){
        return datasetService.getPapersByDatasetId(datasetId);
    }

    @DeleteMapping(path = "{datasetId}")
    public void deleteDatasetById(@PathVariable("datasetId") Long datasetId){
        datasetService.deleteDatasetById(datasetId);
    }

    @DeleteMapping(path = "{datasetId}/deleteDatasetRelationShip/{paperId}")
    public void deleteDatasetRelationShip(@PathVariable("datasetId") Long datasetId, @PathVariable("paperId") String paperId) {
        datasetService.deleteDatasetRelationShip(datasetId, paperId);
    }

    @PostMapping(path = "{datasetId}/addDatasetRelationShip/{paperId}")
    public void addDatasetRelationShip(@PathVariable("datasetId") Long datasetId, @PathVariable("paperId") String paperId) {
        datasetService.addDatasetRelationShip(datasetId, paperId);
    }
}
