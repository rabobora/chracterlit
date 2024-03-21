package com.vamos.characterlit.pay.service;

import com.google.gson.*;
import com.vamos.characterlit.pay.request.AccountTransferRequestDTO;
import com.vamos.characterlit.pay.response.AccountHistoryResponseDTO;
import com.vamos.characterlit.pay.response.AccountInfoResponseDTO;
import com.vamos.characterlit.pay.response.AccountTransferResponseDTO;
import com.vamos.characterlit.pay.response.FindUserkeyResponseDTO;
import com.vamos.characterlit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BankService {

    private final UserRepository userRepository;

    @Value("${spring.ssafy.apiKey}")
    private String apiKey;

    @Value("${spring.ssafy.registUserUrl}")
    private String registUserUrl;

    @Value("${spring.ssafy.findUserUrl}")
    private String findUserUrl;

    @Value("${spring.ssafy.accountInfoUrl}")
    private String accountInfoUrl;

    @Value("${spring.ssafy.accountTransferUrl}")
    private String accountTransferUrl;

    @Value("${spring.ssafy.accountHistoryUrl}")
    private String accountHistoryUrl;


    // SSAFY 금융망 사용자 등록 ( 상민오빠 회원가입 과정에 추가해줘... )
    public String registBankUser(String email) {

        if (findBankUser(email) == null) {
            System.out.println("2번, findBankUser : " + findBankUser(email));
        }
            Map<String, Object> request = new HashMap<>();
            request.put("apiKey", apiKey);
            request.put("userId", email);
            System.out.println("사용자등록 request : " + request);

        try {

            WebClient wc = WebClient.create(registUserUrl);
            String response = wc.post()
                    .uri(registUserUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("3번, response : " + response);

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            Gson gson = new Gson();
            JsonObject payloadObject = jsonObject.getAsJsonObject("payload");
            FindUserkeyResponseDTO findUserkey = null;

            System.out.println("4번, finduserkey : " + findUserkey);

            findUserkey = gson.fromJson(payloadObject, FindUserkeyResponseDTO.class);
            System.out.println("5번, finduserkey : " + findUserkey);
            System.out.println("6번, Userkey : " + findUserkey.getUserKey());

            return findUserkey.getUserKey();
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("1번오류");
            return null;
        } catch (JsonParseException e) {
            e.printStackTrace();
            System.out.println("2번오류");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("3번오류");
            return null;
        }
    }

    // SSAFY 금융망 사용자 조회
    public String findBankUser(String email) {

        Map<String, Object> request = new HashMap<>();
        request.put("userId", email);
        request.put("apiKey", apiKey);

        System.out.println("2-1. request : " + request);

        try {
            WebClient wc = WebClient.create(findUserUrl);
            System.out.println("2-2. webclient : " + wc);

            String response = wc.post()
                    .uri(findUserUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("2-2. response : " + response);

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            Gson gson = new Gson();
            JsonObject payloadObject = jsonObject.getAsJsonObject("payload");

            FindUserkeyResponseDTO findUserkey = null;
            System.out.println("2-3. finduserkey : " + findUserkey);

            findUserkey = gson.fromJson(payloadObject, FindUserkeyResponseDTO.class);
            System.out.println("2-4. findUserkey : " + findUserkey);

            System.out.println("2-5. userkey : " + findUserkey.getUserKey());
            return findUserkey.getUserKey();
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("1번오류");
            return null;
        } catch (JsonParseException e) {
            e.printStackTrace();
            System.out.println("2번오류");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("3번오류");
            return null;
        }
    }
    // SSAFY 금융망 계좌 조회
    public boolean accountCheck(String bankCode, String account, String userkey){

        LocalDateTime now = LocalDateTime.now();
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        int randomNumber = (int) (Math.random() * 900000) + 100000;

        Map<String,Object> Header = new HashMap<>();
        Header.put("apiName", "inquireAccountInfo");
        Header.put("transmissionDate", transmissionDate);
        Header.put("transmissionTime", transmissionTime);
        Header.put("institutionCode", "00100");
        Header.put("fintechAppNo", "001");
        Header.put("apiServiceCode", "inquireAccountInfo");
        Header.put("institutionTransactionUniqueNo", transmissionDate+transmissionTime+randomNumber);
        Header.put("apiKey", apiKey);
        Header.put("userKey", userkey);

        Map<String, Object> request = new HashMap<>();
        request.put("Header", Header);
        request.put("bankCode", bankCode);
        request.put("accountNo",account);

        try {
            WebClient wc = WebClient.create(accountInfoUrl );

            String response = wc.post()
                    .uri(accountInfoUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            Gson gson = new Gson();
            JsonObject Object = jsonObject.getAsJsonObject("REC");

            AccountInfoResponseDTO accountInfo= null;
            accountInfo = gson.fromJson(Object, AccountInfoResponseDTO.class);

            if(account.equals(accountInfo.getAccountNo())){
            return true;
            }else{
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        } catch (JsonParseException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // SSAFY 금융망 계좌이체
    public boolean accountTransfer(AccountTransferRequestDTO accountrequest, String userkey){

        LocalDateTime now = LocalDateTime.now();
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        int randomNumber = (int) (Math.random() * 900000) + 100000;

        Map<String,Object> Header = new HashMap<>();
        Header.put("apiName", "accountTransfer");
        Header.put("transmissionDate", transmissionDate);
        Header.put("transmissionTime", transmissionTime);
        Header.put("institutionCode", "00100");
        Header.put("fintechAppNo", "001");
        Header.put("apiServiceCode", "accountTransfer");
        Header.put("institutionTransactionUniqueNo", transmissionDate+transmissionTime+randomNumber);
        Header.put("apiKey", apiKey);
        Header.put("userKey", userkey);

        Map<String, Object> request = new HashMap<>();
        request.put("Header", Header);
        request.put("depositBankCode", accountrequest.getDepositBankCode());
        request.put("depositAccountNo",accountrequest.getDepositAccountNo());
        request.put("transactionBalance",accountrequest.getTransactionBalance());
        request.put("withdrawalBankCode",accountrequest.getWithdrawalBankCode());
        request.put("withdrawalAccountNo",accountrequest.getWithdrawalAccountNo());
        request.put("depositTransactionSummary",accountrequest.getDepositTransactionSummary()); // 거래내용요약(입금)
        request.put("withdrawalTransactionSummary",accountrequest.getWithdrawalTransactionSummary()); // 거래내용요약(출금)

        try {
            WebClient wc = WebClient.create(accountTransferUrl);

            System.out.println("wc : "+wc);

            String response = wc.post()
                    .uri(accountTransferUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("response : "+response);

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            System.out.println("jsonObject : "+jsonObject);

            Gson gson = new Gson();
            JsonArray array = jsonObject.getAsJsonArray("REC");

            List<AccountTransferResponseDTO> accounts = new ArrayList<>();
            for (JsonElement element : array) {
                AccountTransferResponseDTO account = gson.fromJson(element, AccountTransferResponseDTO.class);
                accounts.add(account);
            }

            // 서로의 계좌 거래번호로 거래 잘되었는지 확인
            // => 입출금계좌 크로스쳌
            // 각각의 거래내역 단건조회로 잘 되었는지 확인

            if(accounts.get(0).getAccountNo().equals(accounts.get(1).getTransactionAccountNo())){
                return true;
            }else{
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        } catch (JsonParseException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // SSAFY 금융망 계좌거래내역 조회 (단건)
    public AccountHistoryResponseDTO accoutHistory(String bankCode, String aacountNo, int transactionUniqueNo, String userkey){
        LocalDateTime now = LocalDateTime.now();
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        int randomNumber = (int) (Math.random() * 900000) + 100000;

        Map<String,Object> Header = new HashMap<>();
        Header.put("apiName", "inquireTransactionHistoryDetail");
        Header.put("transmissionDate", transmissionDate);
        Header.put("transmissionTime", transmissionTime);
        Header.put("institutionCode", "00100");
        Header.put("fintechAppNo", "001");
        Header.put("apiServiceCode", "inquireTransactionHistoryDetail");
        Header.put("institutionTransactionUniqueNo", transmissionDate+transmissionTime+randomNumber);
        Header.put("apiKey", apiKey);
        Header.put("userKey", userkey);

        Map<String, Object> request = new HashMap<>();
        request.put("Header", Header);
        request.put("bankCode", bankCode);
        request.put("accountNo",aacountNo);
        request.put("transactionUniqueNo",transactionUniqueNo);

        try {
            WebClient wc = WebClient.create(accountHistoryUrl);

            String response = wc.post()
                    .uri(accountHistoryUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            Gson gson = new Gson();
            JsonObject Object = jsonObject.getAsJsonObject("REC");

            AccountHistoryResponseDTO histroy= null;
            histroy = gson.fromJson(Object, AccountHistoryResponseDTO.class);

            return histroy;

        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



}
