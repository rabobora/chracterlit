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
	
	<div class="top3-container" :class="{ 'visible': top3ContainerVisible }">
		<div>
			<h1 class="ranktitle">현재 인기 있는 상품들</h1>
		</div>
		<div class="items-container"> <!-- 아이템을 가로로 나열하기 위한 새로운 컨테이너 -->
			<div @click="navigateToItem(product.bidId)"  class="item" v-for="(product, index) in productStore.gettop3ProductList" :key="product.bidId">
			<div class="rank">{{ index + 1 }}위</div>
			<img class="thumbnail" :src="product.thumbnail" alt="thumbnail">
			<div class="title">{{ product.title }}</div>
			</div>
		</div>
	</div>


 
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
const top3ContainerVisible = ref(false);


onMounted(() => {
  productStore.researchTop3Product();
  
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      // 요소가 뷰포트에 들어오면 true, 아니면 false로 설정
      top3ContainerVisible.value = entry.isIntersecting;
    });
  }, { threshold: 0.1 });

  const top3ContainerEl = document.querySelector('.top3-container');
  observer.observe(top3ContainerEl);
});

const gotoproductlist = () => {
	router.push({ name: 'productList' });
};

const navigateToItem = (bidId) => {
	router.push({ name: 'ReadView', params: { number: bidId } });
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
	margin-top: -5%;
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

.items-container {
  display: flex; /* 아이템을 가로로 나열 */
  justify-content: center; /* 아이템들을 컨테이너 중앙에 정렬 */
  gap: 20px; /* 아이템 사이의 간격 조정 */
}

.top3-container {
  display: flex;
  flex-direction: column; /* 내부 요소를 세로로 정렬 */
  align-items: center; /* 내부 요소를 가운데 정렬 */
  justify-content: center;
  padding: 20px;
  background-color: #3e78e5;
  height: 100vh;
}

.item {
  width: 250px;
  height: 300px;
  /* box-shadow: 0 2px 4px rgba(0,0,0,0.1); */
  border-radius: 5px;
  overflow: hidden;
  background: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  cursor: pointer;
}

.title {
  margin-top: 8%;
  font-size: 22px;
  font-weight: bold;
  /* margin-bottom: 10px; */
}

.thumbnail {
  width: 200px; 
  height: 200px; 
  /* object-fit: contain;  */
  border-radius: 5px; 
}
.rank{
	margin-bottom: 3%;
	font-size: 30px;
	font-weight: bolder;
}
.ranktitle {
  font-size: 28px; /* 폰트 크기 조정 */
  color: #ffffff; /* 글자 색상을 변경하여 가독성 향상 */
  margin-bottom: 20px; /* 상품 카드와의 간격 조정 */
  text-align: center; /* 제목을 가운데 정렬 */
}

/* 초기 상태: 요소가 화면에 등장하기 전 */
.top3-container {
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease-out, transform 0.5s ease-out;
}

/* 화면에 등장했을 때 적용될 스타일 */
.top3-container.visible {
  opacity: 1;
  transform: translateY(0);
}

</style>
