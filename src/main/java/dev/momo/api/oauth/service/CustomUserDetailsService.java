package dev.momo.api.oauth.service;


import dev.momo.api.member.entity.User;
import dev.momo.api.member.repository.UserRepository;
import dev.momo.api.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /*
     * UserDetailsService 인터페이스는 DB에서 유저 정보를 가져오는 역할을 한다.
     * 사용자 관리를 하기 위해서 UserDetailsService를 사용한다.
     * UserDetails안에 사용자의 정보가 들어있다.
     * DB에 있는 이용자의 정보와 화면에서 입력한 로그인 정보를 비교하게 됨.
     *
     * UserDetails는 사용자의 정보를 담는 인터페이스, 직접 상속받아 사용하면된다.
       UserDetails 인터페이스를 구현하게 되면 오버라이드되는 메소드들이 있다.
       이 메소드들에 대해 파악을 해야 된다. 그리고 회원 정보에 관한 다른 정보(이름, 나이, 생년월일, ...)도 추가해도 된다.
       오버라이드되는 메소드들만 Spring Security에서 알아서 이용하기 때문에 따로 클래스를 만들지 않고 멤버변수를 추가해서 같이 사용해도 무방하다.
       만든 멤버변 수들은 getter, setter를 만들어서 사용하면 된다.
     * */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException("Can not find username.");
        }
        return UserPrincipal.create(user);
    }
}
