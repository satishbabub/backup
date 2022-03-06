package com.veracode.storypointer.repository;

import com.veracode.storypointer.entity.StoryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryPointsRepository extends JpaRepository<StoryPoint, String> {
    StoryPoint findByStoryId(String storyId);
    void deleteAllByTeam(String team);
}
