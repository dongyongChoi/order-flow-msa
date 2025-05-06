package io.github.cni274.userservice.controller;

import io.github.cni274.userservice.dto.UserDto;
import io.github.cni274.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<UserDto> getUser(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}
