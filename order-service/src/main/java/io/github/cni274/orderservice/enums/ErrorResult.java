package io.github.cni274.orderservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResult {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 중복입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
