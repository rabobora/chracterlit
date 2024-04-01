<template>
	<header>
		<TheHeader />
	</header>
	<div id="app" class="container-fluid">
		<div class="row">
			<div class="category-navbar">
				<ul>
					<li v-for="myPage in myPagelist" :key="myPage.id">
						<i class="fa-solid fa-paper-plane" />
						<RouterLink to="/mypage/update">내 정보</RouterLink>
					</li>
				</ul>
			</div>
			<div class="col-md-10">
				<RouterView />
			</div>
		</div>
	</div>
</template>

<script>
import TheHeader from '@/components/common/TheHeader.vue';
import { RouterView } from 'vue-router';

export default {
	components: {
		TheHeader,
		RouterView,
	},
	data() {
		return {
			myPagelist: [
				{ id: 0, name: '내 정보' },
				{ id: 1, name: '입찰 내역' },
				{ id: 2, name: '판매 내역' },
				{ id: 3, name: '페이 내역' },
			],
		};
	},
};
</script>

<style>
.category-navbar {
	position: fixed; /* 고정 위치 */
	top: 50%; /* 뷰포트의 중앙에 배치 */
	transform: translateY(-25%); /* 요소의 중심을 정확히 뷰포트 중앙에 맞춤 */
	left: 0; /* 왼쪽 가장자리에 배치 */
	width: 200px; /* 네비게이션 바의 너비 */
	background-color: #000; /* 검정색 배경 */
	color: #fff; /* 텍스트 색상을 흰색으로 */
	box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
	z-index: 1000;
	margin-left: 2%;
	border-radius: 2%;
}

.category-navbar ul {
	list-style: none; /* 목록 스타일 제거 */
	padding: 0; /* 내부 여백 제거 */
	margin: 0; /* 외부 여백 제거 */
}

.category-navbar li {
	cursor: pointer; /* 마우스 오버 시 커서 변경 */
	padding: 10px; /* 패딩으로 여백 추가 */
	border-bottom: 1px solid #fff; /* 경계선 추가 */
}

.category-navbar li:last-child {
	border-bottom: none; /* 마지막 요소의 하단 경계선 제거 */
}
</style>
