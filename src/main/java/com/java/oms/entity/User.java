package com.java.oms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Column(name = "phone", length = 150)
    private String phone;

    @Column(name = "username", length = 150, unique = true)
    private String username;

    @Column(name = "password", length = 150)
    private String password;

    @Column(name = "active")
    private boolean active;

    @Column(name = "verification_token", length = 150)
    private String verificationToken;

    @Column(name = "password_reset_token", length = 150)
    private String passwordResetToken;

    @Column(name = "creation_timestamp")
    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
