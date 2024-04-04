<template>
	<div class="container">
		<div class="login">
			<router-link to="/">
				<img class="logo" src="@/assets/logo.png" alt="logo" />
			</router-link>
			<div class="login-btns">
				<div class="naver-login-btn" @click="onNaverLogin">
					<img
						class="naver-login-btn-img"
						src="@/assets/naver_btn_big.png"
						alt="logo"
					/>
				</div>
				<div class="kakao-login-btn">
					<img
						class="kakao-login-btn-img"
						@click="onKakaoLogin"
						src="@/assets/kakao_btn_big.png"
						alt="logo"
					/>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	methods: {
		onNaverLogin() {
			window.location.href = `${
				import.meta.env.VITE_REST_URL
			}/oauth2/authorization/naver`;
		},
		onKakaoLogin() {
			window.location.href = `${
				import.meta.env.VITE_REST_URL
			}/oauth2/authorization/kakao`;
		},
	},
};
</script>

<style scoped>
.container {
	display: flex;
	justify-content: center;
	align-items: center;
}

.login {
	/* background-color: #333; */
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	margin-top: 20px;
	padding: 50px;
}

.logo {
	width: 300px;
	margin: 0 0 80px 0;
}

.login-btns {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.naver-login-btn-img,
.kakao-login-btn-img {
	width: 250px;
	height: 65px;
	margin: 1px 0;
	cursor: pointer;
}
</style>
