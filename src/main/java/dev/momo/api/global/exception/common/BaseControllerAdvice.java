package dev.momo.api.global.exception.common;

import dev.momo.api.global.exception.CategoryNotFoundException;
import dev.momo.api.global.response.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class BaseControllerAdvice {

    @ExceptionHandler(CategoryNotFoundException.class)
    public BaseResponse<CategoryNotFoundException>
    test(HttpServletRequest request, HttpServletResponse response, CategoryNotFoundException e) {
        return new BaseResponse<CategoryNotFoundException>(e.getStatus());
    }

}
