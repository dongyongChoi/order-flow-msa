package io.github.cni274.orderservice.service;

import io.github.cni274.orderservice.entity.User;
import io.github.cni274.orderservice.exception.UserException;
import io.github.cni274.orderservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    String username = "tester";
    String email = "test@test.com";

    @Test
    @DisplayName("유저 등록 실패 - 이메일 중복")
    void failedRegisterUser_DuplicateEmail() {
        // given
        doReturn(Optional.of(new User()))
                .when(userRepository)
                .findByEmail(email);

        // when
        assertThrows(UserException.class, () -> userService.registerUser(username, email));

        verify(userRepository).findByEmail(email);
    }
}