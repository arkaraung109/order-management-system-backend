package com.java.oms.service;

import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.UserDto;
import com.java.oms.entity.User;
import com.java.oms.exception.NotFoundException;
import com.java.oms.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${app.baseURL}")
    private String baseURL;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean findExistByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public boolean findExistByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public boolean findExistByPasswordResetToken(String passwordResetToken) {
        return this.userRepository.existsByPasswordResetToken(passwordResetToken);
    }

    @Override
    public boolean checkOldPassword(String encodedPassword, String rawPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> optional = this.userRepository.findById(id);
        return optional.map((user) -> this.modelMapper.map(user, UserDto.class)).orElseThrow(() -> new NotFoundException("User is not found."));
    }

    @Override
    public UserDto findByUsername(String username) {
        Optional<User> optional = this.userRepository.findFirstByUsername(username);
        return optional.map((user) -> this.modelMapper.map(user, UserDto.class)).orElseThrow(() -> new NotFoundException("User is not found."));
    }

    @Override
    public UserDto findByPasswordResetToken(String passwordResetToken) {
        Optional<User> optional = this.userRepository.findFirstByPasswordResetToken(passwordResetToken);
        return optional.map((user) -> this.modelMapper.map(user, UserDto.class)).orElseThrow(() -> new NotFoundException("Password Reset Token is invalid."));
    }

    @Override
    public UserDto findByVerificationToken(String verificationToken) {
        Optional<User> optional = this.userRepository.findFirstByVerificationToken(verificationToken);
        return optional.map((user) -> this.modelMapper.map(user, UserDto.class)).orElseThrow(() -> new NotFoundException("Verification Token is invalid."));
    }

    @Override
    public PaginationResponse<UserDto> findPage(String roleName, String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id"));
        Page<User> page = this.userRepository.findPage(roleName, keyword, pageable);
        PaginationResponse<UserDto> paginationResponse = new PaginationResponse<UserDto>();
        paginationResponse.setElementList(page.stream().map((user) -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList()));
        paginationResponse.setTotalElements(page.getTotalElements());
        paginationResponse.setTotalPages(page.getTotalPages());
        paginationResponse.setPageSize(page.getSize());
        return paginationResponse;
    }

    @Override
    public UserDto save(UserDto userDto) {
        userDto.setVerificationToken(UUID.randomUUID().toString());
        userDto.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        User user = this.userRepository.save(this.modelMapper.map(userDto, User.class));
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public void update(UserDto userDto) {
        this.userRepository.save(this.modelMapper.map(userDto, User.class));
    }

    @Override
    public void resetPassword(UserDto userDto, String newPassword) {
        userDto.setPassword(this.passwordEncoder.encode(newPassword));
        userDto.setPasswordResetToken(null);
        this.userRepository.save(this.modelMapper.map(userDto, User.class));
    }

    @Override
    public void changePassword(UserDto userDto, String newPassword) {
        userDto.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(this.modelMapper.map(userDto, User.class));
    }

    @Override
    public void sendPasswordResetEmail(UserDto userDto) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset";
        String senderName = "Global Generation Plastic Co., Ltd";
        String mailContent = "<p>Dear " + userDto.getName() + ", </p>";
        mailContent += "<p>If you forgot password of your account, please click the link below to reset new password.</p>";
        mailContent += "<h3><a href='" + baseURL + "/forgot-password/reset-password?token=" + userDto.getPasswordResetToken() + "'>RESET</a></h3>";
        mailContent += "<p>Thank you<br>Global Generation Plastic Co., Ltd</p>";

        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("arkaraung109@gmail.com", senderName);
        messageHelper.setTo(userDto.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        this.javaMailSender.send(message);
    }

    @Override
    public void sendActivationEmail(UserDto userDto) throws MessagingException, UnsupportedEncodingException {
        String subject = "Account Activation";
        String senderName = "Global Generation Plastic Co., Ltd";
        String mailContent = "<p>Dear " + userDto.getName() + ", </p>";
        mailContent += "<p>Your account registration is successfully completed and you need to be verified for account activation, so please click the link below to activate your account.</p>";
        mailContent += "<h3><a href='" + baseURL + "/activation?token=" + userDto.getVerificationToken() + "'>ACTIVATE</a></h3>";
        mailContent += "<p>Thank you<br>Global Generation Plastic Co., Ltd</p>";

        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("arkaraung109@gmail.com", senderName);
        messageHelper.setTo(userDto.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        this.javaMailSender.send(message);
    }

}
