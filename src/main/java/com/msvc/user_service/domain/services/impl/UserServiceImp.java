package com.msvc.user_service.domain.services.impl;

import com.msvc.user_service.api.dtos.converter.UserConverter;
import com.msvc.user_service.api.dtos.request.UserRequestDTO;
import com.msvc.user_service.api.dtos.response.UserDTO;
import com.msvc.user_service.domain.entities.User;
import com.msvc.user_service.domain.exceptions.EmailAlreadyExistsException;
import com.msvc.user_service.domain.exceptions.NameAlreadyExistsException;
import com.msvc.user_service.domain.exceptions.UserNotFoundException;
import com.msvc.user_service.domain.repositories.UserRepository;
import com.msvc.user_service.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {

    @Value("${mcsv.qualification.url}")
    private String urlQualification;

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RestTemplate restTemplate;

    @Transactional
    @Override
    public UserDTO saveUser(UserRequestDTO userRequestDTO) {
        User user = userConverter.toEntity(userRequestDTO);
        validateUser(userRequestDTO);
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

        findQualificationByUserId(userId, user);

        return userConverter.toDTO(user);
    }

    @Transactional
    @Override
    public UserDTO updateUser(String userId, UserRequestDTO userRequestDTO) {
        User user = optionalUser(userId);
        validateUser(userRequestDTO);
        User updateUser = userConverter.toUpdateUser(user, userRequestDTO);

        return userConverter.toDTO(userRepository.save(updateUser));
    }

    @Transactional
    @Override
    public void deleteUser(String userId) {
        User user = optionalUser(userId);
        userRepository.delete(user);
    }

    @Override
    public User optionalUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void findQualificationByUserId(String userId, User user) {
        ArrayList qualifiersByUser = restTemplate.getForObject(urlQualification + userId, ArrayList.class);
        user.setQualifiers(qualifiersByUser);
        log.info("{}", qualifiersByUser);
    }

    private void validateUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByName(userRequestDTO.getName())) {
            throw new NameAlreadyExistsException(userRequestDTO.getName());
        }

        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(userRequestDTO.getEmail());
        }
    }

}
