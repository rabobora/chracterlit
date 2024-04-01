import { createRouter, createWebHistory } from 'vue-router'

import CategorySelectView from '@/views/product/CategorySelectView.vue'
import ProductUpdateView from '@/views/product/ProductUpdateView.vue'
import ProductCreateView from '@/views/product/ProductCreateView.vue'
import ProductListView from '@/views/product/ProductListView.vue'
import SearchBarView from '@/views/product/SearchBarView.vue'
import ReadView from '@/views/product/ReadView.vue'
import MainPageView from '@/views/main/MainPageView.vue'
import MyBiddingView from '@/views/my/MyBiddingView.vue'
import MySellingView from '@/views/my/MySellingView.vue'
import LoginView from '@/views/LoginView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [  
    {
      path: '/',
      name: 'mainPage',
      component: MainPageView
    },

    {
      path: '/product/category',
      name: 'productCategory',
      component: CategorySelectView
    },

    {
      path: '/product/update/:bidId',
      name: 'productUpdate',
      component: ProductUpdateView
    },

    {
      path: '/product/create',
      name: 'productCreate',
      component: ProductCreateView
    },

    {
      path: '/product/list',
      name: 'productList',
      component: ProductListView
    },

    {
      path: '/searchbar',
      name: 'searchbar',
      component: SearchBarView
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
      component: LoginView,
      children: [
          {
              path: '/login',
              name: 'login',
              component: LoginView,
          },
      ],
  },   
  ]
})

export default router
