package com.veracode.storypointer.repository;

import com.veracode.storypointer.entity.SessionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionUserRepository extends JpaRepository<SessionUser, Integer> {
    List<SessionUser> findAllBySessionId(String sessionId);
    void deleteAllBySessionId(String sessionId);
    SessionUser findBySessionIdAndUserName(String sessionId, String userName);
    SessionUser findByUserName(String userName);
    void deleteByUserNameAndSessionId(String sessionId, String userName);
}
