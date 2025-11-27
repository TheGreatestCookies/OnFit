package kspo.onfit.imageFile.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class ImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private Long postId;

    public ImageFile(String url, Long postId){
        this.url = url;
        this.postId = postId;
    }

    protected ImageFile(){}

}