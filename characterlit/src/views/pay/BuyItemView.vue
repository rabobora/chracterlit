<template>
	<div class="buyitem-container">
		<div class="buyitem-section">
			<div class="buyitem-title">
				<h2 class="buyitem-title-name">결제하기</h2>
			</div>
			<div class="buyitem-info">
				<h3 class="buyitem-info-title">결제상품 정보</h3>
				<hr />
				<img :src="getDetail.thumbnail" class="buyitem-info-img">
				<p> 상품 이름 : {{ getDetail.title }}</p>
				<p> 결제 금액 : {{ getDetail.finalBid }}</p>
			</div>
			<div class="buyitem-point">
				<div class="buyitem-point-title">
					<h4 class="buyitem-point-name">포인트 사용</h4>
					<span class="buyitem-point-usable">사용가능 포인트 {{ payStore.userPoint.usablePoint }}</span>
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
				<button @click="buyItemButton" :disabled="!agree" class="buyitem-confirm-button">결제하기</button>
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
const router = useRouter();
const route = useRoute();

const detail = ref({})
const pointInput = ref(0);
const agree = ref(false);
const getDetail = computed(() => {
	return detail.value
})


// 전액사용
const useAllPoint = () => {
	if (payStore.userPoint.allPoint < getDetail.value.finalBid) {
		window.alert("포인트가 부족합니다.");
	} else {
		pointInput.value = getDetail.finalBid;
	}
}

// 결제하기
const buyItemButton = () => {

	console.log(pointInput.value)
	console.log(getDetail.value)
	if (getDetail.value.finalBid == pointInput.value) {

		const request = {
			bidId: getDetail.value.bidId,
			userNumber: getDetail.value.userNumber,
			finalBid: getDetail.value.finalBid,
			winnerNumber: getDetail.value.winnerNumber
		};

		payStore.buyItem(request);
	} else {
		window.alert("포인트를 올바르게 입력하세요.");
	}
}

onMounted(async () => {
	const bidId = route.params.number;
	const pd = await productStore.researchProductDetail(bidId);
	detail.value = pd
	await payStore.pointCheck();
});

</script>

<style>
.buyitem-container {
	display: flex;
	justify-content: center;
	align-items: center;
	padding: 20px;
}

.buyitem-title,
.buyitem-info,
.buyitem-point,
.buyitem-confirm {
	text-align: left;
	background-color: white;
	border-radius: 8px;
	box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
	padding: 20px;
	margin: 20px 0px;
	min-width: 100vh;
}

.buyitem-title {
	padding: 5px;
	text-align: center;
}

.buyitem-info-img {
	margin-right: 5px;
	max-width: 100px;
	max-height: 100px;
}

.buyitem-info-item {
	display: flex;
	align-items: center;
	justify-content: start;
	gap: 10px;
	margin: 0;
	line-height: 0.5;
}

.buyitem-info-text {
	display: flex;
	text-align: left;
	flex-direction: column;
	justify-content: center;
}

.buyitem-title-name,
.buyitem-info-title,
.buyitem-point-name {
	color: #333;
	margin-bottom: 15px;
}

hr {
	border-color: #eee;
	margin-bottom: 20px;
}

.buyitem-info-img {
	width: 100%;
	border-radius: 5px;
	margin-bottom: 20px;
}

.buyitem-point-title {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 10px;
}

.buyitem-point-usable {
	color: #000000;
	font-weight: 600;
}

.buyitem-point-input {
	display: flex;
	gap: 10px;
}

.buyitem-point-box {
	flex-grow: 1;
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.buyitem-point-button {
	background-color: #000000;
	color: white;
	border: none;
	padding: 10px 15px;
	border-radius: 4px;
	cursor: pointer;
}

.buyitem-confirm-white {
	padding: 15px;
	background-color: #fff;
	border: 1px solid #eee;
	border-radius: 4px;
	margin-bottom: 20px;
}

.buyitem-confirm-checkbox {
	margin-right: 10px;
}

.buyitem-confirm-button {
	width: 100%;
	padding: 10px 0;
	background-color: #000000;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	transition: background-color 0.3s;
}

.buyitem-confirm-button:disabled {
	background-color: #ccc;
	cursor: not-allowed;
}

.buyitem-confirm-button:hover:not(:disabled) {
	background-color: #000000;
}
</style>