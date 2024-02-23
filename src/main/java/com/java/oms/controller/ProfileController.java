package com.java.oms.controller;

import com.java.oms.dto.HttpResponse;
import com.java.oms.dto.PasswordChangeDto;
import com.java.oms.dto.ProfileChangeDto;
import com.java.oms.dto.UserDto;
import com.java.oms.exception.DuplicatedException;
import com.java.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PostMapping("/changePassword")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'DELIVERY_MANAGER', 'SALES_STAFF')")
    public ResponseEntity<HttpResponse> changePassword(@RequestBody PasswordChangeDto requestDto, final Authentication authentication) {
        // Check whether authentication exists or not
        if(authentication != null) {
            // Find user by username
            UserDto userDto = this.userService.findByUsername(authentication.getName());

            // If user is disabled
            if(!userDto.isActive()) {
                throw new DisabledException("Account is disabled.");
            }

            // If old password is incorrect
            if(!this.userService.checkOldPassword(userDto.getPassword(), requestDto.getOldPassword())) {
                throw new BadCredentialsException("Old Password is incorrect.");
            }

            // Change new password
            this.userService.changePassword(userDto, requestDto.getNewPassword());

            HttpResponse response = HttpResponse.builder()
                    .status(HttpStatus.OK.value())
                    .title("New Password Change")
                    .message("Successfully changed.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        throw new AccessDeniedException("Authentication does not exist.");
    }

    @PutMapping("/update")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'DELIVERY_MANAGER', 'SALES_STAFF')")
    public ResponseEntity<HttpResponse> update(@RequestBody ProfileChangeDto requestDto, final Authentication authentication) {
        // Check whether authentication exists or not
        if(authentication != null) {
            // Find user by username
            UserDto userDto = this.userService.findByUsername(authentication.getName());

            // If user is disabled
            if(!userDto.isActive()) {
                throw new DisabledException("Account is disabled.");
            }

            // If old password is incorrect
            if(!this.userService.checkOldPassword(userDto.getPassword(), requestDto.getOldPassword())) {
                throw new BadCredentialsException("Old Password is incorrect.");
            }

            // If email already exists
            if(this.userService.findExistByEmail(requestDto.getEmail()) && !Objects.equals(requestDto.getEmail(), userDto.getEmail())) {
                throw new DuplicatedException("Duplicated Email");
            }

            // User is updated
            userDto.setName(requestDto.getName());
            userDto.setEmail(requestDto.getEmail());
            userDto.setPhone(requestDto.getPhone());
            this.userService.update(userDto);

            HttpResponse response = HttpResponse.builder()
                    .status(HttpStatus.OK.value())
                    .title("Profile Update")
                    .message("Successfully updated.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        throw new AccessDeniedException("Authentication does not exist.");
    }

}
