package com.example.demo.Service;


import com.example.demo.Model.*;
import com.example.demo.Repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PaperService {

    private final PaperRepository paperRepository;

    @Autowired
    public PaperService(@Qualifier("paperDAO") PaperRepository paperRepository) {
        this.paperRepository = paperRepository;
    }

    private void paperIntersection(List<Paper> paperList1, List<Paper> paperList2) {
        if (paperList1.isEmpty()) {
            paperList1.addAll(paperList2);
        } else {
            paperList1.retainAll(paperList2);
        }
    }

    private List<Paper> paperUnion(List<Paper> paperList1, List<Paper> paperList2) {
        Set<Paper> set = new HashSet<Paper>();

        set.addAll(paperList1);
        set.addAll(paperList2);

        return new ArrayList<Paper>(set);
    }

    public List<Paper> getAllPapers() {
        return this.paperRepository.findAll();
    }

    public Optional<Paper> getPaperInfoById(String paperId) {
        return paperRepository.findById(paperId);
    }

    public void addPaper(Paper paper) {
        List<Preprocessing> preProcList = paper.getDatasets();
        for (Preprocessing preproc : preProcList) {
            preproc.setPaper(paper);
        }
        paperRepository.save(paper);
    }

    public void deletePaper(String paperId) {
        paperRepository.deleteById(paperId);
    }

    public void updatePaper(String paperId, Paper updatedPaper) {
        updatedPaper.setPaperId(paperId);
        paperRepository.save(updatedPaper);
    }

    public List<String> getKeywordsById(String paperId) {

        Optional<Paper> paper = paperRepository.findById(paperId);
        if (paper.isPresent()) {
            return paper.get().getKeywords();
        } else {
            return new ArrayList<String>();
        }
    }

    public List<Dataset> getDatasetsById(String paperId) {
        return paperRepository.findDatasetsById(paperId);
    }

    public List<Library> getLibrariesById(String paperId) {
        return paperRepository.getLibrariesById(paperId);
    }

    public List<Reader> getReadersById(String paperId) {
        return paperRepository.getReadersById(paperId);
    }

    public List<Paper> getRelatedWorksById(String paperId) {
        return paperRepository.getRelatedWorksByID(paperId);
    }

    public List<Paper> paperSearchWithAND(Optional<String> title, Optional<String> publishDate, Optional<String> readerName,
                                          Optional<String> keyword, Optional<String> datasetName, Optional<String> libraryName) {

        List<Paper> resultList = new ArrayList<>();
        if (title.isPresent()) {
            resultList = paperRepository.paperSearchWithTitle(title.get());
        }
        if (publishDate.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithPublishDate(publishDate.get());
            paperIntersection(resultList, tempList);
        }
        if (readerName.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithReaderName(readerName.get());
            paperIntersection(resultList, tempList);
        }
        if (keyword.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithKeyword(keyword.get());
            paperIntersection(resultList, tempList);
        }
        if (datasetName.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithDatasetName(datasetName.get());
            paperIntersection(resultList, tempList);
        }
        if (libraryName.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithLibraryName(libraryName.get());
            paperIntersection(resultList, tempList);
        }

        return resultList;
    }

    public List<Paper> paperSearchWithOR(Optional<String> title, Optional<String> publishDate, Optional<String> readerName,
                                         Optional<String> keyword, Optional<String> datasetName, Optional<String> libraryName) {

        List<Paper> resultList = new ArrayList<>();
        if (title.isPresent()) {
            resultList = paperRepository.paperSearchWithTitle(title.get());
        }
        if (publishDate.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithPublishDate(publishDate.get());
            resultList = paperUnion(resultList, tempList);
        }
        if (readerName.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithReaderName(readerName.get());
            resultList = paperUnion(resultList, tempList);
        }
        if (keyword.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithKeyword(keyword.get());
            resultList = paperUnion(resultList, tempList);
        }
        if (datasetName.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithDatasetName(datasetName.get());
            resultList = paperUnion(resultList, tempList);
        }
        if (libraryName.isPresent()) {
            List<Paper> tempList = paperRepository.paperSearchWithLibraryName(libraryName.get());
            resultList = paperUnion(resultList, tempList);
        }

        return resultList;
    }

    public List<Paper> getRelatedPapersWithKeyword(String paperId) {
        Optional<Paper> paper = paperRepository.findById(paperId);
        List<Paper> paperList = new ArrayList<>();
        if (paper.isPresent()) {
            List<String> keywords = paper.get().getKeywords();
            for (String keyword : keywords) {
                List<Paper> temp = paperRepository.paperSearchWithKeyword(keyword);
                paperList = paperUnion(paperList, temp);
            }
            paperList.remove(paper.get());

        }
        return paperList;
    }

//    public List<String> getPreprocessingSteps(Long preprocessingId) {
//        Optional<Preprocessing> paperPreprocessing = paperRepository.getPreprocessing(preprocessingId);
//        if(paperPreprocessing.isPresent()){
//            return paperPreprocessing.get().getPreprocessingSteps();
//        }
//        else{
//            return new ArrayList<>();
//        }
//
//    }
}
