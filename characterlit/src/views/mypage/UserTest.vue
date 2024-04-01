<template>
	<div>
		<h1>Login</h1>
		<button @click="onNaverLogin">NaverLogin</button>
		<button @click="onKakaoLogin">onKakaoLogin</button>
		<button @click="onLogout">LogOut</button>
		<button @click="reissueToken">ReissueAccessToken</button>
		<button @click="getMy">(User 권한 필요 O)</button>
		<button @click="getMain">(User 권한 필요 X)</button>
		<button @click="getLoginUser">로그인한 유저 정보 요청</button>
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

const deleteCookie = (name) => {
	document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
};

const saveTokenToLocalStorage = () => {
	let token = getCookie('access_token');
	deleteCookie(token);
	if (token != null && token !== 'undefined')
		localStorage.setItem('access-token', token);
	token = null;
};

const onNaverLogin = () => {
	window.location.href = 'http://localhost:8080/oauth2/authorization/naver';
};

const onKakaoLogin = () => {
	window.location.href = 'http://localhost:8080/oauth2/authorization/kakao';
};

const onLogout = () => {
	fetch('http://localhost:8080/logout', {
		method: 'POST',
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};

const reissueToken = () => {
	saveTokenToLocalStorage();
	fetch('http://localhost:8080/reissue', {
		method: 'POST',
		headers: { 'access_token': localStorage.getItem('access-token') },
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};

const getMy = () => {
	saveTokenToLocalStorage();
	fetch('http://localhost:8080/my', {
		method: 'GET',
		headers: { 'access_token': localStorage.getItem('access-token') },
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};

const getMain = () => {
	saveTokenToLocalStorage();
	fetch('http://localhost:8080/main', {
		method: 'GET',
		credentials: 'include',
	}).then((res) => {
		console.log(res);
	});
};

const getLoginUser = () => {
	saveTokenToLocalStorage();
	// fetch 함수 자체가 Promise를 반환하므로 이를 직접 반환함.
	return fetch('http://localhost:8080/api/users/loginuser', {
		method: 'GET',
		headers: {
			'access_token': localStorage.getItem('access-token'),
			'Content-type': 'application/json',
		},
		credentials: 'include',
	})
		.then((res) => {
			if (res.ok) {
				return res.json(); // 성공 시, JSON 형태로 파싱된 데이터를 다음 then 구문으로 전달.
			}
			throw new Error('Network response was not ok.');
		})
		.catch((error) => {
			console.error(error);
			throw error;
		});
};
</script>

<style scoped></style>
