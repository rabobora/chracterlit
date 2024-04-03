import { createRouter, createWebHistory } from "vue-router";

import ProductUpdateView from "@/views/product/ProductUpdateView.vue";
import ProductCreateView from "@/views/product/ProductCreateView.vue";
import ProductListView from "@/views/product/ProductListView.vue";
import SearchBarView from "@/views/product/SearchBarView.vue";
import ReadView from "@/views/product/ReadView.vue";
import MainPageView from "@/views/main/MainPageView.vue";
import MyBiddingView from "@/views/mypage/MyBiddingView.vue";
import MySellingView from "@/views/mypage/MySellingView.vue";
import LoginView from "@/views/LoginView.vue";
import MypageView from "@/views/mypage/MypageView.vue";
import MypageMainView from "@/views/mypage/MypageMainView.vue";
import MypageUpdateView from "@/views/mypage/MypageUpdateView.vue";
import chatView from "@/views/chat/chatView.vue";
import BuyItemView from "@/views/pay/BuyItemView.vue";
import PayStatementView from "@/views/pay/PayStatementView.vue";
import ChargeView from "@/views/pay/ChargeView.vue";
import WithdrawView from "@/views/pay/WithdrawView.vue";
import PayLoadingView from "@/views/pay/PayLoadingView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "mainPage",
      component: MainPageView,
    },

    {
      path: "/mypage",
      component: MypageView,
      children: [
        {
          path: "",
          name: "MypageMainView",
          component: MypageMainView,
        },
        {
          path: "update",
          name: "MypageUpdateView",
          component: MypageUpdateView,
        },
        {
          path: "mybid",
          name: "MyBiddingView",
          component: MyBiddingView,
        },
        {
          path: "mysell",
          name: "MySellingView",
          component: MySellingView,
        },
        {
          path: "paystatement",
          name: "PayStatement",
          component: PayStatementView,
        },
      ],
    },
    {
      path: "/product/update/:bidId",
      name: "productUpdate",
      component: ProductUpdateView,
    },

    {
      path: "/product/create",
      name: "productCreate",
      component: ProductCreateView,
    },

    {
      path: "/product/list",
      name: "productList",
      component: ProductListView,
    },

    {
      path: "/searchbar",
      name: "searchbar",
      component: SearchBarView,
    },

		{
			path: '/product/:number',
			name: 'ReadView',
			component: ReadView,
		},
		{
			path: '/login',
			name: 'login',
			component: LoginView,
		},
		{
			path: '/paystatement',
			name: 'payStatement',
			component: PayStatementView,
		},
		{
			path: '/charge',
			name: 'charge',
			component: ChargeView,
		},
		{
			path: '/loading',
			name: 'approveKakao',
			component: PayLoadingView,
			props: (route) => (
				{ order_id: route.query.order_id }, { pg_token: route.query.pg_token }
			),
		},
		{
			path: '/buyitem/:number',
			name: 'buyItem',
			component: BuyItemView,
		},
		{
			path: '/withdraw',
			name: 'withdraw',
			component: WithdrawView,
		},
		{
			path: '/chatPage/:bidId?',
			name: 'chatPage',
			component: chatView,
			props: (route) => ({
				bidId: route.params.bidId ? parseInt(route.params.bidId) : 0 // bidId가 전달되지 않았을 때 기본값으로 0 설정
			  }),
		},
	],
});

export default router;
