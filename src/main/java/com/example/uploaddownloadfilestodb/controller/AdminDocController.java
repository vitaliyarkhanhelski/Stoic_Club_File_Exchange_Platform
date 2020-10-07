package com.example.uploaddownloadfilestodb.controller;

import com.example.uploaddownloadfilestodb.model.Doc;
import com.example.uploaddownloadfilestodb.service.DocStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDocController {

    private DocStorageService docStorageService;

    @Autowired
    public AdminDocController(DocStorageService docStorageService) {
        this.docStorageService = docStorageService;
    }


    @GetMapping("/files")
    public String showAdminFilesPage(ModelMap map) {

        map.put("docs", docStorageService.getFiles());
        map.put("flag", false);

//        List<Doc> docs = new ArrayList<>();
//        if (page == null) {
//            docs = docStorageService.getFiles();
//            map.put("docs", docs);
//            map.put("flag", false);
////            map.put("title", "Files");
////            map.put("filesCount", docs.size());
//
//        } else {
//            docs = docStorageService.getArchive();
//            map.put("docs", docs);
//            map.put("flag", true);
////            map.put("title", "Files/Archive");
////            map.put("filesCount", docs.size());
//        }
        return "admin";
    }


    @GetMapping("/files/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("docId") Long id, ModelMap map) {
        Doc doc = docStorageService.findById(id);
        map.put("doc", doc);
        return "form";
    }


    @PostMapping("files/update")
    public String save(@ModelAttribute("doc") Doc doc, ModelMap map) {
        Doc newDoc = docStorageService.findById(doc.getId());
        newDoc.setDocName(doc.getDocName());
        newDoc.setPersonName(doc.getPersonName());
        newDoc.setDescription(doc.getDescription());
        newDoc.setArchive(doc.isArchive());
        docStorageService.save(newDoc);
        List<Doc> docs = new ArrayList<>();
        docs.add(newDoc);
        map.put("docs", docs);
        map.put("flag", false);
        return "admin";
    }


    @PostMapping("/files/uploadFiles")
    public String uploadMultipleFiles(@Size(max = 20971520) @RequestParam("files") MultipartFile[] files
            , @RequestParam(value = "personName", required = false) String personName
            , @RequestParam(value = "description", required = false) String description
            , ModelMap map) {
        for (MultipartFile file : files)
            docStorageService.saveFile(file, personName, description);
        return "redirect:/admin/files";
    }


    @GetMapping("/files/archiveFiles")
    public String showAdminArchivedFilesPage(ModelMap map) {
        map.put("docs", docStorageService.getArchive());
        map.put("flag", true);
        return "admin";
    }


    @Transactional
    @GetMapping("/files/delete")
    public String deleteById(@RequestParam("docId") Long id) {
        docStorageService.deleteById(id);
        return "redirect:/admin/files";
    }


    @GetMapping("/files/archive")
    public String archiveById(@RequestParam("docId") Long id, @RequestParam(value = "archive", required = false) String archive) {
        docStorageService.archiveById(id);
        if (archive == null) return "redirect:/admin/files";
        return "redirect:/admin/files/archiveFiles";
    }

    @Transactional
    @PostMapping("/files/findByWord")
    public String findByWord(@RequestParam(value = "word", required = false) String word, ModelMap map) {
        List<Doc> docs = new ArrayList<>();
        for (Doc doc : docStorageService.getFiles()) {
            if (doc.getDocName().toLowerCase().contains(word.toLowerCase()))
                docs.add(doc);
            else if (doc.getDescription() != null)
                if (doc.getDescription().toLowerCase().contains(word.toLowerCase()))
                    docs.add(doc);
        }
        map.put("docs", docs);
        map.put("flag", false);
        return "admin";
    }

}
