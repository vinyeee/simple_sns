package com.fastcampus.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor // 회원 가입이니까 두 개다 입력 받아야함
@Getter
// 회원가입 시 request body 로 받아올 데이터
public class UserJoinRequest {

    private String name;
    private String password;


}
