<template>
  <div class="charge">
  <header>
    <TheHeader/>
  </header>
  <div class="charge-container">
    <div class="charge-section">
      <div class="charge-title">
        <h2 class="charge-title-name">충전하기</h2>
      </div>
      <div class="charge-info">
        <h3 class="charge-info-title">보유중인 포인트</h3>
        <h3 class="charge-info-point">{{ payStore.userPoint.usablePoint }}</h3>
      </div>
      <div class="charge-point">
        <span class="charge-point-info">충전 금액의 0.5%는 수수료로 부과됩니다.</span>
        <input type="number" min="1000" name="pointInput" id="pointInput" v-model="pointInput"
          placeholder="충전할 금액을 입력해주세요" class="charge-point-box" />
      </div>
      <div class="charge-payment">
        <div class="charge-payment-white">
          <h3 class="charge-payment-title">결제수단</h3>
          <hr />
          <div class="charge-payment-all">
            <div class="charge-payment-kakao">
              <input type="radio" id="kakaoPay" value="kakaoPay" v-model="paymentMethod" />
              <img src="@/assets/payment_icon_yellow_small.png" alt="logo" class="charge-kakaologo" />
            </div>
            <div class="charge-payment-account">
              <input type="radio" id="accountTransfer" value="accountTransfer" v-model="paymentMethod" />
              <label for="accountTransfer">계좌이체</label>
            </div>
          </div>
          <hr />
          <label class="charge-payment-confirm">
            <input type="checkbox" v-model="agree" @change="agreement" class="charge-confirm-checkbox">
            결제조건 확인 및 결제 진행 동의
          </label>
        </div>
        <ChargeInput v-if="isChargeModal" :pointInput="pointInput" />
        <button v-if="paymentMethod=='kakaoPay'" @click="chargeKakao" :disabled="!agree" class="charge-payment-button">충전하기</button>
        <button v-else="paymentMethod=='accountTransfer'" @click="chargeAccount" :disabled="!agree" class="charge-payment-button">충전하기</button>
      </div>
    </div>
  </div>
</div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { usePayStore } from '@/stores/pay';
import ChargeInput from '@/components/pay/ChargeInput.vue';
import { isChargeModal } from '@/stores/util';
import TheHeader from '@/components/common/TheHeader.vue';

const payStore = usePayStore();
const pointInput = ref(0);
const paymentMethod = ref('');

const agree = ref(false);

const chargeKakao = async() => {
  const request = {
      money: pointInput.value
    };
    await payStore.readyKakao(request);
}

const chargeAccount = () => {
  isChargeModal.value = true
}

onMounted(async () => {
  await payStore.pointCheck();
  isChargeModal.value = false;
});

</script>

<style>

.charge{
  background-color: #f5f5f5;
}

.charge-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px;
  
}

.charge-title {
  display: flex;
  justify-content: center;
  /* 수평 가운데 정렬 */
  align-items: center;
  /* 수직 가운데 정렬 */
}

.charge-info {
  height: 60px;
  display: flex;
  /* Flex 컨테이너로 지정 */
  justify-content: space-between;
  /* 내부 요소들을 양 끝으로 정렬 */
  align-items: center;
  /* 수직 가운데 정렬 */
  padding: 0 20px;
  /* 좌우 패딩을 추가하여 내부 요소들이 테두리에 바짝 붙지 않도록 함 */
}

.charge-point {
  height: 115px;
  display: flex;
  /* Flexbox 레이아웃 사용 */
  flex-direction: column;
  /* 자식 요소들을 세로 방향으로 배열 */
  padding: 20px;
  /* 자식 요소들을 수직 방향에서 중앙 정렬 */
  justify-content: center;
  /* 자식 요소들을 수직 방향에서 중앙 정렬 */
  align-items: center;
  /* 자식 요소들을 수평 방향에서 중앙 정렬 */
}

.charge-point-info {
  padding-left: 20px;
  align-self: flex-start;
  color: #666666;
}

.charge-point-box:focus {
  outline: none;
  /* 포커스 됐을 때의 외곽선 제거 */
}

.charge-point-box {
  width: calc(100% - 40px);
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

.charge-payment-title {
  padding-top: 20px;
}

.charge-payment-white {
  padding: 20px;
}

.charge-payment-kakao,
.charge-payment-account {
  margin-bottom: 20px;
  /* 하단 마진 추가로 간격 조정 */
}

.charge-kakaologo {
  width: 50px;
}


.charge-title,
.charge-info,
.charge-point,
.charge-payment-white {
  width: 870px;
  text-align: left;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
  min-width: 100vh;
  box-sizing: border-box;
}

.charge-title,
.charge-info,
.charge-point {
  margin: 20px 0px;
}

.charge-payment-confirm {
  font-size: 18px; /* h3 태그와 유사한 크기 */
  font-weight: bold; /* 굵은 글씨 */
}

.charge-confirm-checkbox {
  /* 체크박스 크기 조절 */
  width: 18px; /* 너비 */
  height: 18px; /* 높이 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
}


.charge-payment-button {
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

.charge-payment-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.charge-payment-button:hover {
    background-color: #0056b3;
    /* 호버 시 버튼 색상 변경 */
}
</style>
