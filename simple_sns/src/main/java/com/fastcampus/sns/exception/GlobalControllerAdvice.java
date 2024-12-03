package com.fastcampus.sns.exception;


import com.fastcampus.sns.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // Spring의 전역 예외 처리용 애너테이션. 모든 @RestController에서 발생하는 예외를 처리할 수 있게 해준다.
@Slf4j  // Lombok의 애너테이션으로, 로그를 찍을 수 있게 해준다. log 객체를 자동으로 생성.
public class GlobalControllerAdvice {

    // SnsApplicationException이 발생하면 이 메서드가 호출된다.
    @ExceptionHandler(SnsApplicationException.class)
    public ResponseEntity<?> applicationHandler(SnsApplicationException e){

        // 예외 발생 시 해당 예외에 대한 정보와 함께 로그를 남긴다.
        log.error("Error occurs {}", e.toString());  // 발생한 예외의 정보를 ERROR 수준으로 로그에 기록.

        // 예외에 따라 적절한 HTTP 상태 코드와 응답 본문을 설정하여 반환
        return ResponseEntity.status(e.getErrorCode().getStatus())  // 예외에서 얻은 HTTP 상태 코드로 응답 상태 설정
                .body(Response.error(e.getErrorCode().name()));  // 에러 코드의 이름을 응답 본문에 포함시켜 반환.
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e){

        // 예외 발생 시 해당 예외에 대한 정보와 함께 로그를 남긴다.
        log.error("Error occurs {}", e.toString());  // 발생한 예외의 정보를 ERROR 수준으로 로그에 기록.

        // 예외에 따라 적절한 HTTP 상태 코드와 응답 본문을 설정하여 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)  //  HTTP 상태 코드로 응답 상태 설정
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));  // 에러 코드의 이름을 응답 본문에 포함시켜 반환.
    }
}
