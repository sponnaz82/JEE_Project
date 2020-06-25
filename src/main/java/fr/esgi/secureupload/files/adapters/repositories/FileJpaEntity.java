package fr.esgi.secureupload.files.adapters.repositories;

import fr.esgi.secureupload.common.repository.BaseJPAEntity;
import fr.esgi.secureupload.files.entities.Status;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity(name="File")
@Table(name="files")
public class FileJpaEntity extends BaseJPAEntity {
    protected FileJpaEntity(){}

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="contentType", nullable = false)
    private String contentType;

    @Column(name="size", nullable = false)
    private long size;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="status", nullable = false)
    private Status status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
