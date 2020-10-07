package com.example.uploaddownloadfilestodb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
@ToString
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
    private boolean isArchive;

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
        this.isArchive=false;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public void setIsArchive(boolean archive) {
        isArchive = archive;
    }
}
