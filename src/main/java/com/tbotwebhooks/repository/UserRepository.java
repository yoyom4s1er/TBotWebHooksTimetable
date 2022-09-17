package com.tbotwebhooks.repository;

import com.tbotwebhooks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(long id);
    List<User> findAllByNotificationSubscription(boolean subscription);
}
