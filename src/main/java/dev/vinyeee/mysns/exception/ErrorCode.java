package dev.vinyeee.mysns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"User name is duplicated"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Internal server error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"User not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"Password is invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"Token is invalid");

    private HttpStatus status;
    private String message;


}
