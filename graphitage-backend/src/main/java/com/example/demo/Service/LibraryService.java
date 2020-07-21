package com.example.demo.Service;

import com.example.demo.Model.Library;
import com.example.demo.Model.Paper;
import com.example.demo.Repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;

    @Autowired
    public LibraryService(@Qualifier("libraryDAO") LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public List<Library> getAllLibraries(){
        return libraryRepository.findAll();
    }

    public List<Paper> getPapersByLibraryId(Long libraryId) {
        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(libraryOptional.isPresent()){
            return libraryOptional.get().getPapers();
        }
        else{
            return new ArrayList<Paper>();
        }
    }

    public void deleteLibraryById(Long libraryId) {
        libraryRepository.deleteById(libraryId);
    }
}
