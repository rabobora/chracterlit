<template>
	<div class="view">
		<RouterView />
	</div>
	<footer>
		<TheFooter />
	</footer>
</template>

<script setup>
import { RouterView } from 'vue-router';
import TheFooter from './components/common/TheFooter.vue';
</script>

<style scoped>
.view {
	min-height: 100%;
}
</style>
