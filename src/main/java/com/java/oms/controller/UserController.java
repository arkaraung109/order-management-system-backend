package com.java.oms.controller;

import com.java.oms.dto.HttpResponse;
import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.UserDto;
import com.java.oms.exception.DuplicatedException;
import com.java.oms.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/findByUsername")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'DELIVERY_MANAGER', 'SALES_STAFF')")
    // "T(com.java.oms.enums.UserRole).ADMIN.toString()" is used for enum
    public ResponseEntity<UserDto> findByUsername(@RequestParam("username") String username) {
        // Find user by username
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findByUsername(username));
    }

    @GetMapping("/findExistByPasswordResetToken")
    public ResponseEntity<Boolean> findExistByPasswordResetToken(@RequestParam("token") String token) {
        // Find exist by password reset token
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findExistByPasswordResetToken(token));
    }

    @GetMapping("/findPage")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<PaginationResponse<UserDto>> findPage(
            @RequestParam(defaultValue = "") String roleName,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findPage(roleName, keyword, pageNo, pageSize));
    }

    @PostMapping("/create")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> create(@RequestBody UserDto requestDto) throws MessagingException, UnsupportedEncodingException {
        List<String> duplicationList = new ArrayList<>();
        String duplication = "";

        // If email already exists
        if(this.userService.findExistByEmail(requestDto.getEmail())) {
            duplicationList.add("Duplicated Email");
        }

        // If username already exists
        if(this.userService.findExistByUsername(requestDto.getUsername())) {
            duplicationList.add("Duplicated Username");
        }

        duplication = String.join(" & ", duplicationList);

        // If duplication is found
        if(!duplication.isEmpty()) {
            throw new DuplicatedException(duplication);
        }

        // User is saved
        UserDto userDto = this.userService.save(requestDto);

        // Send account activation email
        this.userService.sendActivationEmail(userDto);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.CREATED.value())
                .title("System User Creation")
                .message("Successfully created.")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/activate")
    public ResponseEntity<HttpResponse> activate(@RequestBody String verificationToken) {
        // Find user by verification token
        UserDto userDto = this.userService.findByVerificationToken(verificationToken);

        // User is updated
        userDto.setActive(true);
        userDto.setVerificationToken(null);
        this.userService.update(userDto);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Account Activation")
                .message("Successfully activated.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/changeStatus")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> changeStatus(@RequestBody Long id) {
        // Find user by id
        UserDto userDto = this.userService.findById(id);

        // User is updated
        userDto.setActive(!userDto.isActive());
        this.userService.update(userDto);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Account Status Change")
                .message("Successfully changed status.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
