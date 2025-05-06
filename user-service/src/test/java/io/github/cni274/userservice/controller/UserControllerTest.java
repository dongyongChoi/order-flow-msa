package io.github.cni274.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cni274.userservice.advice.GlobalRestControllerAdvice;
import io.github.cni274.userservice.dto.UserDto;
import io.github.cni274.userservice.enums.ErrorResult;
import io.github.cni274.userservice.exception.UserException;
import io.github.cni274.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

import static io.github.cni274.userservice.advice.GlobalRestControllerAdvice.ErrorResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    MockMvc mockMvc;

    static ObjectMapper objectMapper = new ObjectMapper();

    String username = "tester";
    String email = "test@test.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalRestControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("유저 등록 실패 - username, email은 필수")
    void failedRegisterUser_RequiredFields() throws Exception {
        // given
        UserDto addUser = UserDto.builder()
                .username("")
                .email("")
                .build();
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .content(objectMapper.writeValueAsString(addUser))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }



    @Test
    @DisplayName("유저 등록 실패 - 이메일이 존재함")
    void failedRegisterUser_DuplicateEmail() throws Exception {
        // given
        UserDto addUser = UserDto.builder()
                .username(username)
                .email(email)
                .build();

        doThrow(new UserException(ErrorResult.DUPLICATE_EMAIL))
                .when(userService)
                .registerUser(addUser.getUsername(), addUser.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .content(objectMapper.writeValueAsString(addUser))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        ErrorResponse errorResponse = getErrorResponse(resultActions);

        resultActions.andExpect(status().isBadRequest());
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorResult.DUPLICATE_EMAIL.getMessage());

        verify(userService).registerUser(addUser.getUsername(), addUser.getEmail());
    }

    @Test
    @DisplayName("유저 등록 성공")
    void successfulRegisterUser() throws Exception {
        // given
        UserDto addUser = UserDto.builder()
                .username(username)
                .email(email)
                .build();

        UserDto returnUserDto = UserDto.builder()
                .id(-1L)
                .username(username)
                .email(email)
                .build();

        doReturn(returnUserDto)
                .when(userService)
                .registerUser(addUser.getUsername(), addUser.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .content(objectMapper.writeValueAsString(addUser))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());
        String body = resultActions.andReturn().getResponse().getContentAsString();
        UserDto userDto = objectMapper.readValue(body, UserDto.class);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(-1L);
        assertThat(userDto.getUsername()).isEqualTo(username);
        assertThat(userDto.getEmail()).isEqualTo(email);

        verify(userService).registerUser(addUser.getUsername(), addUser.getEmail());
    }

    @Test
    @DisplayName("등록된 유저 조회 실패 - 존재하지 않음")
    void failedGetUser() throws Exception {
        doThrow(new UserException(ErrorResult.NOT_FOUND_USER)).when(userService).getUserByEmail(email);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
                        .param("email", email)
        );

        // then
        ErrorResponse errorResponse = getErrorResponse(resultActions);

        resultActions.andExpect(status().isNotFound());
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorResult.NOT_FOUND_USER.getMessage());

        verify(userService).getUserByEmail(email);
    }

    @Test
    @DisplayName("등록된 유저 조회 성공")
    void successfulGetUser() throws Exception {
        // given
        UserDto returnUserDto = UserDto.builder()
                .id(-1L)
                .username(username)
                .email(email)
                .build();

        doReturn(returnUserDto).when(userService).getUserByEmail(email);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
                        .param("email", email)
        );

        // then
        UserDto resultUserDto = getBodyResponse(resultActions, UserDto.class);

        resultActions.andExpect(status().isOk());
        assertThat(resultUserDto).isNotNull();
        assertThat(resultUserDto.getId()).isEqualTo(-1L);
        assertThat(resultUserDto.getUsername()).isEqualTo(username);
        assertThat(resultUserDto.getEmail()).isEqualTo(email);

        verify(userService).getUserByEmail(email);
    }


    private ErrorResponse getErrorResponse(ResultActions resultActions) throws UnsupportedEncodingException, JsonProcessingException {
        return getBodyResponse(resultActions, ErrorResponse.class);
    }


    private <T> T getBodyResponse(ResultActions resultActions, Class<T> clazz) throws UnsupportedEncodingException, JsonProcessingException {
        String body = resultActions.andReturn().getResponse().getContentAsString();
        return StringUtils.hasText(body) ? objectMapper.readValue(body, clazz) : null;
    }
}