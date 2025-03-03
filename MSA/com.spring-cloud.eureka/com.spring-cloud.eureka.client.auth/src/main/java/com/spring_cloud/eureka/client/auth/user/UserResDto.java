package com.spring_cloud.eureka.client.auth.user;

import com.spring_cloud.eureka.client.auth.core.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {
    private String userId;
    private String username;
    private String role;

}
