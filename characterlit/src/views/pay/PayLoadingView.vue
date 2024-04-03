<template>
  <div class="loading-main">
  <div class="droplet_spinner">
    <div class="droplet"></div>
    <div class="droplet"></div>
    <div class="droplet"></div>
  </div>
</div>
</template>

<script setup>
import { onMounted } from 'vue';
import { usePayStore } from '@/stores/pay';
import { useRoute } from 'vue-router';
const payStore = usePayStore();

const route = useRoute()

onMounted(() => {
    const order_id=route.query.order_id;
    const pg_token =route.query.pg_token;
    console.log(order_id);
    console.log(pg_token);
    payStore.approveKakao(order_id,pg_token);
});

</script>

<style>
.loading-main{
  width: 90vw;
  margin: 0 auto;
  text-align: center;
}

.droplet_spinner {
  display: flex;
  justify-content: center;
  margin: 30px;
}

.droplet_spinner .droplet {
  width: 15px;
  height: 15px;
  margin: 0 5px;
  
  background-color: #0056b3;
  border-radius: 50%;
  transform-origin: center bottom;
  
  animation: bounce 1.2s cubic-bezier(0.3, 0.01, 0.4, 1) infinite;
}

.droplet_spinner .droplet:nth-child(1) {
  animation-delay: -0.4s;
}

.droplet_spinner .droplet:nth-child(2) {
  animation-delay: -0.2s;
}

.droplet_spinner .droplet:nth-child(3) {
  animation-delay: 0s;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

</style>