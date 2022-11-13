package dev.momo.api.global.exception.common;

import dev.momo.api.global.response.BaseResponseStatus;

public class IsDeletedException extends CustomBaseException {

    public IsDeletedException() {
        super(BaseResponseStatus.IS_DELETED_VALUE);
    }

    public IsDeletedException(String message) {
        super(message);
    }

    public IsDeletedException(BaseResponseStatus status) {
        super(status);
    }


}
