<template>
    <div class="payloading-container">
        <div class="payloading-section">
            <div class="payloading-content">
                <p>결제 승인 요청 중입니다. . .</p>
            </div>
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

</style>