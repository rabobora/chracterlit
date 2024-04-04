package com.vamos.characterlit.pay.service;

import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import com.vamos.characterlit.pay.domain.Payment;
import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.domain.PointStatements;
import com.vamos.characterlit.pay.repository.PaymentRepository;
import com.vamos.characterlit.pay.repository.PointRepository;
import com.vamos.characterlit.pay.repository.PointStatementRepository;
import com.vamos.characterlit.pay.request.AccountTransferRequestDTO;
import com.vamos.characterlit.pay.request.BuyRequestDTO;
import com.vamos.characterlit.pay.request.ChargeRequestDTO;
import com.vamos.characterlit.pay.response.KakaoReadyResponseDTO;
import com.vamos.characterlit.users.domain.Users;
import com.vamos.characterlit.users.repository.UsersRepository;
import com.vamos.characterlit.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UsersRepository usersRepository;
    private final PointRepository pointRepository;
    private final PointStatementRepository pointStatementRepository;
    private final PaymentRepository paymentRepository;
    private final ItemRepository itemRepository;
    private final BankService bankService;
    private final PointStatementService pointStatementService;

    @Value("${spring.ssafy.pointAccount}")
    private String pointAccount;

    @Value("${spring.ssafy.pointKey}")
    private String pointKey;

    @Value("${spring.ssafy.feeAccount}")
    private String feeAccount;

    @Value("${spring.ssafy.accountKey}")
    private String accountKey;

    @Value("${spring.ssafy.mainAccount}")
    private String mainAccount;


    //포인트 조회
    public Point getPoint(Long userNumber) {

        Point point = pointRepository.findByuserNumber(userNumber);

        // point 테이블에 userNumber에 해당하는 레코드가 없을 경우
        if (point == null) {
            point = Point.builder()
                    .userNumber(userNumber)
                    .allPoint(0)
                    .usablePoint(0)
                    .build();
        }
        return point;
    }

    // 포인트 충전 (계좌이체)
    public void charge(ChargeRequestDTO request, Long userNumber) {

        String userkey = bankService.findBankUser(userNumber);
        AccountTransferRequestDTO chargeRequest = AccountTransferRequestDTO.builder()
                .depositBankCode("004")
                .depositAccountNo(mainAccount)
                .transactionBalance(request.getTransactionBalance())
                .withdrawalBankCode(request.getBankCode())
                .withdrawalAccountNo(request.getAccountNo())
                .depositTransactionSummary("Characterlit 포인트 충전")
                .withdrawalTransactionSummary("Characterlit 포인트 충전")
                .build();

        boolean success = bankService.accountTransfer(chargeRequest, userkey);

        int fee = (int)(request.getTransactionBalance()*0.005);

        AccountTransferRequestDTO divideFee = AccountTransferRequestDTO.builder()
                .depositBankCode("004")
                .depositAccountNo("0047672363735716")
                .transactionBalance(fee)
                .withdrawalBankCode("004")
                .withdrawalAccountNo(mainAccount)
                .depositTransactionSummary("Characterlit 수수료")
                .withdrawalTransactionSummary("Characterlit 수수료")
                .build();

        boolean feeSuccess = bankService.accountTransfer(divideFee, accountKey);

        int getpoint = request.getTransactionBalance()-fee;

        AccountTransferRequestDTO dividePoint = AccountTransferRequestDTO.builder()
                .depositBankCode("004")
                .depositAccountNo(pointAccount)
                .transactionBalance(getpoint)
                .withdrawalBankCode("004")
                .withdrawalAccountNo(mainAccount)
                .depositTransactionSummary("Characterlit 수수료 제외")
                .withdrawalTransactionSummary("Characterlit 수수료 제외")
                .build();

        boolean pointSuccess = bankService.accountTransfer(dividePoint, accountKey);

        if (success && feeSuccess && pointSuccess) {

            LocalDateTime now = LocalDateTime.now();
            String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
            String orderId = transmissionDate + pointStatementService.createOrderId();

            Payment payment = Payment.builder()
                    .paymentId(orderId)
                    .userNumber(userNumber)
                    .money(request.getTransactionBalance())
                    .paymentDate(LocalDateTime.now())
                    .paymentStatus(1)
                    .build();

            paymentRepository.save(payment);

            PointStatements pointStatements = PointStatements.builder()
                    .userNumber(userNumber)
                    .point(getpoint)
                    .statementDate(payment.getPaymentDate())
                    .pointStatus(1)
                    .build();

            pointStatementRepository.save(pointStatements);

            Point point = getPoint(userNumber);
            Point updatePoint = Point.builder()
                    .userNumber(userNumber)
                    .allPoint(point.getAllPoint()+ getpoint)
                    .usablePoint(point.getUsablePoint()+ getpoint)
                    .build();

            pointRepository.save(updatePoint);
        }
    }

    // 포인트 출금
    public void withdraw(ChargeRequestDTO request, Long userNumber){

        String userkey = bankService.findBankUser(userNumber);
        AccountTransferRequestDTO withdrawRequest = AccountTransferRequestDTO.builder()
                .depositBankCode(request.getBankCode())
                .depositAccountNo(request.getAccountNo())
                .transactionBalance(request.getTransactionBalance())
                .withdrawalBankCode("004")
                .withdrawalAccountNo(pointAccount)
                .depositTransactionSummary("Characterlit 포인트 출금")
                .withdrawalTransactionSummary("Characterlit 포인트 출금")
                .build();

        boolean success = bankService.accountTransfer(withdrawRequest, pointKey);

        if (success) {

            LocalDateTime now = LocalDateTime.now();
            String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
            String orderId = transmissionDate + pointStatementService.createOrderId();

            Payment payment = Payment.builder()
                    .paymentId(orderId)
                    .userNumber(userNumber)
                    .money(request.getTransactionBalance())
                    .paymentDate(LocalDateTime.now())
                    .paymentStatus(1)
                    .build();

            paymentRepository.save(payment);

            PointStatements pointStatements = PointStatements.builder()
                    .userNumber(userNumber)
                    .point(request.getTransactionBalance())
                    .statementDate(payment.getPaymentDate())
                    .pointStatus(3)
                    .build();

            pointStatementRepository.save(pointStatements);

            Point point = getPoint(userNumber);
            Point updatePoint = Point.builder()
                    .userNumber(userNumber)
                    .allPoint(point.getAllPoint()- payment.getMoney())
                    .usablePoint(point.getUsablePoint()- payment.getMoney())
                    .build();

            pointRepository.save(updatePoint);
        }
    }

    // 구매하기
public boolean buyItem(BuyRequestDTO request){

    Point seller = getPoint(request.getUserNumber());
    Point buyer =getPoint(request.getWinnerNumber());

    if(buyer.getUsablePoint()< request.getFinalBid()){
        return false;
    }

    LocalDateTime now = LocalDateTime.now();

    Point updateBuyer = Point.builder()
            .userNumber(request.getWinnerNumber())
            .allPoint(buyer.getAllPoint()-request.getFinalBid())
            .usablePoint(buyer.getUsablePoint()- request.getFinalBid())
            .build();
    pointRepository.save(updateBuyer);

    Point updateSeller = Point.builder()
            .userNumber(request.getUserNumber())
            .allPoint(seller.getAllPoint()+request.getFinalBid())
            .usablePoint(seller.getUsablePoint())
            .build();
    pointRepository.save(updateSeller);

    PointStatements buyerstate = PointStatements.builder()
            .userNumber(request.getWinnerNumber())
            .point(request.getFinalBid())
            .statementDate(now)
            .pointStatus(2)
            .bidId(request.getBidId())
            .build();

    PointStatements sellerstate = PointStatements.builder()
            .userNumber(request.getUserNumber())
            .point((int)(request.getFinalBid()))
            .statementDate(now)
            .pointStatus(4)
            .bidId(request.getBidId())
            .build();

    pointStatementRepository.save(buyerstate);
    pointStatementRepository.save(sellerstate);

    Items item = itemRepository.findByBidId(request.getBidId());
    item.setIsPaid(true);
    itemRepository.save(item);

    return true;
}
}
