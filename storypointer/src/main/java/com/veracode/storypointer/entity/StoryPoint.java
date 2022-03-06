package com.veracode.storypointer.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "STORY_POINTS")
@Data
@NoArgsConstructor
public class StoryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "STORY_ID")
    private String storyId;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @Column(name = "TEAM")
    private String team;

    @Column(name = "ESTIMATE")
    private Integer estimate;
}
