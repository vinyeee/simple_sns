package com.fastcampus.sns.controller.response;

import com.fastcampus.sns.model.User;
import com.fastcampus.sns.model.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class UserJoinResponse {

    private Integer id;
    private String userName;
    private UserRole userRole;

    public static UserJoinResponse fromUser(User user){
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
