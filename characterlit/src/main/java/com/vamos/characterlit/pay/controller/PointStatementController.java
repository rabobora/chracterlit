package com.vamos.characterlit.pay.controller;

import com.vamos.characterlit.pay.response.StatementResponseDTO;
import com.vamos.characterlit.pay.service.PointStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointStatementController {

    private PointStatementService pointStatementService;

    // 포인트 내역 조회
    @GetMapping("/statement")
    public ResponseEntity<List<StatementResponseDTO>> statementRead(Long userId){
        return new ResponseEntity<List<StatementResponseDTO>>(pointStatementService.pointStatementsList(userId), HttpStatus.OK);
    }
}
