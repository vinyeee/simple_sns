package dev.vinyeee.mysns.model;

import dev.vinyeee.mysns.model.entity.UserEntity;
import dev.vinyeee.mysns.model.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

// dto 유저 정보를 가지고 와서 서비스 단에서 처리할 때 사용
// db 에는 영향을 주지 않으면서 단순히 dto 에 있는 필드만 변경
@AllArgsConstructor
@Getter
public class User {
    private Integer id;
    private String userName;
    private String password;
    private UserRole role;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;


    public static User fromEntity (UserEntity entity){

        return new User(entity.getId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getRole(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }
}
