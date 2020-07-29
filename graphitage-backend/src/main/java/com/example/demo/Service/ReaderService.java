package com.example.demo.Service;

import com.example.demo.Model.Paper;
import com.example.demo.Model.Reader;
import com.example.demo.Repository.PaperRepository;
import com.example.demo.Repository.ReaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final PaperRepository paperRepository;
    Logger logger = LoggerFactory.getLogger(ReaderService.class);

    @Autowired
    public ReaderService(ReaderRepository readerRepository, PaperRepository paperRepository) {
        this.readerRepository = readerRepository;
        this.paperRepository = paperRepository;
    }

    public List<Reader> getAllReaders(){
        return readerRepository.findAll();
    }


    public List<Paper> getPapersByReaderId(Long readerId) {
        Optional<Reader> optionalReader = readerRepository.findById(readerId);
        if(optionalReader.isPresent()){
            return optionalReader.get().getPapers();
        }
        else{
            return new ArrayList<Paper>();
        }
    }

    public void deleteReaderById(Long readerId) {
        readerRepository.deleteById(readerId);
    }

    public void deleteReaderRelationShip(Long readerId, String paperId) {
        Optional<Reader> reader = readerRepository.findById(readerId);
        if (reader.isPresent()) {

            List<Paper> paperList = reader.get().getPapers();
            Paper searchPaper = new Paper(paperId);

            if (paperList.contains(searchPaper)) {
                readerRepository.deleteReaderRelationShip(readerId, paperId);
            } else {
                logger.warn("[READER SERVICE - DELETE READER RELATIONSHIP] There is no reader relationship between library with id " + readerId.toString() + " and paper with id " + paperId);
            }

        } else {
            logger.warn("[READER SERVICE - DELETE READER RELATIONSHIP] Reader with id " + readerId.toString() + " does not exists in the database.");
        }
    }

    public void addReaderRelationShip(Long readerId, String paperId) {
        Optional<Reader> reader = readerRepository.findById(readerId);
        if (reader.isPresent()) {

            Optional<Paper> paper = paperRepository.findById(paperId);
            if (paper.isPresent()) {

                List<Paper> paperList = reader.get().getPapers();
                if (!paperList.contains(paper.get())) {

                    paperList.add(paper.get());
                    reader.get().setPapers(paperList);
                    readerRepository.save(reader.get());

                } else {
                    logger.warn("[READER SERVICE - ADD READER RELATIONSHIP] The relationship between reader with id " + readerId.toString() + " and paper with id " + paperId + " already exists in the database.");
                }

            } else {
                logger.warn("[READER SERVICE - ADD READER RELATIONSHIP] Paper with id " + paperId + " does not exists in the database.");
            }

        } else {
            logger.warn("[READER SERVICE - ADD READER RELATIONSHIP] Reader with id " + readerId.toString() + " does not exists in the database.");
        }
    }
}
