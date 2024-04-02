/
<template>
  <div class="statement-container">
    <div class="statement-title">
      <div class="statement-icon">
        <font-awesome-icon icon="piggy-bank" />
        <h3 class="statement-name">포인트 이용내역</h3>
      </div>
      <div class="statement-list">
        <table>
          <thead>
            <tr>
              <th>날짜</th>
              <th>내역</th>
              <th>포인트</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="state in statements" :key="state.statementId">
              <td><span class="statement-date">{{ state.statementDate }}</span></td>
              <td><span class="statement-content">{{ state.pointStatus }}</span></td>
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
import { onMounted,computed } from 'vue';

const payStore = usePayStore();

const statements = payStore.getStatementList;


onMounted(() => {
  payStore.statementRead();
});

</script>

<style></style>
