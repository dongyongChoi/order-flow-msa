package io.github.cni274.userservice.repository;

import io.github.cni274.userservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    String username = "tester";
    String email = "test@test.com";

    @BeforeEach
    void setUp() {
        // 전체 제거
        userRepository.deleteAll();

        // 테스트 유저 등록
        User testerUser = User.builder()
                .username(username)
                .email(email)
                .build();

        userRepository.save(testerUser);
    }

    @Test
    @DisplayName("이메일은 중복 등록될 수 없다")
    void failedSave_DuplicateEmail() {
        // given
        User addUser = User.builder()
                .username("tester2")
                .email(email)
                .build();

        assertThatThrownBy(() -> userRepository.save(addUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("이메일로 유저를 조회한다")
    void successfulFindByEmail() {
        Optional<User> findUserOptional = userRepository.findByEmail(email);

        assertThat(findUserOptional).isPresent();
        assertThat(findUserOptional.get().getEmail()).isEqualTo(email);
        assertThat(findUserOptional.get().getUsername()).isEqualTo(username);
    }
}