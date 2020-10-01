package com.example.uploaddownloadfilestodb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Doc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String docName;
    @Lob
    private String description;
    private String docType;
    private String docSize;
    private String uploadTime;
    private String personName;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    private byte[] data;

    public Doc(String docName, String description, String docType, String docSize, String uploadTime, String personName, byte[] data) {
        this.docName = docName;
        this.description = description;
        this.docType = docType;
        this.docSize = docSize;
        this.uploadTime = uploadTime;
        this.personName = personName;
        this.data = data;
    }
}
