package io.github.cni274.userservice.dto;

import lombok.Getter;

@Getter
public class ResultDto<T> {
    private T result;
    private String message;
}
