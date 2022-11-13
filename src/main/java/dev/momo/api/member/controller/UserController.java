package dev.momo.api.member.controller;

import dev.momo.api.global.oauth2_common.ApiResponse;
import dev.momo.api.global.oauth2_common.BasicResponse;
import dev.momo.api.member.dto.UserRequestDto;
import dev.momo.api.member.entity.AuthReqModel;
import dev.momo.api.member.entity.User;
import dev.momo.api.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    * 회원탈퇴나 회원정보 수정시, user의 email과 password를 한번 더 확인
    * */
    @PostMapping("/check-user")
    public ResponseEntity<BasicResponse> checkUser(@RequestBody UserRequestDto userRequestDto){
        userService.checkUser(userRequestDto.getEmail(), userRequestDto.getPassword());
        BasicResponse response = new BasicResponse("정상 회원입니다.", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
    * 회원가입
    * */
    @PostMapping("/sign-up")
    public ResponseEntity<BasicResponse> signUp(@RequestBody UserRequestDto userRequestDto) {
        userService.signUp(userRequestDto.getEmail(), userRequestDto.getPassword());
        BasicResponse response = new BasicResponse("회원가입 성공", HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*
    * 회원정보 변경
    * */
    @PutMapping("/update")
    public ResponseEntity<BasicResponse> update(@RequestBody UserRequestDto userRequestDto) {
        userService.update(userRequestDto.getEmail(), userRequestDto.getPassword());
        BasicResponse response = new BasicResponse("변경완료 되었습니다.", HttpStatus.ACCEPTED);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    /*
    * 회원탈퇴
    * */
    @PutMapping("/resign")
    public ResponseEntity<BasicResponse> resign(@RequestBody UserRequestDto userRequestDto) {
        userService.resign(userRequestDto.getEmail(), userRequestDto.getPassword());
        BasicResponse response = new BasicResponse("회원탈퇴 처리되었습니다.", HttpStatus.ACCEPTED);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    @GetMapping
    public ApiResponse getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ApiResponse.success("user", user);

    }

}