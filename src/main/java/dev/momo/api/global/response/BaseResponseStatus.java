package dev.momo.api.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    INVALID_VALUE_PARAM( false,2010, "잘못된 파라미터 값 입니다."),
    INVALID_VALUE(false,2011,"제대로된 입력값을 작성해주세요."),
    IS_DELETED_VALUE(false,2012,"삭제된 값입니다."),

    NOT_FOUND_CATEGORY_EXCEPTION(false,2022,"존재하지 않는 카테고리입니다"),
    NOT_FOUND_QUESTION_EXCEPTION(false,2023,"존재하지 않는 질문입니다"),
    NOT_FOUND_POST_EXCEPTION(false,2024,"존재하지 않는 게시글 입니다"),
    NOT_FOUND_COMMENT_EXCEPTION(false,2025,"존재하지 않는 댓글 입니다"),



    /**
     * 3000 : Response 오류
     */

    /**
     * 4000 : Database, Server 오류
     */
    INTERNAL_SERVER_EXCEPTION(false, 4010, "서버 내부 에러입니다. 관리자에게 문의하세요."),

    ;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}
