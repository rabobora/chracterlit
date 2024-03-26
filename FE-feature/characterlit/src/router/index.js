import HomeView from '@/views/HomeView.vue';
import ReadView from '@/views/ReadView.vue';
import { createRouter, createWebHistory } from 'vue-router';


const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: "/",
			name: 'HomeView',
			component: HomeView,
		},
		{
			path: '/read/:number',
			name: 'ReadView',
			component: ReadView,
		},
	],
});

export default router;
