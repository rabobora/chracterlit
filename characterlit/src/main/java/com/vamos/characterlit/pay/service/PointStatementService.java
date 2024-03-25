package com.vamos.characterlit.pay.service;

import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.domain.PointStatements;
import com.vamos.characterlit.pay.repository.PointRepository;
import com.vamos.characterlit.pay.repository.PointStatementRepository;
import com.vamos.characterlit.pay.response.StatementResponseDTO;
import com.vamos.characterlit.user.domain.Users;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
@AllArgsConstructor
public class PointStatementService {

    private final PointStatementRepository pointStatementRepository;
    private final PointRepository pointRepository;
    private final PointStatementService pointStatementService;

    // 포인트 내역 조회
    public List<StatementResponseDTO> pointStatementsList(Long userNumber) {

        List<PointStatements> statements = pointStatementRepository.findByuserNumber(userNumber);
        List<StatementResponseDTO> result = new ArrayList<>();
        for (PointStatements statement : statements) {
            result.add(StatementResponseDTO.builder()
                    .statementId(statement.getStatementId())
                    .point(statement.getPoint())
                    .statementDate(statement.getStatementDate())
                    .pointStatus(statement.getPointStatus())
                    .build());
        }
        return result;
    }

    // 포인트 주문번호 생성
    public String createOrderId() {
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();

        for (int i = 0; i < 10; i++) {

            if (rd.nextBoolean()) {
                sb.append(rd.nextInt(10));
            } else {
                sb.append((char) (rd.nextInt(26) + 65));
            }
        }
        return sb.toString();
    }

    // 포인트 홀딩하기
    @Scheduled(cron = "0 0 0 * * *")
    public void pointHolding(){
        LocalDate localDate = LocalDate.now().minusDays(8);
        List<PointStatements> pointList = pointStatementRepository.findByStatementDate(localDate);

        for(PointStatements state : pointList){
            state.setPointStatus(5);
            Point point = pointRepository.findByuserNumber(state.getUserNumber());
            int usablePoint = point.getUsablePoint();
            point.setUsablePoint(usablePoint+state.getPoint());
        }
    }
}

