package com.veracode.storypointer.entity;

import com.veracode.storypointer.util.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class SessionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SESSION_ID")
    private String sessionId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_ROLE")
    private Role userRole;
}
