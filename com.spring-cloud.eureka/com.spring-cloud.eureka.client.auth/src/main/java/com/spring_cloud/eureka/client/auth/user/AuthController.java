package com.spring_cloud.eureka.client.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<?> createAuthToken(@RequestBody SignInReqDto signInReqDto){
        try{
            String token = userService.signIn(signInReqDto);
            return ResponseEntity.ok(new SignInResDto(token));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status" , HttpStatus.UNAUTHORIZED.value(),
                            "message", e.getMessage()
                    ));
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpReqDto signUpReqDto){
        System.out.println("test");
        return ResponseEntity.ok(userService.signUp(signUpReqDto));
    }

}
