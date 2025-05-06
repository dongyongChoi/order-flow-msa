package io.github.cni274.userservice.service;

import io.github.cni274.userservice.dto.UserDto;
import io.github.cni274.userservice.entity.User;
import io.github.cni274.userservice.enums.ErrorResult;
import io.github.cni274.userservice.exception.UserException;
import io.github.cni274.userservice.repository.UserRepository;
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
        UserException userException = assertThrows(UserException.class, () -> userService.registerUser(username, email));

        assertThat(userException.getErrorResult()).isEqualTo(ErrorResult.DUPLICATE_EMAIL);
        assertThat(userException.getErrorResult().getMessage()).isEqualTo("이메일이 중복입니다.");

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

    @Test
    @DisplayName("이메일로 유저 찾기 실패 - 존재하는 이메일 없음")
    void failedGetUserByEmail_DuplicateEmail() {
        // given
        doReturn(Optional.empty()).when(userRepository).findByEmail(email);

        // when
        UserException userException = assertThrows(UserException.class, () -> userService.getUserByEmail(email));

        // then
        assertThat(userException).isNotNull();
        assertThat(userException.getErrorResult()).isEqualTo(ErrorResult.NOT_FOUND_USER);
        assertThat(userException.getErrorResult().getMessage()).isEqualTo("사용자가 존재하지 않습니다.");

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("이메일로 유저 찾기 성공")
    void successfulGetUserByEmail() {
        // given
        doReturn(Optional.of(getUser(-1L))).when(userRepository).findByEmail(email);

        // when
        UserDto resultUser = userService.getUserByEmail(email);

        // then
        assertThat(resultUser).isNotNull();
        assertThat(resultUser.getId()).isEqualTo(-1L);
        assertThat(resultUser.getUsername()).isEqualTo(username);
        assertThat(resultUser.getEmail()).isEqualTo(email);

        verify(userRepository).findByEmail(email);
    }

    private User getUser(Long id) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
    }
}