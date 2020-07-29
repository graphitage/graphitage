package com.example.demo.Service;

import com.example.demo.Model.Library;
import com.example.demo.Model.Paper;
import com.example.demo.Repository.LibraryRepository;
import com.example.demo.Repository.PaperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final PaperRepository paperRepository;
    Logger logger = LoggerFactory.getLogger(LibraryService.class);

    @Autowired
    public LibraryService(@Qualifier("libraryDAO") LibraryRepository libraryRepository, @Qualifier("paperDAO") PaperRepository paperRepository) {
        this.libraryRepository = libraryRepository;
        this.paperRepository = paperRepository;
    }

    public List<Library> getAllLibraries() {
        return libraryRepository.findAll();
    }

    public List<Paper> getPapersByLibraryId(Long libraryId) {
        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if (libraryOptional.isPresent()) {
            return libraryOptional.get().getPapers();
        } else {
            return new ArrayList<Paper>();
        }
    }

    public void deleteLibraryById(Long libraryId) {
        libraryRepository.deleteById(libraryId);
    }

    public void deleteLibraryRelationShip(Long libraryId, String paperId) {
        Optional<Library> library = libraryRepository.findById(libraryId);
        if (library.isPresent()) {

            List<Paper> paperList = library.get().getPapers();
            Paper searchPaper = new Paper(paperId);

            if (paperList.contains(searchPaper)) {
                libraryRepository.deleteLibraryRelationShip(libraryId, paperId);
            } else {
                logger.warn("[LIBRARY SERVICE - DELETE LIBRARY RELATIONSHIP] There is no library relationship between library with id " + libraryId.toString() + " and paper with id " + paperId);
            }

        } else {
            logger.warn("[LIBRARY SERVICE - DELETE LIBRARY RELATIONSHIP] Library with id " + libraryId.toString() + " does not exists in the database.");
        }
    }

    public void addLibraryRelationShip(Long libraryId, String paperId) {
        Optional<Library> library = libraryRepository.findById(libraryId);
        if (library.isPresent()) {

            Optional<Paper> paper = paperRepository.findById(paperId);
            if (paper.isPresent()) {

                List<Paper> paperList = library.get().getPapers();
                if (!paperList.contains(paper.get())) {

                    paperList.add(paper.get());
                    library.get().setPapers(paperList);
                    libraryRepository.save(library.get());

                } else {
                    logger.warn("[LIBRARY SERVICE - ADD LIBRARY RELATIONSHIP] The relationship between library with id " + libraryId.toString() + " and paper with id " + paperId + " already exists in the database.");
                }

            } else {
                logger.warn("[LIBRARY SERVICE - ADD LIBRARY RELATIONSHIP] Paper with id " + paperId + " does not exists in the database.");
            }

        } else {
            logger.warn("[LIBRARY SERVICE - ADD LIBRARY RELATIONSHIP] Library with id " + libraryId.toString() + " does not exists in the database.");
        }
    }
}
