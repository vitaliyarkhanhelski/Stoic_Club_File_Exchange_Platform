package com.example.uploaddownloadfilestodb.controller;

import com.example.uploaddownloadfilestodb.model.Doc;
import com.example.uploaddownloadfilestodb.service.DocStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.Size;
import java.util.List;

@Controller
@Validated
public class DocController {

    private DocStorageService docStorageService;

    @Autowired
    public DocController(DocStorageService docStorageService) {
        this.docStorageService = docStorageService;
    }


    @GetMapping("/accessDenied")
    public String error() {
        return "accessDenied";
    }


    @Transactional
    @GetMapping("/files")
    public String get(ModelMap map, @RequestParam(value = "page", required = false) String page) {
        if (page == null) {
            List<Doc> docs = docStorageService.getFiles();
            map.put("docs", docs);
            map.put("flag", false);
            map.put("title", "Files");
            map.put("filesCount", docs.size());

        } else {
            List<Doc> docs = docStorageService.getArchive();
            map.put("docs", docs);
            map.put("flag", true);
            map.put("title", "Files/Archive");
            map.put("filesCount", docs.size());

        }
        return "doc";
    }


    @PostMapping("/files/uploadFiles")
    public String uploadMultipleFiles(@Size(max = 20971520) @RequestParam("files") MultipartFile[] files
            , @RequestParam(required = false) String personName
            , @RequestParam(required = false) String description
            , ModelMap map) {
        for (MultipartFile file : files)
            docStorageService.saveFile(file, personName, description);
        return "redirect:/files";
    }


    @GetMapping("/files/downloadFile/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId) {
        Doc doc = docStorageService.getFile(fileId).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + doc.getDocName() + "\"")
                .body(new ByteArrayResource(doc.getData()));
    }


    @Transactional
    @GetMapping("files/delete")
    public String deleteById(@RequestParam("docId") Long id) {
        docStorageService.deleteById(id);
        return "redirect:/files";
    }


    @GetMapping("files/archive")
    public String archiveById(@RequestParam("docId") Long id, @RequestParam(value = "archive", required = false) String archive) {
        docStorageService.archiveById(id);
        System.out.println("Check!!!!!!!!");
        if (archive==null)  return "redirect:/files";
        return "redirect:/files?page=archive";
    }
}
