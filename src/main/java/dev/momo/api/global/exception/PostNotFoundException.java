package dev.momo.api.global.exception;

import dev.momo.api.global.exception.common.CustomBaseException;
import dev.momo.api.global.response.BaseResponseStatus;

public class PostNotFoundException extends CustomBaseException {

    public PostNotFoundException() {
        super(BaseResponseStatus.NOT_FOUND_POST_EXCEPTION);
    }

    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(BaseResponseStatus status) {
        super(status);
    }

}
