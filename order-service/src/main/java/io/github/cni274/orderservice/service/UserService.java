package io.github.cni274.orderservice.service;

import io.github.cni274.orderservice.entity.User;
import io.github.cni274.orderservice.exception.UserException;
import io.github.cni274.orderservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void registerUser(String username, String email) {
        Optional<User> findEmailOptional = userRepository.findByEmail(email);

        if (findEmailOptional.isPresent()) {
            throw new UserException();
        }
    }
}
