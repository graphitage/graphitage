package com.example.demo.Service;

import com.example.demo.Model.Dataset;
import com.example.demo.Model.Library;
import com.example.demo.Model.Paper;
import com.example.demo.Model.Preprocessing;
import com.example.demo.Repository.DatasetRepository;
import com.example.demo.Repository.PaperRepository;
import com.example.demo.Repository.PreprocessingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final PaperRepository paperRepository;
    private final PreprocessingRepository preprocessingRepository;
    Logger logger = LoggerFactory.getLogger(DatasetService.class);

    @Autowired
    public DatasetService(DatasetRepository datasetRepository, PaperRepository paperRepository, PreprocessingRepository preprocessingRepository) {
        this.datasetRepository = datasetRepository;
        this.paperRepository = paperRepository;
        this.preprocessingRepository = preprocessingRepository;
    }

    public List<Dataset> getAllDatasets() {
        return datasetRepository.findAll();
    }

    public void addDataset(Dataset dataset) {
        datasetRepository.save(dataset);
    }

    public List<Paper> getPapersByDatasetId(Long datasetId) {

            Optional<Dataset> datasetOptional = datasetRepository.findById(datasetId);
            List<Paper> paperList = new ArrayList<>();
            if (datasetOptional.isPresent()) {
                List<Preprocessing> preprocessingList = datasetOptional.get().getPapers();
                for(Preprocessing preprocessing: preprocessingList){
                    paperList.add(preprocessing.getPaper());
                }
            }
            return paperList;
    }

    public void deleteDatasetById(Long datasetId) {
        datasetRepository.deleteById(datasetId);
    }

    public void deleteDatasetRelationShip(Long datasetId, String paperId) {
        Optional<Dataset> dataset = datasetRepository.findById(datasetId);
        if (dataset.isPresent()) {

            boolean isContains = false;
            List<Preprocessing> preprocessingList = dataset.get().getPapers();

            for (Preprocessing preprocessing: preprocessingList) {
                if ( preprocessing.getPaper().getPaperId().equals(paperId) ) {
                    isContains = true;
                    preprocessingRepository.deleteById(preprocessing.getId());
                }
            }

            if (!isContains) {
                logger.warn("[DATASET SERVICE - DELETE DATASET RELATIONSHIP] There is no dataset relationship between dataset with id " + datasetId.toString() + " and paper with id " + paperId);
            }

        } else {
            logger.warn("[DATASET SERVICE - DELETE DATASET RELATIONSHIP] Dataset with id " + datasetId.toString() + " does not exists in the database.");
        }
    }

    public void addDatasetRelationShip(Long datasetId, String paperId) {
        Optional<Dataset> dataset = datasetRepository.findById(datasetId);
        if (dataset.isPresent()) {

            Optional<Paper> paper = paperRepository.findById(paperId);
            if (paper.isPresent()) {

                boolean isContains = false;
                List<Preprocessing> preprocessingList = dataset.get().getPapers();

                for (Preprocessing preprocessing: preprocessingList) {
                    if ( preprocessing.getPaper().getPaperId().equals(paperId) ) {
                        isContains = true;
                    }
                }

                if (!isContains) {

                    Preprocessing newPreprocessing = new Preprocessing();
                    newPreprocessing.setPaper(paper.get());
                    newPreprocessing.setDataset(dataset.get());

                    preprocessingList.add(newPreprocessing);
                    dataset.get().setPapers(preprocessingList);
                    datasetRepository.save(dataset.get());

                } else {
                    logger.warn("[DATASET SERVICE - ADD DATASET RELATIONSHIP] The relationship between dataset with id " + datasetId.toString() + " and paper with id " + paperId + " already exists in the database.");
                }

            } else {
                logger.warn("[DATASET SERVICE - ADD DATASET RELATIONSHIP] Paper with id " + paperId + " does not exists in the database.");
            }

        } else {
            logger.warn("[DATASET SERVICE - ADD DATASET RELATIONSHIP] Dataset with id " + datasetId.toString() + " does not exists in the database.");
        }
    }
}
