package com.veracode.storypointer.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER_STORY_POINTS")
@Data
@NoArgsConstructor
public class UserStoryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "STORY_ID")
    private String storyId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "ESTIMATE")
    private Integer estimate;

    @Column(name = "TEAM")
    private String team;
}
