package com.vamos.characterlit.pay.controller;

import com.vamos.characterlit.auth2.annotation.ExtractPayload;
import com.vamos.characterlit.pay.response.StatementResponseDTO;
import com.vamos.characterlit.pay.service.PointStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointStatementController {

    private final PointStatementService pointStatementService;

    // 포인트 내역 조회
    @GetMapping("/statement")
    public ResponseEntity<List<StatementResponseDTO>> statementRead(@ExtractPayload Long userNumber){
        return new ResponseEntity<List<StatementResponseDTO>>(pointStatementService.pointStatementsList(userNumber), HttpStatus.OK);
    }

    // 구매확정
    @PutMapping("/buy/complete")
    public ResponseEntity<Void> buyComplete(@ExtractPayload Long bidId){
        pointStatementService.pointConfirm(bidId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
