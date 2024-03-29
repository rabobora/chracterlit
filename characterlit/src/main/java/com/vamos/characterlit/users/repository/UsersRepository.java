package com.vamos.characterlit.users.repository;

import com.vamos.characterlit.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUserId(String userId);

    Users findByUserNumber(Long userNumber);

    boolean existsUserByNickname (String nickname);
}
