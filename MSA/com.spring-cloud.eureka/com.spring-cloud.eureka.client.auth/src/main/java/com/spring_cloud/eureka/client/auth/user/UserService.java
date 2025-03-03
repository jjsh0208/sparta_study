package com.spring_cloud.eureka.client.auth.user;

import com.spring_cloud.eureka.client.auth.core.domain.User;
import com.spring_cloud.eureka.client.auth.core.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Transactional
public class UserService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    * 생성자
    * Base64 URL 인코딩된 비밀키를 디코딩하여 HMAC-SHA 알고리즘에 적합한 SecretKey 객체를 생성
    * */
    public UserService(@Value("${service.jwt.secret-key}") String secretKey,
                       UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    * 로그인 정보를 받아 사용자 인증
    * 
    * @param  SignInReqDto (userId 사용자 ID , password 비밀번호)
    * @Return JWT 엑세스 토큰
    * */
    public String signIn(SignInReqDto signInReqDto) {
        User user = userRepository.findByUserId(signInReqDto.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("Invalid user Id or password"));

        if (!passwordEncoder.matches(signInReqDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Invalid user Id or password");
        }
        return createAccessToken(user.getUserId(), user.getRole().toString());
    }

    /*
    * 사용자 정보를 받아 사용자 등록
    *
    * @param  SignUpReqDto (userId 사용자 ID, username 회원명, password 비밀번호)
    * @Return SignUpResDto  (userId 사용자 ID, username 회원명, role 회원권한)
    * */
    public UserResDto signUp(SignUpReqDto signInReqDto) {
        User user = User.builder()
                .userId(signInReqDto.getUserId())
                .password(passwordEncoder.encode(signInReqDto.getPassword()))
                .username(signInReqDto.getUsername())
                .role(UserRole.MEMBER)
                .build();
        return  userRepository.save(user).toResDto();
    }


    /*
     *  사용자 계정을 받아 JWT 엑세스 토큰을 생성한다.
     * */
    public String createAccessToken(String userId ,String role){
        return Jwts.builder()
                .claim("USER_ID",userId)
                .claim("USER_ROLE",role)
                .issuer(issuer) //발급자
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey , Jwts.SIG.HS512)
                .compact();
    }


}
