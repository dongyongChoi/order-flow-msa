package io.github.cni274.orderservice.dto;

import io.github.cni274.orderservice.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;

    public static UserDto valueOf(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
