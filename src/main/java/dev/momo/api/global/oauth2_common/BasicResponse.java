package dev.momo.api.global.oauth2_common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BasicResponse {

    /*
    * 일반적인 성공은 String message를 응답하기 때문에 이런 경우에 응답하는 BasicResponseDTO를 작성한다.
    * */

    private String message;
    private HttpStatus status;
}