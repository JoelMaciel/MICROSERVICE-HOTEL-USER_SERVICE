package com.msvc.user_service.domain.services;

import com.msvc.user_service.api.dtos.request.UserRequestDTO;
import com.msvc.user_service.api.dtos.response.UserDTO;
import com.msvc.user_service.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDTO saveUser(UserRequestDTO userRequestDTO);

    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getUser(String userId);

    User optionalUser(String userId);

    UserDTO updateUser(String userId, UserRequestDTO userRequestDTO);

    void deleteUser(String userId);
}
