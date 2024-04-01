package com.vamos.characterlit.auth2.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getStatus();

    String getErrorCode();

    String getMessage();
}

