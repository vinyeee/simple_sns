package dev.vinyeee.mysns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResponse {

    private String token; // 로그인이 정상적으로 이루어지면 토큰을 반환해야함

}
