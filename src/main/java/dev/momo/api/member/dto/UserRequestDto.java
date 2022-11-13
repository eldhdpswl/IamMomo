package dev.momo.api.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRequestDto {
    private String email;
    private String password;
}