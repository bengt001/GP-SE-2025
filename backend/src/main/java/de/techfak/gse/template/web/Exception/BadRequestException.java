package de.techfak.gse.template.web.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST) //<1>
public class BadRequestException extends RuntimeException  {
    @Serial
    private static final long serialVersionUID = 42L;
}

