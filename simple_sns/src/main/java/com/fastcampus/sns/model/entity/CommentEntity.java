package com.fastcampus.sns.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "comment")
@Getter
@Setter
@SQLDelete(sql = "UPDATE \"commnet\" set deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL") // 엔티티를 조회할 때 deleted_at 필드가 NULL인 레코드만 조회
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne // 참조하는 쪽: 참조한다는 건 다른 엔티티를 가지고 와서 쓴다는 의미
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "comment")
    private String comment;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;


    // 외부에서 호출되는 메서드가 아니라 JPA가 내부적으로 호출하기에 public 이 아니여도 됨
    // 엔티티가 처음으로 데이터베이스에 insert되기 직전에 자동으로 호출
    @PrePersist
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static CommentEntity of(UserEntity userEntity, PostEntity postEntity, String comment){
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUser(userEntity);
        commentEntity.setPost(postEntity);
        commentEntity.setComment(comment);
        return commentEntity;
    }
}
