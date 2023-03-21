package com.blog.entity;

import com.blog.oAuth.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;
    private String pwd;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email,String pwd, Role role,String name){
        this.name=name;
        this.email=email;
        this.pwd=pwd;
        this.role=role;
    }
    public User update(String name) {
        this.name = name;
        return this;
    }
    public String getRoleKey() {
        return this.role.getRole();
    }
    /*
    @Builder(builderClassName = "UserDetailRegister", builderMethodName = "userDetailRegister")
    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public User(String username, String password, String email, Role role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;

    } */


}
