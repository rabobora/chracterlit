package com.vamos.characterlit.pay.service;

import com.vamos.characterlit.pay.domain.PointStatements;
import com.vamos.characterlit.pay.repository.PointStatementRepository;
import com.vamos.characterlit.pay.response.StatementResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class PointStatementService {

    private final PointStatementRepository pointStatementRepository;

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
}

