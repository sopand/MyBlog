package com.blog.entity;

import com.blog.dto.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String pwd;
    @Enumerated(EnumType.STRING)
    private Role role;

    public String getRole() {
        return this.role.getValue();
    }


}
