import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import UserTest from '@/views/mypage/UserTest.vue';
import UserInfoRevView from '@/views/mypage/UserInfoRevView.vue';
const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			component: HomeView,
			children: [
				{
					path: 'test',
					name: 'test',
					component: UserTest,
				},
				{
					path: 'user',
					name: 'user',
					component: UserInfoRevView,
				},
			],
		},
		{
			path: '/login',
			component: LoginView,
			children: [
				{
					path: '/login',
					name: 'login',
					component: LoginView,
				},
			],
		},
	],
});

export default router;
