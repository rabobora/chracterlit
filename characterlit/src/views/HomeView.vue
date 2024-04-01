<template>
	<TheHeader />
	<div>
		<RouterView />
	</div>
</template>

<script setup>
import { RouterView } from 'vue-router';
import TheHeader from '@/components/common/TheHeader.vue';
</script>

<style scoped></style>
