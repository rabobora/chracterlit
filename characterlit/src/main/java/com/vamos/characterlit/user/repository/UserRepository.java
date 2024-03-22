package com.vamos.characterlit.user.repository;

import com.vamos.characterlit.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
}
