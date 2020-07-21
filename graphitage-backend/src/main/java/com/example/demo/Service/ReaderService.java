package com.example.demo.Service;

import com.example.demo.Model.Paper;
import com.example.demo.Model.Reader;
import com.example.demo.Repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public List<Reader> getAllReaders(){
        return readerRepository.findAll();
    }


    public List<Paper> getPapersByReaderId(Long readerId) {
        Optional<Reader> optionalReader = readerRepository.findById(readerId);
        if(optionalReader.isPresent()){
            return optionalReader.get().getPapers();
        }
        else{
            return new ArrayList<Paper>();
        }
    }

    public void deleteReaderById(Long readerId) {
        readerRepository.deleteById(readerId);
    }
}
