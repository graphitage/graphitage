package com.example.demo.Service;


import com.example.demo.Model.*;
import com.example.demo.Repository.PaperRepository;
import com.example.demo.Repository.PreprocessingRepository;
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

    public List<Paper> paperSearchWithAND(Optional<List<String>> titles, Optional<List<String>> publishDates, Optional<List<String>> readerNames,
                                          Optional<List<String>> keywords, Optional<List<String>> datasetNames, Optional<List<String>> libraryNames) {

        List<Paper> resultList = new ArrayList<>();
        if (titles.isPresent()) {
            for (String title : titles.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithTitle(title);
                paperIntersection(resultList, tempList);
            }
        }
        if (publishDates.isPresent()) {
            for (String publishDate : publishDates.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithPublishDate(publishDate);
                paperIntersection(resultList, tempList);
            }
        }
        if (readerNames.isPresent()) {
            for (String readerName : readerNames.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithReaderName(readerName);
                paperIntersection(resultList, tempList);
            }
        }
        if (keywords.isPresent()) {
            for (String keyword : keywords.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithKeyword(keyword);
                paperIntersection(resultList, tempList);
            }
        }
        if (datasetNames.isPresent()) {
            for (String datasetName : datasetNames.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithDatasetName(datasetName);
                paperIntersection(resultList, tempList);
            }
        }
        if (libraryNames.isPresent()) {
            for (String libraryName : libraryNames.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithLibraryName(libraryName);
                paperIntersection(resultList, tempList);
            }

        }

        return resultList;
    }

    public List<Paper> paperSearchWithOR(Optional<List<String>> titles, Optional<List<String>> publishDates, Optional<List<String>> readerNames,
                                         Optional<List<String>> keywords, Optional<List<String>> datasetNames, Optional<List<String>> libraryNames) {

        List<Paper> resultList = new ArrayList<>();
        if (titles.isPresent()) {
            for (String title : titles.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithTitle(title);
                resultList = paperUnion(resultList, tempList);
            }
        }
        if (publishDates.isPresent()) {
            for (String publishDate : publishDates.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithPublishDate(publishDate);
                resultList = paperUnion(resultList, tempList);
            }
        }
        if (readerNames.isPresent()) {
            for (String readerName : readerNames.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithReaderName(readerName);
                resultList = paperUnion(resultList, tempList);
            }
        }
        if (keywords.isPresent()) {
            for (String keyword : keywords.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithKeyword(keyword);
                resultList = paperUnion(resultList, tempList);
            }
        }
        if (datasetNames.isPresent()) {
            for (String datasetName : datasetNames.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithDatasetName(datasetName);
                resultList = paperUnion(resultList, tempList);
            }

        }
        if (libraryNames.isPresent()) {
            for (String libraryName : libraryNames.get()) {
                List<Paper> tempList = paperRepository.paperSearchWithLibraryName(libraryName);
                resultList = paperUnion(resultList, tempList);
            }
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

}
