package io.github.cni274.userservice.exception;

import io.github.cni274.userservice.enums.ErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserException extends RuntimeException {
    private final ErrorResult errorResult;
}
