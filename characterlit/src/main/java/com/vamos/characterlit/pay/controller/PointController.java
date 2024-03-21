package com.vamos.characterlit.pay.controller;


import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.request.AccountTransferRequestDTO;
import com.vamos.characterlit.pay.response.PointResponseDTO;
import com.vamos.characterlit.pay.service.BankService;
import com.vamos.characterlit.pay.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {

    private final PointService pointService;
    private final BankService bankService;

    // 포인트 조회
    @GetMapping
    public ResponseEntity<PointResponseDTO> pointCheck(Long userId){

        Point point = pointService.getPoint(userId);

        return new ResponseEntity<>(
                PointResponseDTO.builder()
                        .allPoint(point.getAllPoint())
                        .usablePoint(point.getUsablePoint())
                        .build()
                , HttpStatus.OK);
    }

    // 사용가능한 포인트 조회
    @GetMapping("/usable")
    public ResponseEntity<Integer> usablePointCheck(@RequestParam Long userId){
        Point point = pointService.getPoint(userId);
        return new ResponseEntity<>(point.getUsablePoint(),HttpStatus.OK);
    }

}