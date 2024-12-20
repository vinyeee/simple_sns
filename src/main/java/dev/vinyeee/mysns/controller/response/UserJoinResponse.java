package dev.vinyeee.mysns.controller.response;

import dev.vinyeee.mysns.controller.request.UserJoinRequest;
import dev.vinyeee.mysns.model.User;
import dev.vinyeee.mysns.model.entity.UserRole;
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
