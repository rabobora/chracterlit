package com.vamos.characterlit.pay.service;

import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.repository.PointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    //포인트 조회
    public Point getPoint(Long userId) {

        Point point = pointRepository.findByUserId(userId);

        // point 테이블에 userId에 해당하는 레코드가 없을 경우
        if (point == null) {
            point = Point.builder()
                    .userId(userId)
                    .allPoint(0)
                    .usablePoint(0)
                    .build();
        }
        return point;
    }
}
