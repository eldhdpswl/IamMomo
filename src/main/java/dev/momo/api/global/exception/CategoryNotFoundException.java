package dev.momo.api.global.exception;

import dev.momo.api.global.exception.common.CustomBaseException;
import dev.momo.api.global.response.BaseResponseStatus;

public class CategoryNotFoundException extends CustomBaseException {

    public CategoryNotFoundException() {
        super(BaseResponseStatus.NOT_FOUND_CATEGORY_EXCEPTION);
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(BaseResponseStatus status) {
        super(status);
    }

    public BaseResponseStatus QuestionExceptionMessage(){
        return BaseResponseStatus.NOT_FOUND_QUESTION_EXCEPTION;
    }

}
