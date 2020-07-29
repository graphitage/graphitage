package com.example.demo.Controller;

import com.example.demo.Model.Dataset;
import com.example.demo.Model.Library;
import com.example.demo.Model.Paper;
import com.example.demo.Model.Reader;
import com.example.demo.Service.PaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api.graphitage.com/papers")
public class PaperController {

    private final PaperService paperService;
    Logger logger = LoggerFactory.getLogger(PaperController.class);

    @Autowired
    public PaperController(PaperService paperService) {

        this.paperService = paperService;
    }

    @GetMapping
    public List<Paper> getAllPapers() {
        return paperService.getAllPapers();
    }

    @GetMapping(path = "/{paperId}")
    public Optional<Paper> getPaperInfoById(@PathVariable("paperId") String paperId) {
        return paperService.getPaperInfoById(paperId);
    }

    @GetMapping("{paperId}/keywords/")
    public List<String> getKeywordsById(@PathVariable("paperId") String paperId) {
        return paperService.getKeywordsById(paperId);
    }

    @GetMapping("{paperId}/datasets/")
    public List<Dataset> getDatasetsById(@PathVariable("paperId") String paperId) {
        return paperService.getDatasetsById(paperId);
    }

    @GetMapping("{paperId}/libraries/")
    public List<Library> getLibrariesById(@PathVariable("paperId") String paperId) {
        return paperService.getLibrariesById(paperId);
    }

    @GetMapping("{paperId}/readers/")
    public List<Reader> getReadersById(@PathVariable("paperId") String paperId) {
        return paperService.getReadersById(paperId);
    }

    @GetMapping("{paperId}/relatedWorks/")
    public List<Paper> getRelatedWorksById(@PathVariable("paperId") String paperId) {
        return paperService.getRelatedWorksById(paperId);
    }

    @PostMapping
    public void addPaper(@RequestBody Paper newPaper) {
        boolean isExists = paperService.existsPaperById(newPaper.getPaperId());
        if ( !isExists )
        {
            paperService.savePaper(newPaper);
        }
        else
        {
            logger.warn("[PAPER CONTROLLER - ADD PAPER] Paper with id " + newPaper.getPaperId() + " already exists in the database.");
        }
    }

    @DeleteMapping(path = "{paperId}")
    public void deletePaper(@PathVariable("paperId") String paperId) {
        paperService.deletePaper(paperId);
    }

    @PutMapping
    public void updatePaper(@RequestBody Paper updatedPaper) {
        boolean isExists = paperService.existsPaperById(updatedPaper.getPaperId());
        if ( isExists )
        {
            paperService.updatePaper(updatedPaper);
        }
        else
        {
            logger.warn("[PAPER CONTROLLER - UPDATE PAPER] Paper with id " + updatedPaper.getPaperId() + " requested to be updated does not exist in the database.");
        }
    }

    @GetMapping(path = "searchWithAND")
    public List<Paper> paperSearchWithAND(@RequestParam("title") Optional<List<String>> title, @RequestParam("dataset") Optional<List<String>> dataset,
                                          @RequestParam("library") Optional<List<String>> libraryName, @RequestParam("publishDate") Optional<List<String>> publishDate,
                                          @RequestParam("readerName") Optional<List<String>> readerName, @RequestParam("keyword") Optional<List<String>> keyword) {
        return paperService.paperSearchWithAND(title, publishDate, readerName, keyword, dataset, libraryName);
    }

    @GetMapping(path = "searchWithOR")
    public List<Paper> paperSearchWithOR(@RequestParam("title") Optional<List<String>> title, @RequestParam("dataset") Optional<List<String>> dataset,
                                         @RequestParam("library") Optional<List<String>> libraryName, @RequestParam("publishDate") Optional<List<String>> publishDate,
                                         @RequestParam("readerName") Optional<List<String>> readerName, @RequestParam("keyword") Optional<List<String>> keyword) {
        return paperService.paperSearchWithOR(title, publishDate, readerName, keyword, dataset, libraryName);
    }

    @DeleteMapping(path = "{paperId}/deleteRelatedWithRelationShip/{relatedWorkPaperId}")
    public void deleteRelatedWithRelationShip(@PathVariable("paperId") String paperId, @PathVariable("relatedWorkPaperId") String relatedWorkPaperId) {
        paperService.deleteRelatedWithRelationShip(paperId, relatedWorkPaperId);
    }

    @PostMapping(path = "{paperId}/addRelatedWithRelationShip/{relatedWorkPaperId}")
    public void addRelatedWithRelationShip(@PathVariable("paperId") String paperId, @PathVariable("relatedWorkPaperId") String relatedWorkPaperId) {
        paperService.addRelatedWithRelationShip(paperId, relatedWorkPaperId);
    }

    @GetMapping("{paperId}/relatedPaperWithKeyword")
    public List<Paper> getRelatedPapersWithKeyword(String paperId){
        return paperService.getRelatedPapersWithKeyword(paperId);
    }

    @GetMapping("semantic_api/{paperIdType}:{paperId}")
    public Paper getPaperUsingSemanticAPI(@RequestParam("paperIdType") String paperIdType, @RequestParam("paperId")String paperId) throws IOException, ParseException {
        return paperService.getPaperInfoUsingSemanticAPI(paperIdType, paperId);
    }

}
