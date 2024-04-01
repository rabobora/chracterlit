<template>
	<header>
		<TheHeader />
	</header>
	<div class="searchandword">
		<div class="fadecomponents">
			<div class="catchpri">
				<h1>세상에 있는 모든 굿즈,<br />캐릭터릿</h1>
			</div>
			<div class="searchbar">
				<SearchBarView />
			</div>
			<div class="gotoitemlist">
				<p @click="gotoproductlist" class="productlist">시작하기</p>
			</div>
		</div>
	</div>
	<!-- <div>다음페이지</div>     -->
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useProductStore } from '../../stores/product';
import SearchBarView from '../product/SearchBarView.vue';

import { useRoute, useRouter } from 'vue-router';
import TheHeader from '@/components/common/TheHeader.vue';
const router = useRouter();
const route = useRoute();
const productStore = useProductStore();

const gotoproductlist = () => {
	router.push({ name: 'productList' });
};
</script>

<style scoped>
.fadecomponents {
}

@keyframes fade-in {
	from {
		opacity: 0; /* 시작 시 요소가 완전히 투명 */
	}
	to {
		opacity: 1; /* 종료 시 요소가 완전히 불투명 */
	}
}

.fadecomponents > div {
	animation: fade-in 2.5s ease-out 0.5s; /* 애니메이션 이름, 지속 시간, 타이밍 함수, 시작 지연 */
	animation-fill-mode: forwards; /* 애니메이션 종료 시 요소가 최종 상태를 유지하도록 설정 */
}

.productlist {
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 10px 20px;
	background-color: black;
	border: 2px solid #dcdcdc;
	border-radius: 5px;
	cursor: pointer;
	text-decoration: none;
	color: white;
	transition: background-color 0.3s, color 0.3s;
	height: 50px;
	width: 100px;
	font-size: x-large;
	font-weight: bold;
	margin-left: 38%;
}

.productlist:hover {
	background-color: #e2dddd;
	color: #3e78e5;
}

.searchandword {
	background-image: url('../../pictures/깃털.png');
	background-size: auto;
	background-repeat: no-repeat;
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-items: center;
	height: 100vh;
	padding-top: 7%;
}

.catchpri {
	margin-left: -2%;
	width: 110%;
	text-align: center;
	font-size: x-large;
}
.searchbar {
	display: flex;
	align-items: center;
	margin-top: 1%;
	text-align: center;
	width: 500px;
	margin-left: 3%;
	justify-content: center;
}
.gotoitemlist {
	margin-top: 10%;
	text-align: center;
}
.pictureandwords {
}
</style>
