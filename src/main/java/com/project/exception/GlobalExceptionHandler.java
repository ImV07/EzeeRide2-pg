package com.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    //400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException bre) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bre.getMessage());
    }

    //404
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFound rnfe) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rnfe.getMessage());
    }

    //403
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException se) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(se.getMessage());
    }

    //403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ade) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Insufficient permissions");
    }

    //401
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidToken(InvalidTokenException ite) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT Token: " + ite.getMessage());
    }

    //409
    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<String> handleDuplicateRequest(DuplicateRequestException dre) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dre.getMessage());
    }

    @ExceptionHandler(BookingStatusException.class)
    public ResponseEntity<?> handleBookingStatusException(BookingStatusException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", true);
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}
