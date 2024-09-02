package com.msvc.user_service.api.dtos.converter;

import com.msvc.user_service.api.dtos.request.UserRequestDTO;
import com.msvc.user_service.api.dtos.response.UserDTO;
import com.msvc.user_service.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserConverter {
    private UserConverter() {
    }

    public Page<UserDTO> toUserDTOPage(Page<User> userPage) {
        return userPage.map(this::toDTO);
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .information(user.getInformation())
                .creationDate(user.getCreationDate())
                .build();
    }

    public User toEntity(UserRequestDTO userRequestDTO) {
        return User.builder()
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .information(userRequestDTO.getInformation())
                .creationDate(LocalDate.now())
                .build();
    }
}
