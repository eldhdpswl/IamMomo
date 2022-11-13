package dev.momo.api.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HeaderUtil {

    /*
    *  일반적으로 토큰은 요청 헤더의 Authorization 필드에 담아져 보내집니다.
       Authorization: <type> <credentials>

    *  bearer는 위 형식에서 type에 해당합니다. 토큰에는 많은 종류가 있고 서버는 다양한 종류의 토큰을 처리하기 위해
       전송받은 type에 따라 토큰을 다르게 처리합니다.

    * */
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION); //header에서 HEADER_AUTHORIZATION에 해당하는 부분을 찾는다.
        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}