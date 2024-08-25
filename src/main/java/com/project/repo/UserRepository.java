package com.project.repo;

import com.project.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.email = :email")
    User getUserByUserName(@Param("email") String email);

    List<User> findByNameContaining(String name);

    @Query("from User u")
    Page<User> findAllUsers(Pageable pageable);
}
