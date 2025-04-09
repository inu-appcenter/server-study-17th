package com.example.service;

import com.example.DTO.UserSignupRequest;
import com.example.DTO.UserUpdateRequest;
import com.example.domain.user.User;
import com.example.domain.user.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private UserRepository userRepository;

    public User CreateUser(UserSignupRequest request) {
        User user = new User(
                request.getName(),
                request.getPhoneNumber(),
                request.getAddress()
        );

        return userRepository.save(user);
    }

    public void UpdateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        user.UpdateInfo(request.getName(),
                request.getPhoneNumber(),
                request.getAddress());
    }


}
