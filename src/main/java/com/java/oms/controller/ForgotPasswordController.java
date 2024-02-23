package com.java.oms.controller;

import com.java.oms.dto.HttpResponse;
import com.java.oms.dto.PasswordResetDto;
import com.java.oms.dto.UserDto;
import com.java.oms.exception.NotFoundException;
import com.java.oms.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/forgotPassword")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendPasswordResetEmail")
    public ResponseEntity<HttpResponse> sendPasswordResetEmail(@RequestBody String username, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        // Find user by username
        UserDto userDto = this.userService.findByUsername(username);

        // Set new passwordResetToken and user is updated
        userDto.setPasswordResetToken(UUID.randomUUID().toString());
        this.userService.update(userDto);

        // Send password reset email
        this.userService.sendPasswordResetEmail(userDto);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Password Reset Email")
                .message("Successfully sent.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<HttpResponse> resetPassword(@RequestBody PasswordResetDto requestDto) {
        // Find user by password reset token
        UserDto userDto = this.userService.findByPasswordResetToken(requestDto.getPasswordResetToken());

        // If user is not found
        if(userDto == null) {
            throw new NotFoundException("Password Reset Token is invalid.");
        }

        // Reset new password
        this.userService.resetPassword(userDto, requestDto.getNewPassword());

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Password Reset")
                .message("Successfully reset.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    private String generateRandomString() {
//        List<CharacterRule> rules = Arrays.asList(
//                new CharacterRule(EnglishCharacterData.UpperCase, 1),
//                new CharacterRule(EnglishCharacterData.LowerCase, 1),
//                new CharacterRule(EnglishCharacterData.Digit, 1),
//                new CharacterRule(EnglishCharacterData.Special, 1)
//        );
//
//        return new PasswordGenerator().generatePassword(8, rules);
//    }

}
