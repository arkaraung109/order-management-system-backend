package com.java.oms.service;

import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.UserDto;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface UserService {

    boolean findExistByUsername(String username);

    boolean findExistByEmail(String email);

    boolean findExistByPasswordResetToken(String passwordResetToken);

    boolean checkOldPassword(String encodedPassword, String rawPassword);

    UserDto findById(Long id);

    UserDto findByUsername(String username);

    UserDto findByPasswordResetToken(String passwordResetToken);

    UserDto findByVerificationToken(String verificationToken);

    PaginationResponse<UserDto> findPage(String roleName, String keyword, int pageNo, int pageSize);

    UserDto save(UserDto userDto);

    void update(UserDto userDto);

    void resetPassword(UserDto userDto, String newPassword);

    void changePassword(UserDto userDto, String newPassword);

    void sendPasswordResetEmail(UserDto userDto) throws MessagingException, UnsupportedEncodingException;

    void sendActivationEmail(UserDto userDto) throws MessagingException, UnsupportedEncodingException;

}
