package com.vamos.characterlit.pay.repository;

import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.domain.PointStatements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PointStatementRepository extends JpaRepository<PointStatements, Long> {

    List<PointStatements> findByuserNumber(Long userNumber);

    @Query("SELECT s FROM PointStatements s WHERE s.pointStatus =4 "+
            "AND s.statementDate =:date")
    List<PointStatements> findByStatementDate(LocalDate date);
}
