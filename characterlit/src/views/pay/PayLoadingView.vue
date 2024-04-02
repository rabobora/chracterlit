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

onMounted(() => {
    
    payStore.approveKakao(route.params.order_id,route.params.pg_token);
});

</script>

<style>

</style>