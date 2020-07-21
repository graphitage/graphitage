package com.example.demo.Controller;

import com.example.demo.Model.Dataset;
import com.example.demo.Model.Library;
import com.example.demo.Model.Paper;
import com.example.demo.Model.Reader;
import com.example.demo.Service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api.graphitage.com/papers")
public class PaperController {

    private final PaperService paperService;


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
    public void addPaper(@RequestBody Paper paper) {
        paperService.addPaper(paper);
    }

    @DeleteMapping(path = "{paperId}")
    public void deletePaper(@PathVariable("paperId") String paperId) {
        paperService.deletePaper(paperId);
    }

    @PutMapping(path = "{paperId}")
    public void updatePaper(@PathVariable("paperId") String paperId, @RequestBody Paper updatedPaper) {
        paperService.updatePaper(paperId, updatedPaper);
    }

    @GetMapping(path = "searchWithAND")
    public List<Paper> paperSearchWithAND(@RequestParam("title") Optional<String> title, @RequestParam("dataset") Optional<String> dataset, @RequestParam("library") Optional<String> libraryName, @RequestParam("publishDate") Optional<String> publishDate, @RequestParam("readerName") Optional<String> readerName, @RequestParam("keyword") Optional<String> keyword) {
        List<Paper> papers = paperService.paperSearchWithAND(title, publishDate, readerName, keyword, dataset, libraryName);
        return papers;
    }

    @GetMapping(path = "searchWithOR")
    public List<Paper> paperSearchWithOR(@RequestParam("title") Optional<String> title, @RequestParam("dataset") Optional<String> dataset, @RequestParam("library") Optional<String> libraryName, @RequestParam("publishDate") Optional<String> publishDate, @RequestParam("readerName") Optional<String> readerName, @RequestParam("keyword") Optional<String> keyword) {
        List<Paper> papers = paperService.paperSearchWithOR(title, publishDate, readerName, keyword, dataset, libraryName);
        return papers;
    }

    @GetMapping("{paperId}/relatedPaperWithKeyword")
    public List<Paper> getRelatedPapersWithKeyword(String paperId){
        return paperService.getRelatedPapersWithKeyword(paperId);
    }

}
