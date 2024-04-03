<template>
  <div class="chargeinput-container">
      <div class="chargeinput-section">
          <select v-model="bank" class="chargeinput-bank">
          <option v-for="bank in bankList" :key="bank.code" :value="bank">
            {{ bank.name }}
          </option>
        </select>
        <input type="text" name="accountInput" id="accountInput" v-model="accountInput" placeholder="계좌번호를 입력해주세요"
          class="chargeinput-account" />
          <div class="chargeinput-money">
              <h4 class="chargeinput-money-name">결제금액</h4>
              <h4 class="chargeinput-money-money">{{ pointInput.value }} 원</h4>
          </div>
          <button @click="payMoney" class="chargeinput-button">결제하기</button>
         
      </div>
  </div>
</template>

<script setup>
import { ref,onMounted} from 'vue';
import { usePayStore } from '@/stores/pay';

const payStore = usePayStore();

defineProps({
  pointInput: Number
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

// 이체 정보 전송
const payMoney = () =>{
  if(!bank.value||!accountInput.value||!pointInput){
    window.alert("필요한 정보를 모두 입력해주세요");
  }

  // 요청 객체 생성
  const request = {
    bankCode: bank.value.code,
    accountNo: accountInput.value,
    transactionBalance: pointInput.value
  };
  try {
    payStore.chargeAccount(request)
  } catch (error) {
    window.alert("문제가 발생했습니다. 다시 시도해주세요.")
  }
};

onMounted(() => {
  payStore.pointCheck();
});

</script>

<style>

</style>