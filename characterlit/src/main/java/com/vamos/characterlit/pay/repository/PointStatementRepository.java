package com.vamos.characterlit.pay.repository;

import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.domain.PointStatements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointStatementRepository extends JpaRepository<PointStatements, Long> {

    List<PointStatements> findByuserNumber(Long userNumber);
}
