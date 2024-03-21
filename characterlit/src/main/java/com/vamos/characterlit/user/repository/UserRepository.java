package com.vamos.characterlit.user.repository;

import com.vamos.characterlit.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long userId);


}
