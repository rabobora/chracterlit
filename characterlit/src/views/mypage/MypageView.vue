<template>
	<div class="full-page">
		<header>
			<TheHeader />
		</header>
		<div v-if="store.getIsLogin" id="app" class="container-fluid">
			<div class="row">
				<div class="category-navbar">
					<ul>
						<li v-for="myPage in myPagelist" :key="myPage.id">
							<i class="fa-solid fa-paper-plane"></i>
							<RouterLink :to="getPageLink(myPage.name)" class="nav-link">{{
			myPage.name
		}}</RouterLink>
						</li>
					</ul>
				</div>
				<div class="col-md-10">
					<RouterView />
				</div>
			</div>
		</div>
		<div v-else class="login-notice">
			<h2>잠깐! 로그인이 필요해요 🚀</h2>
			<p>더 많은 기능을 사용하려면 로그인 해주세요.</p>
			<RouterLink to="/login" class="login-btn">로그인하기</RouterLink>
		</div>
	</div>
</template>

<script>
import TheHeader from '@/components/common/TheHeader.vue';
import { RouterView, RouterLink } from 'vue-router';
import { useUsersStore } from '@/stores/users';

export default {
	components: {
		TheHeader,
		RouterView,
		RouterLink,
	},
	data() {
		return {
			myPagelist: [
				{ id: 0, name: '　마이페이지', link: '/mypage' },
				{ id: 1, name: '　내 정보', link: '/mypage/update' },
				{ id: 2, name: '　입찰 내역', link: '/mypage/mybid' },
				{ id: 3, name: '　판매 내역', link: '/mypage/mysell' },
				{ id: 4, name: '　페이 내역', link: '/mypage/paystatement' },
				{ id: 5, name: '　채팅 내역', link: '/chatPage' }
			],
			store: useUsersStore(),
		};
	},
	methods: {
		getPageLink(pageName) {
			const page = this.myPagelist.find((item) => item.name === pageName);
			return page ? page.link : '';
		},
	},
};
</script>

<style>
.full-page {
	display: flex;
	flex-direction: column;
	/* height: 100vh; */
}

.login-notice {
	margin: auto;
	text-align: center;
	width: fit-content;
	padding: 40px;
	background-color: #f8f9fa;
	border-radius: 20px;
	box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.login-notice h2 {
	color: #333;
	margin-bottom: 20px;
}

.login-notice p {
	color: #666;
	margin-bottom: 30px;
}

.login-btn {
	display: inline-block;
	background-color: #007bff;
	color: #fff;
	padding: 10px 20px;
	text-decoration: none;
	border-radius: 5px;
	transition: background-color 0.3s ease;
}

.login-btn:hover {
	background-color: #0056b3;
}

.category-navbar {
	position: fixed;
	top: 50%;
	transform: translateY(-50%);
	left: 0;
	width: 200px;
	background-color: #000;
	color: #fff;
	box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
	z-index: 1000;
	margin-left: 2%;
	border-radius: 5px;
}

.category-navbar li:hover {
	opacity: 0.7;
	color: aqua;
}

.category-navbar ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

.category-navbar li {
	cursor: pointer;
	padding: 10px;
	border-bottom: 1px solid #fff;
}

.category-navbar li:last-child {
	border-bottom: none;
}

.nav-link {
	color: #fff;
	text-decoration: none;
}
</style>
