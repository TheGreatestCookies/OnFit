package kspo.onfit.post.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import kspo.onfit.post.dto.PostRequestDto;
import kspo.onfit.post.dto.PostUpdateDto;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    //userId

    public Post(PostRequestDto postRequestDto){
        this.content = postRequestDto.content();
    }

    public void updateContent(PostUpdateDto updateDto){
        this.content = updateDto.content();
    }

    protected Post(){}

}