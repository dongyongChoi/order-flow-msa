package io.github.cni274.orderservice.user.client;

import io.github.cni274.orderservice.user.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/api/users")
    ResponseEntity<UserResponse> getUserById(@RequestParam String email);
}
