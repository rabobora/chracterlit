<template>
    <div class="withdraw">
        <header>
            <TheHeader />
        </header>
        <div class="withdraw-container">
            <div class="withdraw-section">
                <div class="withdraw-title">
                    <h2 class="withdraw-title-name">출금하기</h2>
                </div>
                <div class="withdraw-point-title">
                    <h3 class="withdraw-point-name">출금가능한 포인트</h3>
                    <h3 class="withdraw-point-usable">{{ payStore.userPoint.usablePoint }}</h3>
                </div>
                <div class="withdraw-point-input">
                    <input type="number" name="withdrawInput" id="withdrawInput" v-model="withdrawInput"
                        placeholder="출금할 포인트를 입력해주세요" class="withdraw-point-box" />
                </div>
                <div class="withdraw-bank">
                    <select v-model="bank" @change="bankSelect" class="withdraw-bank-code">
                        <option v-for="bank in bankList" :key="bank.code" :value="bank">
                            {{ bank.name }}
                        </option>
                    </select>
                    <input type="text" name="accountInput" id="accountInput" v-model="accountInput"
                        placeholder="계좌번호를 입력해주세요" class="withdraw-point-box" />
                </div>
            </div>
            <button @click="withdrawButton" class="withdraw-confirm-button">출금하기</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { usePayStore } from '@/stores/pay';
import TheHeader from '@/components/common/TheHeader.vue';


const payStore = usePayStore();

// 은행선택
const bank = ref(null);
const withdrawInput = ref(0);
const accountInput = ref(null);
const bankList = ref([
    { code: '002', name: '산업은행' },
    { code: '003', name: '기업은행' },
    { code: '004', name: '국민은행' },
    { code: '005', name: '기업은행' },
    { code: '006', name: '농협' },
    { code: '007', name: '신한은행' },
    { code: '008', name: '우리은행' },
    { code: '009', name: '케이뱅크' },
    { code: '010', name: '하나은행' },
    { code: '011', name: '카카오뱅크' },
    { code: '012', name: '토스뱅크' },
    { code: '013', name: '새마을금고' },
    { code: '014', name: '수협' },
    { code: '015', name: '신협' },
    { code: '016', name: '경남은행' },
    { code: '017', name: '광주은행' },
    { code: '018', name: '대구은행' },
    { code: '019', name: '부산은행' },
    { code: '020', name: '전북은행' },
    { code: '021', name: '제주은행' },
]);

// 이체 정보 전송
const withdrawButton = () => {
    if (!bank.value || !accountInput.value || !withdrawInput.value) {
        window.alert("필요한 정보를 모두 입력해주세요");
    }

    // 요청 객체 생성
    const request = {
        bankCode: bank.value.code,
        accountNo: accountInput.value,
        transactionBalance: withdrawInput.value
    };
    try {
        payStore.withdrawPoint(request)
    } catch (error) {
        window.alert("문제가 발생했습니다. 다시 시도해주세요.")
    }
};

onMounted(() => {
    payStore.pointCheck();
});

</script>

<style>
.withdraw {
    background-color: #f5f5f5;
}

.withdraw-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    box-sizing: border-box;
}

.withdraw-title,
.withdraw-point-title,
.withdraw-point-input,
.withdraw-bank {
    width: 870px;
    text-align: left;
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
    min-width: 100vh;
    box-sizing: border-box;
    margin-bottom: 20px;
}

.withdraw-title {
    display: flex;
    justify-content: center;
    align-items: center;
}

.withdraw-point-title {
    display: flex;
    /* Flex 컨테이너로 지정 */
    justify-content: space-between;
    /* 내부 요소들을 양 끝으로 정렬 */
    align-items: center;
    /* 수직 가운데 정렬 */
    padding: 0 20px;
    /* 좌우 패딩을 추가하여 내부 요소들이 테두리에 바짝 붙지 않도록 함 */
}

.withdraw-point-input {
    display: flex;
    /* Flexbox 레이아웃 사용 */
    flex-direction: column;
    /* 자식 요소들을 세로 방향으로 배열 */
    padding-bottom: 30px;
    /* 자식 요소들을 수직 방향에서 중앙 정렬 */
    justify-content: center;
    /* 자식 요소들을 수직 방향에서 중앙 정렬 */
    align-items: center;
    /* 자식 요소들을 수평 방향에서 중앙 정렬 */
    padding-left: 30px;
    padding-right: 30px;
}

.withdraw-point-box:focus {
    outline: none;
    /* 포커스 됐을 때의 외곽선 제거 */
}

.withdraw-point-box {
    width: 100%;
    border: 0;
    /* 모든 테두리 제거 */
    border-bottom: 1px solid black;
    /* 아래쪽 테두리만 추가 */
    margin-top: 20px;
    /* 입력 상자와 위의 텍스트 사이에 마진 추가 */
    font-size: 24px;
    /* h2 태그의 평균 글씨 크기로 설정, 프로젝트에 따라 조정 필요 */
    text-align: left;
}


.withdraw-point-input,
.withdraw-bank {
    margin-bottom: 35px;
}

.withdraw-bank{
    padding: 30px;
    
}

 .withdraw-bank-code {
    width: 100%;
    display: block;
    padding: 10px;
} 

.withdraw-confirm-button {
    width: 870px;
  background-color: #000000;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size:18px;
  font-weight: bold;
  padding: 15px;
}

.withdraw-confirm-button:hover {
    background-color: #0056b3;
    /* 호버 시 버튼 색상 변경 */
}
</style>