package com.java.oms.repository;

import com.java.oms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPasswordResetToken(String passwordResetToken);

    Optional<User> findFirstByUsername(String username);

    Optional<User> findFirstByPasswordResetToken(String passwordResetToken);

    Optional<User> findFirstByVerificationToken(String verificationToken);

    @Query("select u from User u where (u.name like :keyword% or u.email like :keyword% or u.phone like :keyword% or u.username like :keyword% or cast(u.creationTimestamp as string) like :keyword%) and (cast(u.role.name as string) like :roleName%)")
    Page<User> findPage(@Param("roleName") String roleName, @Param("keyword") String keyword, Pageable pageable);

}

