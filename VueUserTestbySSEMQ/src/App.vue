<template>
	<div class="app">
		<TheHeader />
		<div class="main-content">
			<RouterView />
		</div>
	</div>
</template>

<script setup>
import { RouterView } from 'vue-router';
</script>

<style scoped>
.app {
	display: flex;
	flex-direction: column;
	min-height: 100vh;
	position: relative;

	z-index: 1;
	padding-bottom: 5%;
	margin-bottom: 5%;
}

.main-content {
	flex: 1;
	/* Adjust margin bottom to avoid overlapping with footer */
}
</style>
