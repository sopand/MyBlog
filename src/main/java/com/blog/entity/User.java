package com.blog.entity;

import com.blog.dto.Role;
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
    private String pwd;

    private String role;

    @Builder
    public User(String email,String pwd, String role){
        this.email=email;
        this.pwd=pwd;
        this.role=role;
    }


}
