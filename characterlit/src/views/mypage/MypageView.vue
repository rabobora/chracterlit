<template>
	<header>
		<TheHeader />
	</header>
	<div id="app" class="container-fluid">
		<div class="row">
			<div class="category-navbar">
				<ul>
					<li v-for="myPage in myPagelist" :key="myPage.id">
						<i class="fa-solid fa-paper-plane"></i>
						<RouterLink :to="getPageLink(myPage.name)" class="nav-link">{{
							myPage.name
						}}</RouterLink>
					</li>
				</ul>
			</div>
			<div class="col-md-10">
				<RouterView />
			</div>
		</div>
	</div>
</template>

<script>
import TheHeader from '@/components/common/TheHeader.vue';
import { RouterView, RouterLink } from 'vue-router';

export default {
	components: {
		TheHeader,
		RouterView,
		RouterLink,
	},
	data() {
		return {
			myPagelist: [
				{ id: 0, name: '내 정보', link: '/mypage/update' },
				{ id: 1, name: '입찰 내역', link: '/mypage/mybid' },
				{ id: 2, name: '판매 내역', link: '/mypage/mysell' },
				{ id: 3, name: '페이 내역', link: '' },
			],
		};
	},
	methods: {
		getPageLink(pageName) {
			const page = this.myPagelist.find((item) => item.name === pageName);
			return page ? page.link : '';
		},
	},
};
</script>

<style>
.category-navbar {
	position: fixed;
	top: 50%;
	transform: translateY(-50%);
	left: 0;
	width: 200px;
	background-color: #000;
	color: #fff;
	box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
	z-index: 1000;
	margin-left: 2%;
	border-radius: 5px;
}

.category-navbar ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

.category-navbar li {
	cursor: pointer;
	padding: 10px;
	border-bottom: 1px solid #fff;
}

.category-navbar li:last-child {
	border-bottom: none;
}

.nav-link {
	color: #fff;
	text-decoration: none;
}
</style>
