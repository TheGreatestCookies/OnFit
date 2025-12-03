package kspo.onfit.post.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import kspo.onfit.member.domain.Member;
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
    private Long id;

    private String title;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Post(PostRequestDto postRequestDto, Member member){
        this.title = postRequestDto.title();
        this.content = postRequestDto.content();
        this.member = member;
    }

    public void updateContent(PostUpdateDto updateDto){
        this.content = updateDto.content();
    }

    protected Post(){}

}