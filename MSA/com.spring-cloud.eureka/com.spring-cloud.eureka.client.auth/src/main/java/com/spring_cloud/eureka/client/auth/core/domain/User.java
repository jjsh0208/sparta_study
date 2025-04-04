package com.spring_cloud.eureka.client.auth.core.domain;

import com.spring_cloud.eureka.client.auth.core.enums.UserRole;
import com.spring_cloud.eureka.client.auth.user.UserResDto;
import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    @Id
    private String userId;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    /*
    *  DTO로 변환하는 메서드
    * */
    public UserResDto toResDto(){
        return new UserResDto(
                this.userId,
                this.username,
                this.role.toString()
        );
    }


}
