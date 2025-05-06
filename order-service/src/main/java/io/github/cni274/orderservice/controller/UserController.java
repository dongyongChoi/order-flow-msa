package io.github.cni274.orderservice.controller;

import io.github.cni274.orderservice.dto.UserDto;
import io.github.cni274.orderservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto userDto) {
        UserDto addUserDto = userService.registerUser(userDto.getUsername(), userDto.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addUserDto);
    }
}
