package com.spring_cloud.eureka.client.auth.dto;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReqDto {
    private String userId;
    private String username;
    private String password;
}
