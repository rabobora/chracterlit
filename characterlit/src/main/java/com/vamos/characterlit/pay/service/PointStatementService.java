package com.vamos.characterlit.pay.service;

import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.domain.PointStatements;
import com.vamos.characterlit.pay.repository.PointRepository;
import com.vamos.characterlit.pay.repository.PointStatementRepository;
import com.vamos.characterlit.pay.request.AccountTransferRequestDTO;
import com.vamos.characterlit.pay.response.StatementResponseDTO;
import com.vamos.characterlit.user.domain.Users;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PointStatementService {

    private final PointStatementRepository pointStatementRepository;
    private final PointRepository pointRepository;
    private final PointStatementService pointStatementService;
    private final ItemRepository itemRepository;
    private final BankService bankService;

    @Value("${spring.ssafy.pointAccount}")
    private String pointAccount;

    @Value("${spring.ssafy.pointKey}")
    private String pointKey;

    @Value("${spring.ssafy.feeAccount}")
    private String feeAccount;

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

    // 포인트 정산하기 ( D+8 )
    @Scheduled(cron = "0 0 0 * * *")
    public void pointHolding(){
        LocalDate localDate = LocalDate.now().minusDays(8);
        List<PointStatements> pointList = pointStatementRepository.findByStatementDate(localDate);

        for(PointStatements state : pointList){
            state.setPointStatus(5);
            Point point = pointRepository.findByuserNumber(state.getUserNumber());
            int usablePoint = point.getUsablePoint();
            int plus = state.getPoint();
            point.setUsablePoint(usablePoint+plus);
        }
    }

    // 포인트 정산하기 ( 구매확정 )
    public void pointConfirm(Long bidId){

        Items item = itemRepository.findByBidId(bidId);
        PointStatements statements = pointStatementRepository.findByUserNumberAndBidId(item.getUserId(), item.getBidId());
        statements.setPointStatus(5);
        Point point = pointRepository.findByuserNumber(item.getUserId());
        int usablePoint = point.getUsablePoint();
        int plus = statements.getPoint();
        point.setUsablePoint(usablePoint+plus);
        balanceFee(item.getFinalBid()-plus);
    }

    // 수수료
    public void balanceFee(int fee){

        AccountTransferRequestDTO transfer = AccountTransferRequestDTO.builder()
                .depositBankCode("004")
                .depositAccountNo(feeAccount)
                .transactionBalance(fee)
                .withdrawalBankCode("004")
                .withdrawalAccountNo(pointAccount)
                .depositTransactionSummary("수수료")
                .withdrawalTransactionSummary("수수료")
                .build();
        boolean check = bankService.accountTransfer(transfer,pointKey);

    }
}

