/
<template>
  <div class="statement-container">
    <div class="statement-title">
      <div class="statement-icon">
        <!-- <i class="fa-duotone fa-piggy-bank"></i> -->
        <h2 class="statement-name">포인트 이용내역</h2>
      </div>
      <div class="statement-list">
        <table>
          <thead>
            <tr class="statement-header">
              <th>날짜</th>
              <th>내역</th>
              <th>포인트</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="state in statements" :key="state.statementId">
              <td><span class="statement-date">{{ state.statementDate }}</span></td>
              <td><span class="statement-content">{{ convertPointStatus(state.pointStatus) }}</span></td>
              <td><span class="statement-point">{{ state.point }}</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
<script setup>
import { usePayStore } from '@/stores/pay';
import { onMounted, computed } from 'vue';

const payStore = usePayStore();

const statements = computed(() => payStore.getStatementList);

// 포인트 상태를 텍스트로 변환하는 함수
function convertPointStatus(status) {
  switch (status) {
    case 1:
      return '포인트 충전';
    case 2:
      return '상품 구매';
    case 3:
      return '포인트 출금';
    case 4:
      return '상품 판매';
    case 5:
      return '판매 금액 정산';
  }
}


onMounted(async () => {
  await payStore.statementRead();
});

</script>

<style>
.statement-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px;
}

.statement-header th {
  background-color: black;
  color: white;
}

.statement-list table {
  width: 800px;
  margin: 0 auto;
}


.statement-list table,
th,
td {
  border: 1px solid black;
  border-collapse: collapse;
}

th,
td {
  padding: 10px;
  text-align: center;
  /* 셀 내용을 가운데 정렬 */
}
</style>
