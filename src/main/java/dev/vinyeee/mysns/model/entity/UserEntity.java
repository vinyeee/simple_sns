package dev.vinyeee.mysns.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;


// 실제 db 테이블에 매핑되는 클래스
@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@SQLDelete(sql = "UPDATE \"user\" set deleted_at = NOW() where = id = ?") // DELETE 쿼리를 수행하는 대신 UPDATE 쿼리를 사용하여 deleted_at 컬럼에 현재 시간을 기록
@Where(clause = "deleted_at is NULL") //  엔티티를 조회할 때 자동으로 추가되는 조건
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING) //  enum 타입의 필드를 데이터베이스에 매핑
    private UserRole role = UserRole.USER;

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

    // 왜 메소드를 static 으로 만들어 줄까?
    // 정적 팩토리 메서드 패턴을 활용하여 객체 생성을 간편하고 명확하게 하기 위함
    // 새 엔티티를 만들어주는 클래스
    // static 메서드는 클래스 자체에서 호출되기 때문에 객체를 생성하기 위해 인스턴스를 미리 생성할 필요가 없다.
    // UserEntity.of("yebin", "password123")처럼 사용할 수 있어서 가독성이 좋아짐
    public static UserEntity of(String userName, String password){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        return userEntity;
    }


    }
