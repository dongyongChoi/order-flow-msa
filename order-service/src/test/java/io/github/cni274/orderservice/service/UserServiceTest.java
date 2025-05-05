package io.github.cni274.orderservice.service;

import io.github.cni274.orderservice.dto.UserDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        doReturn(Optional.of(new User())).when(userRepository).findByEmail(email);

        // when
        assertThrows(UserException.class, () -> userService.registerUser(username, email));

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("유저 등록 성공")
    void successfulRegisterUser() {
        // given
        doReturn(Optional.empty()).when(userRepository).findByEmail(email);
        doReturn(getUser(-1L)).when(userRepository).save(any(User.class));

        // when
        UserDto userDto = userService.registerUser(username, email);

        // then
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(-1L);
        assertThat(userDto.getUsername()).isEqualTo(username);
        assertThat(userDto.getEmail()).isEqualTo(email);

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    private User getUser(Long id) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
    }
}