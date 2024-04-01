package com.vamos.characterlit.pay.repository;

import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.domain.PointStatements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PointStatementRepository extends JpaRepository<PointStatements, Long> {

    List<PointStatements> findByUserNumber(Long userNumber);

    @Query("SELECT s FROM PointStatements s WHERE s.pointStatus = 4 " +
            "AND s.statementDate = :date " +
            "ORDER BY s.statementDate DESC")
    List<PointStatements> findByStatementDate(LocalDate date);

    PointStatements findByUserNumberAndBidId(Long userNumber, Long bidId);
}
