<template>
<div class="buyitem-container">
        <div class="buyitem-section">
            <div class="buyitem-title">
                <h3 class="buyitem-title-name">결제하기</h3>
            </div>
            <div class="buyitem-info">
                <h4 class="buyitem-info-title">결제상품 정보</h4>
                <hr />
                <img :src="getDetail.thumbnail" class="buyitem-info-img">
            </div>
            <div class="buyitem-point">
                <div class="buyitem-point-title">
                    <h4 class="buyitem-point-name">포인트 사용</h4>
                    <span class="buyitem-point-usable">사용가능 포인트 {{ payStore.getUsablePoint }}</span>
                </div>
                <div class="buyitem-point-input">
                    <input type="number" name="pointInput" id="pointInput" v-model="pointInput"
                        placeholder="사용할 포인트를 입력해주세요" class="buyitem-point-box" />
                    <button @click="useAllPoint" class="buyitem-point-button">전액사용</button>
                </div>
            </div>
            <div class="buyitem-confirm">
                <div class="buyitem-confirm-white">
                    <label class="buyitem-confirm-title">
                        <input type="checkbox" v-model="agree" @change="agreement" class="buyitem-confirm-checkbox">
                        구매조건 확인 및 결제 진행 동의
                    </label>
                </div>
                <button @click="buyItemButton" :disabled="agree" class="buyitem-confirm-button">결제하기</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue';
import { usePayStore } from '@/stores/pay';
import { useProductStore } from '@/stores/product';
import { useRouter, useRoute } from 'vue-router';


const payStore = usePayStore();
const productStore = useProductStore();
const detail = ref({})
const pointInput = ref(0);
const agree = ref(false);
const getDetail = computed(() => {
        return detail.value
    })
const router = useRouter();
const route = useRoute()


// 전액사용
const useAllPoint = () => {
    const price = productStore.productDetail.getProductDetail.price;
    if (payStore.getUsablePoint < price.value) {
        window.alert("포인트가 부족합니다.");
    } else {
        pointInput.value = price;
    }
}

// 결제하기
const buyItemButton = async () => {
    if (price == pointInput.value) {
        await payStore.buyItem();
        window.alert("결제가 완료되었습니다.");
        router.push("/mypage");
    } else {
        window.alert("포인트를 올바르게 입력하세요.");
    }
}

onMounted(async () => {
    const bidId = route.params.number;
    const pd = await productStore.researchProductDetail(bidId);
    detail.value = pd
    console.log(bidId);  
    await payStore.pointCheck();
});

</script>

<style></style>