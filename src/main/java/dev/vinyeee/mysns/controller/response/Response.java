package dev.vinyeee.mysns.controller.response;

import lombok.AllArgsConstructor;

// 각 요청마다 다른 response 형식이 반환되어 프론트 쪽에 parsing 하기 힘든 점을 고려 response 를 통일해준다
@AllArgsConstructor
public class Response<T> {

    private String resultCode;
    private T result;

    // 실패 응답
    public static Response<Void> error(String errorCode){
        return new Response<>(errorCode, null);

    }

    // 성공 응답 <T> 타입 매개변수, <T> 타입의 반환 값
    public static <T> Response<T> success(T result){
        return new Response<>("SUCCESS",result);
    }


}
