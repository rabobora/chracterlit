import { ref, computed } from 'vue';
import { defineStore } from 'pinia';

export const useUsersStore = defineStore(
	'users',
	() => {
		// =========== STATE ===============

		const isLogin = ref(true);
		const loginUser = ref({
			userNumber: '',
			loginServer: '',
			userId: '',
			role: '',
			email: '',
			name: '',
			nickname: '',
			phoneNumber: '',
			address: '',
			profileImg: '',
			credit: '',
			createdDate: '',
			deletedDate: '',
		});

		// =========== GETTER ===============

		const getLoginUser = computed(() => {
			return loginUser.value;
		});

		const getIsLogin = computed(() => {
			return isLogin.value;
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
			deleteCookie('access_token');
			if (token != null && token !== 'undefined')
				localStorage.setItem('access-token', token);
			token = null;
			checkLoginStatus();
		};

		const checkLoginStatus = () => {
			let token = localStorage.getItem('access-token');
			if (token) {
				isLogin.value = true;
			} else {
				isLogin.value = false;
			}
			console.log(isLogin.value);
		};

		const fetchLoginUser = async () => {
			try {
				const userData = await searchLoginUser();
				loginUser.value = userData; // getLoginUser 함수에서 반환된 사용자 데이터를 loginUser ref에 할당
			} catch (error) {
				console.error('Failed to fetch login user:', error);
			}
		};

		// =========== FETCH ===============

		const onLogout = () => {
			fetch(`${import.meta.env.VITE_REST_URL}/logout`, {
				method: 'POST',
				credentials: 'include',
			})
				.then(() => {
					localStorage.removeItem('access-token');
					isLogin.value = false;
				})
				.catch((error) => {
					console.log(error);
				});
		};

		const searchLoginUser = () => {
			saveTokenToLocalStorage();
			return fetch(`${import.meta.env.VITE_REST_API}/users/login`, {
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
			return fetch(`${import.meta.env.VITE_REST_API}/users/login`, {
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

		const isExistNickname = (nickname) => {
			saveTokenToLocalStorage();
			return fetch(
				`${import.meta.env.VITE_REST_API}/users/find/nickname/${nickname}`,
				{
					method: 'GET',
					headers: {
						'access_token': localStorage.getItem('access-token'),
					},
					credentials: 'include',
				},
			)
				.then((res) => {
					return res.json();
				})
				.then((data) => {
					return data;
				})
				.catch((error) => {
					console.log(error);
					throw error;
				});
		};

		return {
			isLogin,
			loginUser,
			getIsLogin,
			getLoginUser,
			getCookie,
			deleteCookie,
			saveTokenToLocalStorage,
			checkLoginStatus,
			fetchLoginUser,
			onLogout,
			searchLoginUser,
			updateLoginUser,
			isExistNickname,
		};
	},
	{ persist: true },
);
