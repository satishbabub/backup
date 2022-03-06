package com.veracode.storypointer.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SESSION")
@Data
@NoArgsConstructor
public class Session {

   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;*/

    @Id
    @Column(name = "TEAM")
    private String team;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @Column(name = "CREATED_BY")
    private String createdBy;
}
