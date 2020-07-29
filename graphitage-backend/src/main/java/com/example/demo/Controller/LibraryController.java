package com.example.demo.Controller;


import com.example.demo.Model.Library;
import com.example.demo.Model.Paper;
import com.example.demo.Service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api.graphitage.com/libraries")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {

        this.libraryService = libraryService;
    }

    @GetMapping
    public List<Library> getAllLibraries() {
        return libraryService.getAllLibraries();
    }

    @GetMapping("{libraryId}/papers")
    public List<Paper> getPapersByLibraryId(@PathVariable("libraryId") Long libraryId){
        return libraryService.getPapersByLibraryId(libraryId);
    }

    @DeleteMapping(path = "{libraryId}")
    public void deleteLibraryById(@PathVariable("libraryId") Long libraryId){
        libraryService.deleteLibraryById(libraryId);

    }

    @DeleteMapping(path = "{libraryId}/deleteLibraryRelationShip/{paperId}")
    public void deleteLibraryRelationShip(@PathVariable("libraryId") Long libraryId, @PathVariable("paperId") String paperId) {
        libraryService.deleteLibraryRelationShip(libraryId, paperId);
    }

    @PostMapping(path = "{libraryId}/addLibraryRelationShip/{paperId}")
    public void addLibraryRelationShip(@PathVariable("libraryId") Long libraryId, @PathVariable("paperId") String paperId) {
        libraryService.addLibraryRelationShip(libraryId, paperId);
    }

}
