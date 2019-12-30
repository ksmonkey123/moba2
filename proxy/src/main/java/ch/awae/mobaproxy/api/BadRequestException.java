package ch.awae.mobaproxy.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException extends RuntimeException {

    BadRequestException(String msg) {
        super(msg);
    }

}
