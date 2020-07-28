package com.example.demo.Service;


import com.example.demo.Model.*;
import com.example.demo.Repository.LibraryRepository;
import com.example.demo.Repository.PaperRepository;
import com.example.demo.Repository.PreprocessingRepository;
import com.example.demo.Repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PaperService {

    private final PaperRepository paperRepository;
    private final ReaderRepository readerRepository;
    private final LibraryRepository libraryRepository;
    private final PreprocessingRepository preprocessingRepository;

    @Autowired
    public PaperService(@Qualifier("paperDAO") PaperRepository paperRepository, @Qualifier("readerDAO") ReaderRepository readerRepository, LibraryRepository libraryRepository, PreprocessingRepository preprocessingRepository) {
        this.paperRepository = paperRepository;
        this.readerRepository = readerRepository;
        this.libraryRepository = libraryRepository;
        this.preprocessingRepository = preprocessingRepository;
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

    public boolean existsPaperById(String paperId) {
        return paperRepository.existsById(paperId);
    }

    /*
     * Paper has been saved to the database.
     */
    public void savePaper(Paper newPaper) {
        /* The new library list is initialized. */
        initLibraryList(newPaper);
        /* The new reader list is initialized. */
        initReaderList(newPaper);
        /* The new dataset list is initialized. */
        initDatasetList(newPaper);

        paperRepository.save(newPaper);
    }

    /*
     * The properties of the paper, which has the same id in the database, have been updated.
     */
    public void updatePaper(Paper newPaper) {

        Optional<Paper> paper = paperRepository.findById(newPaper.getPaperId());
        Paper oldPaper = paper.get();

        /*
         * If the newly added paper has a library list, the library list of the paper with the same id is cleared.
         * The new library list is initialized.
         */
        if (newPaper.getLibraries() != null && newPaper.getLibraries().size() > 0) {
            cleanLibraryList(oldPaper);
            initLibraryList(newPaper);
        }

        /*
         * If the newly added paper has a reader list, the reader list of the paper with the same id is cleared.
         * The new reader list is initialized.
         */
        if (newPaper.getReaders() != null && newPaper.getReaders().size() > 0) {
            cleanReaderList(oldPaper);
            initReaderList(newPaper);
        }

        /*
         * If the newly added paper has a dataset list, the dataset list of the paper with the same id is cleared.
         * The new dataset list is initialized.
         */
        if (newPaper.getDatasets() != null && newPaper.getDatasets().size() > 0) {
            cleanDatasetList(oldPaper);
            initDatasetList(newPaper);
        }

        paperRepository.save(newPaper);

    }

    /*
     * Paper's library list has been initialized.
     */
    public void initLibraryList(Paper paper) {
        /* Checking if there is a library with the same name or link. If there is a library with the same name or link, no new library is created. */
        List<Library> libraryList = paper.getLibraries();
        for (Library library : libraryList) {

            Optional<Library> tempLibrary = libraryRepository.findByName(library.getLibraryName());
            /* If there is a library with the same name, the id of the library is assigned the old library id. */
            if (tempLibrary.isPresent()) {
                library.setId(tempLibrary.get().getId());
            } else {
                /* If there is no library with the same name but a library with the same link, the id of the library is assigned the old library id. */
                tempLibrary = libraryRepository.findByLink(library.getLibraryLink());
                if (tempLibrary.isPresent()) {
                    library.setId(tempLibrary.get().getId());
                }
            }
        }
    }

    /*
     * Paper's reader list has been initialized.
     */
    public void initReaderList(Paper paper) {
        /* Checking if there is a reader with the same name. If there is a reader with the same name, no new reader is created. */
        List<Reader> readerList = paper.getReaders();
        for (Reader reader : readerList) {
            Optional<Reader> tempReader = readerRepository.findByName(reader.getReaderName());
            /* If there is a reader with the same name, the id of the reader is assigned the old reader id. */
            if (tempReader.isPresent()) {
                reader.setId(tempReader.get().getId());
            }
        }
    }

    /*
     * Paper's dataset list has been initialized.
     */
    public void initDatasetList(Paper paper) {
        /*
         * Paper is assigned to the start node in the preprocessing relationship.
         * There is no need to include the start node information from the JSON object.
         */
        List<Preprocessing> preProcList = paper.getDatasets();
        for (Preprocessing preproc : preProcList) {
            preproc.setPaper(paper);
        }
    }

    /*
     * The relationship between paper and library is broken.
     * But the library is not deleted from the database, only the relationship is deleted.
    */
    public void cleanLibraryList(Paper paper) {
        List<Library> libraryList = paper.getLibraries();

        for (Library library : libraryList) {
            deleteLibraryRelationShip(paper.getPaperId(), library.getId());
        }

    }

    /*
     * The relationship between paper and reader is broken.
     * But the reader is not deleted from the database, only the relationship is deleted.
     */
    public void cleanReaderList(Paper paper) {
        List<Reader> readerList = paper.getReaders();

        for (Reader reader : readerList) {
            deleteReaderRelationShip(paper.getPaperId(), reader.getId());
        }

    }

    /*
     * The relationship between paper and dataset is broken.
     * But the dataset is not deleted from the database, only the relationship is deleted.
     */
    public void cleanDatasetList(Paper paper) {

        List<Preprocessing> preprocessingList = paper.getDatasets();
        for (Preprocessing preprocessing : preprocessingList) {
            preprocessingRepository.deleteById(preprocessing.getId());
        }

    }

    /*
     * The relationship between paper and library deleted from the database.
     */
    public void deleteLibraryRelationShip(String paperId, Long libraryId) {
        paperRepository.deleteLibraryRelationship(paperId, libraryId);
    }

    /*
     * The relationship between paper and reader deleted from the database.
     */
    public void deleteReaderRelationShip(String paperId, Long readerId) {
        paperRepository.deleteReaderRelationship(paperId, readerId);
    }

    public void deletePaper(String paperId) {
        paperRepository.deleteById(paperId);
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

}
