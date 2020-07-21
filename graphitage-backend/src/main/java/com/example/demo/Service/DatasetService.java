package com.example.demo.Service;

import com.example.demo.Model.Dataset;
import com.example.demo.Model.Paper;
import com.example.demo.Model.Preprocessing;
import com.example.demo.Repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DatasetService {

    private final DatasetRepository datasetRepository;

    @Autowired
    public DatasetService(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    public List<Dataset> getAllDatasets() {
        return datasetRepository.findAll();
    }

    public void addDataset(Dataset dataset) {
        datasetRepository.save(dataset);
    }

    public List<Paper> getPapersByDatasetId(Long datasetId) {
        return datasetRepository.getPapersByDatasetId(datasetId);
    }

    public void deleteDatasetById(Long datasetId) {
        datasetRepository.deleteById(datasetId);
    }
}
