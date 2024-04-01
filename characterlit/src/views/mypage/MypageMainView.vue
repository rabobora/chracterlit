<template>
	<div class="mypage-container">
		<div class="point-section">
			<div class="point-title">
				<div class="point-icon">
					<font-awesome-icon icon="piggy-bank" />
				</div>
				<h3 class="point-name">포인트</h3>
				<button @click="$router.push('/charge')" class="point-button">
					충전하기
				</button>
				<button @click="$router.push('/withdraw')" class="point-button">
					출금하기
				</button>
			</div>
			<hr />
			<div class="point-first">
				<h4>보유중인 포인트</h4>
				<h4>(보유p)</h4>
			</div>
			<div class="point-second">
				<h4>사용가능 포인트</h4>
				<h4>(사용가능p)</h4>
			</div>
		</div>
		<div class="link-section">
			<RouterLink to="/buylist" class="mypage-link">
				<div class="link-icon">
					<font-awesome-icon icon="bag-shopping" />
				</div>
			</RouterLink>
			<RouterLink to="/selllist" class="mypage-link">
				<div class="link-icon">
					<font-awesome-icon icon="store" />
				</div>
			</RouterLink>
			<RouterLink to="/chatlist" class="mypage-link">
				<div class="link-icon">
					<font-awesome-icon icon="comments" />
				</div>
			</RouterLink>
			<RouterLink to="/pointstatement" class="mypage-link">
				<div class="link-icon">
					<font-awesome-icon icon="receipt" />
				</div>
			</RouterLink>
			<RouterLink to="/updateuser" class="mypage-link">
				<div class="link-icon">
					<font-awesome-icon icon="user" />
				</div>
			</RouterLink>
		</div>
	</div>
</template>

<script setup></script>

<style></style>
