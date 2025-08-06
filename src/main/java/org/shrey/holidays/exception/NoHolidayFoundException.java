package org.shrey.holidays.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoHolidayFoundException extends RuntimeException {

    public NoHolidayFoundException() {
        super("No holiday found");
    }
}
