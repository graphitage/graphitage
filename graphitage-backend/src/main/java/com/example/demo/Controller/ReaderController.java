package com.example.demo.Controller;

import com.example.demo.Model.Paper;
import com.example.demo.Model.Reader;
import com.example.demo.Service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api.graphitage.com/readers")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public List<Reader> getAllReaders(){
        return readerService.getAllReaders();
    }

    @GetMapping("{readerId}/papers")
    public List<Paper> getPapersByReaderId(@PathVariable("readerId") Long readerId){
        return readerService.getPapersByReaderId(readerId);
    }

    @DeleteMapping("{readerId}")
    public void deleteReaderById(@PathVariable("readerId") Long readerId){
        readerService.deleteReaderById(readerId);
    }

    @DeleteMapping(path = "{readerId}/deleteReaderRelationShip/{paperId}")
    public void deleteReaderRelationShip(@PathVariable("readerId") Long readerId, @PathVariable("paperId") String paperId) {
        readerService.deleteReaderRelationShip(readerId, paperId);
    }

    @PostMapping(path = "{readerId}/addReaderRelationShip/{paperId}")
    public void addReaderRelationShip(@PathVariable("readerId") Long readerId, @PathVariable("paperId") String paperId) {
        readerService.addReaderRelationShip(readerId, paperId);
    }
}
