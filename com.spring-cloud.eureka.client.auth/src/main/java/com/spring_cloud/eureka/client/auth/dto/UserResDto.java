package com.spring_cloud.eureka.client.auth.dto;

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

    public UserResDto(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
