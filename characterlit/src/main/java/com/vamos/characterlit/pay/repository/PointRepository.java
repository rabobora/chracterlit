package com.vamos.characterlit.pay.repository;

import com.vamos.characterlit.pay.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

    Point findByuserNumber(Long userNumber);
}
