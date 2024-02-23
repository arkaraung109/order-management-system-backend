package com.java.oms.service;

import com.java.oms.dto.AuthenticatedUser;
import com.java.oms.entity.User;
import com.java.oms.exception.NotFoundException;
import com.java.oms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = this.userRepository.findFirstByUsername(username);

        if(optional.isPresent()) {
            User user = optional.get();
            return AuthenticatedUser.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .active(user.isActive())
                    .creationTimestamp(user.getCreationTimestamp())
                    .role(user.getRole())
                    .build();
        } else {
            throw new NotFoundException("User is not found.");
        }
    }

}
