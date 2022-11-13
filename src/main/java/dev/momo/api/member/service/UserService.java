package dev.momo.api.member.service;


import dev.momo.api.global.exception.BadRequestException;
import dev.momo.api.member.entity.User;
import dev.momo.api.member.repository.UserRepository;
import dev.momo.api.oauth.entity.ProviderType;
import dev.momo.api.oauth.entity.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    * 패스워드 체크
    * */
    private void checkPassword(String password, String encodedPassword) {
        boolean isSame = passwordEncoder.matches(password, encodedPassword);
        if(!isSame) {
            throw new BadRequestException("비밀번호를 확인하세요.");
        }
    }

    /*
    * email 체크
    * */
    private void checkEmailIsDuplicate(String email) {
        boolean isDuplicate = userRepository.existsByEmail(email);
        if(isDuplicate) {
            throw new BadRequestException("이미 존재하는 회원입니다.");
        }
    }

    /*
    * 가입된 user인지 확인(추후에 수정 필요!!)
    * */
    public void checkUser(String email, String password){
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("존재하지 않는 Email입니다."));
//        if (user.getStatus().equals("9")){
//            new BadRequestException("존재하지 않는 Email입니다.");
//        }
        Optional<User> targetEntity = userRepository.findByEmail(email);
        if (!targetEntity.isEmpty()){
            User user = targetEntity.get();
            if (user.getStatus().equals("9")){
                throw new BadRequestException("존재하지 않는 Email입니다.");
            }
            checkPassword(password, user.getPassword());
        }else{
            throw new BadRequestException("존재하지 않는 Email입니다.");
        }


    }

    /*
    * 회원가입
    * */
    @Transactional
    public void signUp(String email, String password) {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        ProviderType providerType = ProviderType.valueOf("LOCAL".toUpperCase());

        checkEmailIsDuplicate(email);
        String encodedPassword = passwordEncoder.encode(password);

        user.Account(
                email,
                encodedPassword,
                "Y",
                providerType,
                RoleType.USER,
                now,
                now,
                "1"
        );
        userRepository.save(user);
    }

    /*
    * 비밀번호 변경
    * */
    @Transactional
    public void update(String email, String newPassword){
//        LocalDateTime now = LocalDateTime.now();
//        Optional<User> targetEntity = this.userRepository.findByEmail(email);
//        if (targetEntity.isEmpty()){
//            throw new BadRequestException("아이디 혹은 비밀번호를 확인하세요.");
//        }
//        User user = targetEntity.get();
//        ProviderType providerType = ProviderType.valueOf("LOCAL".toUpperCase());

        User user = userRepository
                .findByEmail(email).orElseThrow(() -> new BadRequestException("아이디 혹은 비밀번호를 확인하세요."));
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.changeUpdatePassword(encodedPassword);
    }

    /*
    * 회원탈퇴
    * */
    @Transactional
    public void resign(String email, String password){
        User user = userRepository
                .findByEmail(email).orElseThrow(() -> new BadRequestException("아이디 혹은 비밀번호를 확인하세요."));
        checkPassword(password, user.getPassword());
        user.changeDeleteUser(user);
    }


    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }


}