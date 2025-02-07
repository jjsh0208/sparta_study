package com.spring_cloud.eureka.client.auth;

import com.spring_cloud.eureka.client.auth.dto.SignInReqDto;
import com.spring_cloud.eureka.client.auth.dto.SignUpReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signIn")
    public ResponseEntity<?> createAuthToken(@RequestBody SignInReqDto signInReqDto){
        try{
            String token = authService.signIn(signInReqDto);
            return ResponseEntity.ok(token);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status" , HttpStatus.UNAUTHORIZED.value(),
                            "message", e.getMessage()
                    ));
        }
    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpReqDto signUpReqDto){
        return ResponseEntity.ok(authService.signUp(signUpReqDto));
    }

}
