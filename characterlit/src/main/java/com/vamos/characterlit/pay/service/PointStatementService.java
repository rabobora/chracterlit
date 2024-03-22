package com.vamos.characterlit.pay.service;

import com.vamos.characterlit.pay.domain.PointStatements;
import com.vamos.characterlit.pay.repository.PointStatementRepository;
import com.vamos.characterlit.pay.response.StatementResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PointStatementService {

    private final PointStatementRepository pointStatementRepository;

    // 포인트 내역 조회
    public List<StatementResponseDTO> pointStatementsList(Long userId){

        List<PointStatements> statements = pointStatementRepository.findByUserId(userId);
        List<StatementResponseDTO> result = new ArrayList<>();
        for (PointStatements statement : statements){
            result.add(StatementResponseDTO.builder()
                    .statementId(statement.getStatementId())
                    .point(statement.getPoint())
                    .statementDate(statement.getStatementDate())
                    .pointStatus(statement.getPointStatus())
                    .build());
        }
        return result;
    }
}
