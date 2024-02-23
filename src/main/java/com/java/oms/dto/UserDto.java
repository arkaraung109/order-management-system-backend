package com.java.oms.dto;

import com.java.oms.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String username;

    private String password;

    private boolean active;

    private String verificationToken;

    private String passwordResetToken;

    private LocalDateTime creationTimestamp;

    private Role role;

}
