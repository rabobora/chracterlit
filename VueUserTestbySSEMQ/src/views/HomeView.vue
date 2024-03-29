<template>
	<div>
		<h1>Login</h1>
		<button @click="onLogin">Login</button>
		<button @click="onLogout">Logout</button>
		<button @click="reissueToken">Reissue Token</button>
		<button @click="getMy">getMy</button>
		<button @click="getMain">getMain</button>
		<button @click="getLoginUser">getLoginUser</button>
	</div>
	<div v-for="num in 5" :key="num">
      <router-link :to="`/read/${num}`">Read Page {{ num }}</router-link>
    </div>
</template>

<script setup>

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

const saveTokenToLcalStorage = () => {
	if (
		getCookie('access_token') != null &&
		getCookie('access_token') !== 'undefined'
	)
		localStorage.setItem('access-token', getCookie('access_token'));
};

const onLogin = () => {
	window.location.href = 'http://https://j10b105.p.ssafy.io:8080/oauth2/authorization/naver';
};

const getLoginUser = () => {
	saveTokenToLcalStorage();
	fetch('http://j10b105.p.ssafy.io:8080/api/users/id', {
		method: 'GET',
		headers: {
			'access_token': localStorage.getItem('access-token'),
			// 'Content-type': 'application/json',
		},
		credentials: 'include',
	})
		.then((res) => console.log(res.body))
		.catch((error) => console.log(error));
};

const getMy = () => {
	saveTokenToLcalStorage();
	fetch('http://j10b105.p.ssafy.io:8080/my', {
		method: 'GET',
		headers: { 'access_token': localStorage.getItem('access-token') },
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};

const getMain = () => {
	saveTokenToLcalStorage();
	fetch('http://j10b105.p.ssafy.io:8080/', {
		method: 'GET',
		credentials: 'include',
	}).then((res) => {
		console.log(res);
	});
};

const reissueToken = () => {
	saveTokenToLcalStorage();
	fetch('http://j10b105.p.ssafy.io:8080/reissue', {
		method: 'POST',
		headers: { 'access_token': localStorage.getItem('access-token') },
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};

const onLogout = () => {
	fetch('http://j10b105.p.ssafy.io:8080/logout', {
		method: 'POST',
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};
</script>

<style scoped></style>
