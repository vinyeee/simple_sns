package dev.vinyeee.mysns.controller;


import dev.vinyeee.mysns.controller.request.UserJoinRequest;
import dev.vinyeee.mysns.controller.request.UserLoginRequest;
import dev.vinyeee.mysns.controller.response.UserLoginResponse;
import dev.vinyeee.mysns.controller.response.Response;
import dev.vinyeee.mysns.controller.response.UserJoinResponse;
import dev.vinyeee.mysns.model.User;
import dev.vinyeee.mysns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> signup(@RequestBody UserJoinRequest userJoinRequest){
        User user = userService.signup(userJoinRequest.getUserName(),userJoinRequest.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        String token = userService.login(userLoginRequest.getUserName(),userLoginRequest.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
