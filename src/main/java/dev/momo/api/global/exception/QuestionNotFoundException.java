package dev.momo.api.global.exception;

import dev.momo.api.global.exception.common.CustomBaseException;
import dev.momo.api.global.response.BaseResponseStatus;

public class QuestionNotFoundException extends CustomBaseException {

    public QuestionNotFoundException() {
        super(BaseResponseStatus.NOT_FOUND_QUESTION_EXCEPTION);
    }

    public QuestionNotFoundException(String message) {
        super(message);
    }

    public QuestionNotFoundException(BaseResponseStatus status) {
        super(status);
    }

}
