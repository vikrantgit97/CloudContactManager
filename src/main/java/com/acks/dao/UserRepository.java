package com.acks.dao;

import com.acks.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Integer> {

    @Query("select u from Users u where u.email =:email")
    Users getUsersByUserName(@Param("email") String email);

}
