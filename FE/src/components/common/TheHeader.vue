<template>
	<header class="header">
		<nav class="navbar">
			<router-link class="logo" to="/">
				<img src="@/assets/logo.png" alt="logo" />
			</router-link>
			<ul class="nav-links">
				<div v-if="store.getIsLogin" class="nav-if-member">
					<div>
						<h4 class="nav-userInfo" @click="toUserInfo">
							{{ store.getLoginUser.nickname }}
						</h4>
					</div>
					<div><h4 class="nav-logout" @click="toLogout">로그아웃</h4></div>
				</div>
				<div v-else class="nav-if-guest">
					<h4 class="nav-login" @click="toLogin">로그인</h4>
				</div>
			</ul>
		</nav>
	</header>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useUsersStore } from '@/stores/users';
import router from '@/router';
import { RouterLink } from 'vue-router';

const store = useUsersStore();

const toLogin = async () => {
	try {
		await router.push('/login');
	} catch (error) {
		console.error('login failed:', error);
	}
};

const toLogout = async () => {
	try {
		await store.onLogout();
		router.push('/');
	} catch (error) {
		console.error('logout failed:', error);
	}
};

const toUserInfo = () => {
	router.push('/mypage');
};

onMounted(async () => {
	await store.saveTokenToLocalStorage();
	await store.fetchLoginUser();
});
</script>

<style scoped>
.header {
	/* background-color: #333; */
	color: black;
	align-content: center;
	padding: 1rem 0px;
	width: 100%;
}

.navbar {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 0 8%;
}

.logo img {
	height: 120px;
}

.nav-if-member {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.nav-login {
	cursor: pointer;
}

.nav-logout {
	cursor: pointer;
}

.nav-userInfo {
	cursor: pointer;
	margin-right: 20px;
}
</style>
