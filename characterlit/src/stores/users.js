import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import router from '@/router';

export const useUsersStore = defineStore(
	'users',
	() => {
		// =========== STATE ===============

		const user = ref({});
		const userNumber = ref('');
		const userId = ref('');
		const isLogin = ref(true);

		// =========== GETTER ===============

		const getUser = computed(() => {
			return user.value;
		});

		const getUserNumber = computed(() => {
			return userNumber.value;
		});

		const getUserId = computed(() => {
			return userId.value;
		});

		// =========== ACTION ===============
		const getCookie = (name) => {
			let matches = document.cookie.match(
				new RegExp(
					'(?:^|; )' +
						name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') +
						'=([^;]*)',
				),
			);
			return matches ? decodeURIComponent(matches[1]) : undefined;
		};

		const deleteCookie = (name) => {
			document.cookie =
				name + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
		};

		const saveTokenToLocalStorage = () => {
			let token = getCookie('access_token');
			deleteCookie(token);
			if (token != null && token !== 'undefined')
				localStorage.setItem('access-token', token);
			token = null;
		};

		const onLogout = () => {
			fetch('http://localhost:8080/logout', {
				method: 'POST',
				credentials: 'include',
			})
				.then(() => {
					localStorage.removeItem('access-token');
				})
				.catch((error) => {
					console.log(error);
				});
		};

		const getLoginUser = () => {
			saveTokenToLocalStorage();
			return fetch('http://localhost:8080/api/users/loginuser', {
				method: 'GET',
				headers: {
					'access_token': localStorage.getItem('access-token'),
					'Content-type': 'application/json',
				},
				credentials: 'include',
			})
				.then((res) => {
					if (res.ok) {
						return res.json();
					}
					throw new Error('Network response was not ok.');
				})
				.catch((error) => {
					console.log(error);
					throw error;
				});
		};

		const updateLoginUser = (userInfo) => {
			saveTokenToLocalStorage();
			return fetch('http://localhost:8080/api/users/loginuser', {
				method: 'PATCH',
				headers: {
					'access_token': localStorage.getItem('access-token'),
					'Content-Type': 'application/json',
				},
				credentials: 'include',
				body: JSON.stringify(userInfo),
			})
				.then((res) => {
					if (res.ok) {
						return res.json();
					}
					throw new Error('Network response was not ok.');
				})
				.catch((error) => {
					console.log(error);
					throw error;
				});
		};

		return {
			user,
			userId,
			userNumber,
			isLogin,
			getUser,
			getUserNumber,
			getUserId,
			getLoginUser,
			updateLoginUser,
			onLogout,
		};
	},
	{ persist: true },
);
