<template>
    <div class="withdraw-container">
        <div class="withdraw-section">
            <div class="withdraw-title">
                <h3 class="withdraw-title-name">출금하기</h3>
            </div>
            <div class="withdraw-point-title">
                <h4 class="withdraw-point-name">포인트 사용</h4>
                <span class="withdraw-point-usable">{{ payStore.userPoint.usablePoint }}</span>
            </div>
            <div class="withdraw-point-input">
                <input type="number" name="withdrawInput" id="withdrawInput" v-model="withdrawInput"
                    placeholder="출금할 포인트를 입력해주세요" class="withdraw-point-box" />
            </div>
            <div class="withdraw-bank">
                <select v-model="bank" @change="bankSelect" class="buyitem-bank-code">
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
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { usePayStore } from '@/stores/pay';


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
.withdraw-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20px;
	background-color: #f5f5f7; /* 배경 색상 설정 */
	min-height: 100vh; /* 최소 높이 설정 */
}

.withdraw-section {
	background-color: white;
	border-radius: 8px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
	padding: 25px;
	width: 100%;
	max-width: 400px; /* 최대 너비 설정 */
	margin-bottom: 20px; /* 하단 버튼과의 간격 */
}

.withdraw-title-name,
.withdraw-point-name {
	color: #333;
	margin-bottom: 15px;
}

.withdraw-point-usable {
	color: #28a745; /* 사용 가능 포인트 색상 */
	font-weight: 600;
	display: block; /* 개행 효과 */
	margin-top: 5px;
}

.withdraw-point-input,
.withdraw-bank {
	margin-bottom: 20px;
}

.withdraw-point-box,
.buyitem-bank-code {
	width: 100%;
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-bottom: 10px; /* 입력 필드 간 간격 */
}

.buyitem-bank-code {
	display: block; /* 새 줄에서 시작 */
}

.withdraw-confirm-button {
	width: 100%;
	max-width: 400px; /* 버튼 최대 너비 설정 */
	padding: 10px 0;
	background-color: #007bff; /* 버튼 색상 */
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	transition: background-color 0.3s;
}

.withdraw-confirm-button:hover {
	background-color: #0056b3; /* 호버 시 버튼 색상 변경 */
}
</style>