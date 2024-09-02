package com.msvc.user_service.domain.services;

import com.msvc.user_service.api.dtos.converter.UserConverter;
import com.msvc.user_service.api.dtos.request.UserRequestDTO;
import com.msvc.user_service.api.dtos.response.UserDTO;
import com.msvc.user_service.domain.entities.User;
import com.msvc.user_service.domain.exceptions.UserNotFoundException;
import com.msvc.user_service.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Transactional
    @Override
    public UserDTO saveUser(UserRequestDTO userRequestDTO) {
        User user = userConverter.toEntity(userRequestDTO);
        return userConverter.toDTO(userRepository.save(user));
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userConverter.toUserDTOPage(userPage);
    }

    @Override
    public UserDTO getUser(String userId) {
        User user = optionalUser(userId);
        return userConverter.toDTO(user);
    }

    @Override
    public User optionalUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
