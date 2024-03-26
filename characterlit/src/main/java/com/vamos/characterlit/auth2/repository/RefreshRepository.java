package com.vamos.characterlit.auth2.repository;

import com.vamos.characterlit.auth2.domain.Refresh;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    @Transactional
    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteAllByUserId(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}