<template>
  <div class="chargeinput-container">

    <div class="chargeinput-section">
      <div class="chargeinput-close" @click="closeChargeModal">
        <i class="fa-solid fa-xmark fa-2xl" style="color: #ffffff;"></i>
      </div>
      <div class="chargeinput-content">
        <select v-model="bank" class="chargeinput-bank">
          <option v-for="bank in bankList" :key="bank.code" :value="bank">
            {{ bank.name }}
          </option>
        </select>
        <input type="text" name="accountInput" id="accountInput" v-model="accountInput" placeholder="계좌번호를 입력해주세요"
          class="chargeinput-account" />
        <div class="chargeinput-money">
          <h4 class="chargeinput-money-name">결제금액</h4>
          <h4 class="chargeinput-money-money">{{ pointInput }} 원</h4>
        </div>
        <button @click="payMoney" class="chargeinput-button">결제하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { usePayStore } from '@/stores/pay';
import { isChargeModal } from '@/stores/util';

const payStore = usePayStore();

defineProps({
  pointInput: Number,
});

// 은행선택
const bank = ref(null);
const accountInput = ref('');
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

const closeChargeModal = () => {
  isChargeModal.value = false
}

// 이체 정보 전송
const payMoney = () => {
  if (!bank.value || !accountInput.value || !pointInput) {
    window.alert('필요한 정보를 모두 입력해주세요');
  }

  // 요청 객체 생성
  const request = {
    bankCode: bank.value.code,
    accountNo: accountInput.value,
    transactionBalance: pointInput.value,
  };
  try {
    payStore.chargeAccount(request);
  } catch (error) {
    window.alert('문제가 발생했습니다. 다시 시도해주세요.');
  }
};



onMounted(() => {
  payStore.pointCheck();
});
</script>

<style>
.chargeinput-container {
  position: fixed;
  width: 100vw;
  height: 100vh;
  /* max-width:614px;
  max-height: 584px; */
  background-color: rgba(128, 128, 128, 0.863) !important;
  top: 0;
  left: 0;
  margin: 0;
  z-index: 99 !important;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chargeinput-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 500px;
  margin: auto;
  margin-top: 40px;
  background: #FFF;
  padding: 0px;
  border-radius: 8px;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
}

.chargeinput-close {
  background-color: black;
  width: 500px;
  box-sizing: border-box;
  padding: 20px;
  display: flex; /* flexbox 레이아웃 적용 */
  justify-content: flex-end; /* 자식 요소들을 오른쪽으로 정렬 */
}

.chargeinput-content{
  padding:40px;
}

.chargeinput-bank,
.chargeinput-account,
.chargeinput-button {
  width: calc(100% - 40px);
  margin: 15px 20px;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

.chargeinput-bank:focus,
.chargeinput-account:focus {
  border-color: #666;
  outline: none;
}

.chargeinput-money {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 2px solid black;
  /* 검정색 테두리 추가 */
  border-radius: 8px;
  /* 테두리 둥글게 */
  padding: 10px 20px;
  /* 안쪽 여백 추가 */
  margin: 20px 20px;
  /* 간격 조정 */
  width: calc(100% - 40px);
  box-sizing: border-box;
}

.chargeinput-money-name,
.chargeinput-money-money {
  margin: 0;
  padding: 0;
}

.chargeinput-button {
  background-color: #000000;
  color: white;
  cursor: pointer;
  font-size: 16px;
  border: none;
  transition: background-color 0.3s;
}

.chargeinput-button:hover {
  background-color: #55498c;
}

.chargeinput-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

/* Placeholder styling */
.chargeinput-account::placeholder {
  color: #aaa;
}

.chargeinput-button:hover {
    background-color: #0056b3;
    /* 호버 시 버튼 색상 변경 */
}
</style>