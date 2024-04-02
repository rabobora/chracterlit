import { createRouter, createWebHistory } from 'vue-router';


import ProductUpdateView from '@/views/product/ProductUpdateView.vue';
import ProductCreateView from '@/views/product/ProductCreateView.vue';
import ProductListView from '@/views/product/ProductListView.vue';
import SearchBarView from '@/views/product/SearchBarView.vue';
import ReadView from '@/views/product/ReadView.vue';
import MainPageView from '@/views/main/MainPageView.vue';
import MyBiddingView from '@/views/mypage/MyBiddingView.vue'
import MySellingView from '@/views/mypage/MySellingView.vue'
import LoginView from '@/views/LoginView.vue';
import MypageView from '@/views/mypage/MypageView.vue';
import MypageMainView from '@/views/mypage/MypageMainView.vue';
import MypageUpdateView from '@/views/mypage/MypageUpdateView.vue';

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			name: 'mainPage',
			component: MainPageView,
		},

		{
			path: '/mypage',
			component: MypageView,
			children: [
				{
					path: '',
					name: 'MypageMainView',
					component: MypageMainView,
				},
				{
					path: 'update', // 수정됨: 상대 경로 사용
					name: 'MypageUpdateView',
					component: MypageUpdateView,
				},
			],
		},
	

		{
			path: '/product/update/:bidId',
			name: 'productUpdate',
			component: ProductUpdateView,
		},

		{
			path: '/product/create',
			name: 'productCreate',
			component: ProductCreateView,
		},

		{
			path: '/product/list',
			name: 'productList',
			component: ProductListView,
		},

		{
			path: '/searchbar',
			name: 'searchbar',
			component: SearchBarView,
		},

		{
			path: '/product/:number',
			name: 'ReadView',
			component: ReadView,
		},
		{
		  path: '/my/mybid',
		  name: 'MyBiddingView',
		  component: MyBiddingView,
		},
		{
		  path: '/my/mysell',
		  name: 'MySellingView',
		  component: MySellingView,
		},
		{
			path: '/login',
			name: 'login',
			component: LoginView,
		},
	],
});

export default router;
