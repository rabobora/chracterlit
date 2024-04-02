<template>
  <div class="charge-container">
    <div class="charge-section">
      <div class="charge-title">
        <h3 class="charge-title-name">충전하기</h3>
      </div>
      <div class="charge-info">
        <h4 class="charge-info-title">보유중인 포인트</h4>
        <h4 class="charge-info-point">{{ payStore.getUsablePoint }}</h4>
      </div>
      <div class="charge-point">
        <span class="charge-point-info">충전 금액의 0.5%는 수수료로 부과됩니다.</span>
        <input type="number" min="1000" name="pointInput" id="pointInput" v-model="pointInput"
          placeholder="충전할 금액을 입력해주세요" class="charge-point-box" />
      </div>
      <div class="charge-payment">
        <div class="charge-payment-white">
          <h4 class="charge-payment-title">결제수단</h4>
          <hr />
          <div class="charge-payment-kakao">
            <input type="radio" id="kakaoPay" value="kakaoPay" v-model="paymentMethod" />
            <label for="kakaoPay">카카오페이</label>
          </div>
          <div class="charge-payment-account">
            <input type="radio" id="accountTransfer" value="accountTransfer" v-model="paymentMethod" />
            <label for="accountTransfer">계좌이체</label>
          </div>
        </div>
        <div class="charge-payment-black">
          <ChargeInput v-if="isChargeModal" :pointInput="pointInput" @close="isChargeModal = false" />
          <button @click="chargePoint" class="charge-payment-button">충전하기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { usePayStore } from '@/stores/pay';
import { isChargeModal } from '@/stores/util';
import ChargeInput from '@/components/pay/ChargeInput.vue';

const payStore = usePayStore();
const pointInput = ref(0);
const paymentMethod = ref('');


const chargePoint = async () => {
  if (paymentMethod.value === 'kakaoPay') {


    // 요청 객체 생성
  const request = {
    money: pointInput.value
  };

    console.log(request);
    await payStore.readyKakao(request);
    // await payStore.approveKakao(order_id, pg_token);
  } else if (paymentMethod.value === 'accountTransfer') {
    isChargeModal.value = true;
  }
}

onMounted(() => {
  payStore.pointCheck();
});
</script>

<style></style>
