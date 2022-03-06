package com.veracode.storypointer.repository;

import com.veracode.storypointer.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Session findByTeam(String team);
    void deleteByTeam(String team);
}
