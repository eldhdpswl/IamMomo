package dev.momo.api.global.exception;

import dev.momo.api.global.exception.common.CustomBaseException;
import dev.momo.api.global.response.BaseResponseStatus;

public class CommentNotFoundException extends CustomBaseException {

    public CommentNotFoundException() {
        super(BaseResponseStatus.NOT_FOUND_COMMENT_EXCEPTION);
    }

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(BaseResponseStatus status) {
        super(status);
    }

}
