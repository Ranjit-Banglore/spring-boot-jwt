package de.infinity.jwt.exception;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private int code;
    private HttpStatus error;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HttpStatus getError() {
        return error;
    }

    public void setError(HttpStatus error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
