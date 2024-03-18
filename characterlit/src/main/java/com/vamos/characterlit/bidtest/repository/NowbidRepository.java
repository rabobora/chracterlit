package com.vamos.characterlit.bidtest.repository;

import com.vamos.characterlit.bidtest.domain.Nowbid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NowbidRepository extends JpaRepository<Nowbid, Long> {

}
