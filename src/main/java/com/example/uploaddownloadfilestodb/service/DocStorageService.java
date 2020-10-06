package com.example.uploaddownloadfilestodb.service;

import com.example.uploaddownloadfilestodb.model.Doc;
import com.example.uploaddownloadfilestodb.repository.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DocStorageService {

    private DocRepository docRepository;

    @Autowired
    public DocStorageService(DocRepository docRepository) {
        this.docRepository = docRepository;
    }


    public Doc saveFile(MultipartFile file, String personName, String description) {
        String docName = file.getOriginalFilename();

        try {
            Doc doc = new Doc(docName, description,
                    file.getContentType(), formatting(file.getSize() * 0.00000095367432), getDate(), personName, file.getBytes());
            return docRepository.save(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Optional<Doc> getFile(Long fileId) {
        return docRepository.findById(fileId);
    }


    public List<Doc> getFiles() {
        return docRepository.findAllByIsArchiveIsFalseOrderByIdDesc();
    }


    public List<Doc> getArchive() {
        return docRepository.findAllByIsArchiveIsTrueOrderByIdDesc();
    }


    public static String formatting(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(d);
    }


    public static String getDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return currentDateTime.format(formatter);
    }

    
    public void deleteById(long id) {
        docRepository.deleteById( id);
    }

    public void archiveById(Long id) {
        Doc doc = docRepository.findById(id).get();
        if (doc.isArchive()) doc.setArchive(false);
        else doc.setArchive(true);
        docRepository.save(doc);
    }
}
