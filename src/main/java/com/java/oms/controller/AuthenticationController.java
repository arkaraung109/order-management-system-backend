package com.java.oms.controller;

import com.java.oms.dto.AuthRequestDto;
import com.java.oms.dto.AuthResponseDto;
import com.java.oms.dto.AuthenticatedUser;
import com.java.oms.exception.NotFoundException;
import com.java.oms.service.UserService;
import com.java.oms.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/authenticate")
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDto authRequestDto) {
        // If user does not exist with this username
        if(!this.userService.findExistByUsername(authRequestDto.getUsername())) {
            throw new NotFoundException("User is not found with this username.");
        }

        // Authenticate with username and password
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));

        // Generate JWT Token
        AuthenticatedUser authUser = (AuthenticatedUser) authentication.getPrincipal();
        String jwtToken = this.jwtUtil.generateToken(authRequestDto.getUsername(), String.valueOf(authUser.getRole().getName()));
        AuthResponseDto authResponseDto = AuthResponseDto.builder()
                .access_token(jwtToken)
                .build();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).body(authResponseDto);
    }

}
