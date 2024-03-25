package com.vamos.characterlit.users.repository;

import com.vamos.characterlit.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

    Users findByUserNumber(Long userNumber);
}
