<template>
	<header class="header">
		<nav class="navbar">
			<router-link class="logo" to="/">
				<img src="@/assets/logo.png" alt="logo" />
			</router-link>
			<ul class="nav-links">
				<div v-if="isLoggedIn" class="nav-if-member">
					<h2 class="nav-logout" @click="logout">로그아웃</h2>
				</div>
				<div v-else="isLoggedIn" class="nav-if-guest">
					<h2 class="nav-login" @click="login">로그인</h2>
				</div>
			</ul>
		</nav>
	</header>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useUsersStore } from '@/stores/users';
import router from '@/router';

const isLoggedIn = ref(false);
const store = useUsersStore();

const getCookie = (name) => {
	let matches = document.cookie.match(
		new RegExp(
			'(?:^|; )' +
				name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') +
				'=([^;]*)',
		),
	);
	return matches ? decodeURIComponent(matches[1]) : undefined;
};

const deleteCookie = (name) => {
	document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
};

const saveTokenToLocalStorage = () => {
	let token = getCookie('access_token');
	deleteCookie('access_token');
	if (token != null && token !== 'undefined')
		localStorage.setItem('access-token', token);
	token = null;
};

const checkLoginStatus = () => {
	let token = localStorage.getItem('access-token');
	if (token) {
		isLoggedIn.value = true;
	} else {
		isLoggedIn.value = false;
	}
};

const login = async () => {
	try {
		router.push('/login');
	} catch (error) {
		console.error('login failed:', error);
	}
};

const logout = async () => {
	try {
		store.onLogout();
		isLoggedIn.value = false;
		localStorage.removeItem('access-token');
		// router.push('/');
	} catch (error) {
		console.error('logout failed:', error);
	}
	location.reload(true);
};

onMounted(() => {
	saveTokenToLocalStorage();
	checkLoginStatus();
});
</script>

<style scoped>
.header {
	background-color: #333;
	color: white;
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

.nav-login {
	cursor: pointer;
}

.nav-logout {
	cursor: pointer;
}
</style>
