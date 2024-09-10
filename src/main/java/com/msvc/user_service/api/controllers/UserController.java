package com.msvc.user_service.api.controllers;

import com.msvc.user_service.api.dtos.request.UserRequestDTO;
import com.msvc.user_service.api.dtos.response.UserDTO;
import com.msvc.user_service.domain.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Page<UserDTO> pageUsers(
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return userService.getAllUsers(pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDTO saveUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return userService.saveUser(userRequestDTO);
    }

    @GetMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    public UserDTO findUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable String userId, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        return userService.updateUser(userId, userRequestDTO);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

    public UserDTO ratingHotelFallback(String userId, Throwable e) {
        log.error("Hotel service is temporarily unavailable for user: {}. Cause: {}", userId, e.getMessage());
        return UserDTO.builder()
                .id(userId)
                .information("Service is temporarily unavailable")
                .build();
    }

}
