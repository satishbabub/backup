package com.veracode.storypointer.repository;

import com.veracode.storypointer.entity.UserStoryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStoryPointsRepository extends JpaRepository<UserStoryPoint, Integer> {
    List<UserStoryPoint> findAllByStoryId(String storyId);
    UserStoryPoint findByUserNameAndStoryId(String userName, String storyId);
    void deleteAllByTeam(String team);
}
