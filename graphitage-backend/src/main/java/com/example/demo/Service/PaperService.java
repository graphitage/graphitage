package com.example.demo.Service;


import com.example.demo.Model.*;
import com.example.demo.Repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PaperService {

    private final PaperRepository paperRepository;
    private final ReaderRepository readerRepository;
    private final LibraryRepository libraryRepository;
    private final PreprocessingRepository preprocessingRepository;
    private final DatasetRepository datasetRepository;
    Logger logger = LoggerFactory.getLogger(LibraryService.class);

    @Autowired
    public PaperService(@Qualifier("paperDAO") PaperRepository paperRepository,
                        @Qualifier("readerDAO") ReaderRepository readerRepository,
                        @Qualifier("libraryDAO") LibraryRepository libraryRepository,
                        @Qualifier("preprocessingDAO") PreprocessingRepository preprocessingRepository,
                        @Qualifier("datasetDAO") DatasetRepository datasetRepository) {
        this.paperRepository = paperRepository;
        this.readerRepository = readerRepository;
        this.libraryRepository = libraryRepository;
        this.preprocessingRepository = preprocessingRepository;
        this.datasetRepository = datasetRepository;
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
         * The properties that will not be updated with setter and getter functions are determined..
         * Since library, reader, dataset and relatedWork lists are kept in nodes, they are not updated with setter and getter functions.
         * Relations should be deleted and new relationships should be created.
         */
        List<String> withoutProperties = new ArrayList<>();
        withoutProperties.add("Libraries");
        withoutProperties.add("Readers");
        withoutProperties.add("Datasets");
        withoutProperties.add("RelatedWorks");
        withoutProperties.add("Class");

        /* The non-null properties of the new Paper are updated on the old paper. */
        oldPaper.merge(newPaper, withoutProperties);

        /*
         * Updating the relatedWork list.
         * If the newly added paper has a relatedWork list, the relatedWork list of the paper with the same id is cleared.
         * The new relatedWork list is set.
         */
        if (newPaper.getRelatedWorks() != null && newPaper.getRelatedWorks().size() > 0) {
            cleanRelatedWorkList(oldPaper);
            oldPaper.setRelatedWorks(newPaper.getRelatedWorks());
        }

        /*
         * Updating the library list.
         * If the newly added paper has a library list, the library list of the paper with the same id is cleared.
         * The new library list is initialized.
         */
        if (newPaper.getLibraries() != null && newPaper.getLibraries().size() > 0) {
            cleanLibraryList(oldPaper);
            oldPaper.setLibraries(newPaper.getLibraries());
            initLibraryList(oldPaper);
        }

        /*
         * Updating the reader list.
         * If the newly added paper has a reader list, the reader list of the paper with the same id is cleared.
         * The new reader list is initialized.
         */
        if (newPaper.getReaders() != null && newPaper.getReaders().size() > 0) {
            cleanReaderList(oldPaper);
            oldPaper.setReaders(newPaper.getReaders());
            initReaderList(oldPaper);
        }

        /*
         * Updating the dataset list.
         * If the newly added paper has a dataset list, the dataset list of the paper with the same id is cleared.
         * The new dataset list is initialized.
         */
        if (newPaper.getDatasets() != null && newPaper.getDatasets().size() > 0) {
            cleanDatasetList(oldPaper);
            oldPaper.setDatasets(newPaper.getDatasets());
            initDatasetList(oldPaper);
        }

        paperRepository.save(oldPaper);

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
            /* Checking if there is a dataset with the same name. If there is a dataset with the same name, no new dataset is created. */
            Optional<Dataset> tempDataset = datasetRepository.findByName(preproc.getDataset().getDatasetName());
            /* If there is a dataset with the same name, the id of the dataset is assigned the old dataset id. */
            if (tempDataset.isPresent()) {
                preproc.getDataset().setDatasetId(tempDataset.get().getDatasetId());
            }
            preproc.setPaper(paper);
        }
    }

    /*
     * The relationship between the paper and the paper it is related with is broken.
     * But the the paper it is related with is not deleted from the database, only the relationship is deleted.
     */
    public void cleanRelatedWorkList(Paper paper) {
        List<Paper> relatedWorks = paper.getRelatedWorks();

        for (Paper relatedWork : relatedWorks) {
            deleteRelatedWithRelationShip(paper.getPaperId(), relatedWork.getPaperId());
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

    public void addRelatedWithRelationShip(String paperId, String relatedWorkPaperId) {
        Optional<Paper> paper = paperRepository.findById(paperId);
        if (paper.isPresent()) {

            Optional<Paper> relatedWorkPaper = paperRepository.findById(relatedWorkPaperId);
            if (relatedWorkPaper.isPresent()) {

                List<Paper> relatedWorkPapers = paper.get().getRelatedWorks();
                if (!relatedWorkPapers.contains(relatedWorkPaper.get())) {

                    relatedWorkPapers.add(relatedWorkPaper.get());
                    paper.get().setRelatedWorks(relatedWorkPapers);
                    paperRepository.save(paper.get());

                } else {
                    logger.warn("[PAPER SERVICE - ADD RELATED WITH RELATIONSHIP] The relationship between paper with id " + paperId + " and paper with id " + relatedWorkPaperId + " already exists in the database.");
                }

            } else {
                logger.warn("[PAPER SERVICE - ADD RELATED WITH RELATIONSHIP] Paper with id " + relatedWorkPaperId + " does not exists in the database.");
            }

        } else {
            logger.warn("[PAPER SERVICE - ADD RELATED WITH RELATIONSHIP] Paper with id " + paperId + " does not exists in the database.");
        }
    }

    /*
     * The relationship between the paper and the paper it is related with has been deleted from the database.
     */
    public void deleteRelatedWithRelationShip(String paperId, String relatedWorkPaperId) {
        paperRepository.deleteRelatedWithRelationship(paperId, relatedWorkPaperId);
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

    public Paper getPaperInfoUsingSemanticAPI(String paperIdType, String paperId) throws IOException, ParseException {

        String baseAddress = "https://api.semanticscholar.org/v1/paper/";
        String address = "";

        // Control given id type and generate request url with respect to given ID type
        if (paperIdType.equalsIgnoreCase("doi") || paperIdType.equalsIgnoreCase("s2")) {
            address = baseAddress + paperId;
        } else {
            address = baseAddress + paperIdType + ":" + paperId;
        }

        // Send request to semantic api and get response
        URL urlForGetRequest = new URL(address);
        String readLine = null;
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = null;
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

        } else {
            return new Paper();
        }

        // Parse returned response to JSONObject
        JSONObject paperJson = new JSONObject(response.toString());
        JSONObject tempJson;


        // Get paper's id, abstract and url
        String title = (String) paperJson.get("title");
        String abstractOfJ = (String) paperJson.get("abstract");
        String url = (String) paperJson.get("url");

        Date date = null;
        if (!paperJson.isNull("year")) {
            String year = String.valueOf(paperJson.get("year"));
            date = new SimpleDateFormat("yyyy").parse(year);
        }


        List<String> authors = new ArrayList<>();
        JSONArray authorsJ = (JSONArray) paperJson.get("authors");
//        StringBuilder authors = new StringBuilder();
        for (int i = 0; i < authorsJ.length(); i++) {
            tempJson = (JSONObject) authorsJ.get(i);
            authors.add((String) tempJson.get("name"));
//            authors.append((String) tempJson.get("name")).append(",");
        }

        // Use fieldOfStudy and topics as keywords of the paper
        List<String> keywords = new ArrayList<>();
        JSONArray fieldsOfStudy = (JSONArray) paperJson.get("fieldsOfStudy");
        for (int i = 0; i < fieldsOfStudy.length(); i++) {
            keywords.add((String) fieldsOfStudy.get(i));
        }
        JSONArray topicJ = (JSONArray) paperJson.get("topics");
        for (int i = 0; i < topicJ.length(); i++) {
            tempJson = (JSONObject) topicJ.get(i);
            keywords.add((String) tempJson.get("topic"));
        }

        // Get related works
        String referencesId = "", referencesType = "";
        JSONArray referencesJ = (JSONArray) paperJson.get("references");
        List<Paper> references = new ArrayList<>();
        for (int i = 0; i < referencesJ.length(); i++) {
            tempJson = (JSONObject) referencesJ.get(i);

            if (!tempJson.isNull("doi")) {
                referencesType = "DOI";
                referencesId = (String) tempJson.get("doi");
            } else if (!tempJson.isNull("arxivId")) {
                referencesType = "arXiv";
                referencesId = (String) tempJson.get("arxivId");
            } else {
                referencesType = "S2";
                referencesId = (String) tempJson.get("paperId");
            }
            references.add(new Paper(referencesId, referencesType, (String) tempJson.get("title")));
        }

        if (!paperJson.isNull("doi")) {
            paperIdType = "DOI";
            paperId = (String) paperJson.get("doi");
        } else if (!paperJson.isNull("arxivId")) {
            paperIdType = "arXiv";
            paperId = (String) paperJson.get("arxivId");
        }

        Paper paper = new Paper(paperId, paperIdType, authors, keywords, title, abstractOfJ, url, date);
        paper.setRelatedWorks(references);
        return paper;

    }

}
