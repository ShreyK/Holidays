package org.shrey.holidays.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleException(RuntimeException e) {
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public ErrorResponse handleFileUploadException(FileUploadException e) {
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NoHolidayFoundException.class)
    public ErrorResponse handleNoHolidayFoundException(NoHolidayFoundException e) {
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.getMessage());
    }
}
