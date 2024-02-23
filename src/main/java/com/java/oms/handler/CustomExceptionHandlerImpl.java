package com.java.oms.handler;

import com.java.oms.dto.HttpResponse;
import com.java.oms.exception.DuplicatedException;
import com.java.oms.exception.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.SignatureException;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandlerImpl {

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<HttpResponse> handleDuplicatedException(DuplicatedException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .title("Duplication")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<HttpResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Method Argument Type Mismatch")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HttpResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .title("Data Integrity Violation")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpResponse> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Constraint Violation")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<HttpResponse> handleNumberFormatException(NumberFormatException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Number Format")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<HttpResponse> handleBadRequestException(BadRequestException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Bad Request")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .title("Access Denied")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<HttpResponse> handleExpiredJwtException(ExpiredJwtException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .title("Expired JWT")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpResponse> handleNotFoundException(NotFoundException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("Not Found")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .title("Bad Credentials")
                .message("Credentials are wrong.")
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> handleDisabledException(DisabledException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.LOCKED.value())
                .title("Account Disabled")
                .message("Please contact administrator.")
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.LOCKED).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<HttpResponse> handleMalformedJwtException(MalformedJwtException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .title("Malformed JWT")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<HttpResponse> handleSignatureException(SignatureException exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .title("Signature JWT")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * handlerOtherExceptions handles any unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> handleGeneralException(Exception exception, WebRequest request) {
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title("Server Error")
                .message(exception.getMessage())
                .build();
        this.showExceptionLog(exception, response, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private void showExceptionLog(Exception exception, HttpResponse response, WebRequest request) {
        log.error(
                "\n----------------------------------------------------------------------" +
                "\nHanding Exception occurred at " + new Date() +
                ",\n" + "Exception Name: " + exception.getClass() +
                "\nHttpStatusCode: " + response.getStatus() +
                "\nMessage: " + response.getMessage() +
                "\nURL Path: " + ((ServletWebRequest) request).getRequest().getRequestURI() +
                "\n----------------------------------------------------------------------"
        );
    }

}