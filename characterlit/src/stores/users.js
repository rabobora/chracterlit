import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import router from '@/routers';

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

		const onLogin = () => {
			window.location.href = 'http://localhost:8080/oauth2/authorization/naver';
		};

		const onLogout = () => {
			fetch('http://localhost:8080/logout', {
				method: 'POST',
				credentials: 'include',
			})
				.then(() => {
					localStorage.removeItem('access-token');
					router.push('/');
				})
				.catch((error) => {
					console.log(error);
					router.push('/');
				});
		};

		
		// const logout = async () => {
		// 	try {
		// 		await axios({
		// 			url: `${import.meta.env.VITE_REST_API}/logout`,
		// 			method: 'PUT',
		// 		});
		// 		localStorage.removeItem('userAccessToken');
		// 		userAccessToken.value = null;

		// 		router.push('/');
		// 	} catch (err) {
		// 		router.push('/');
		// 		window.alert('오류가 발생했습니다');
		// 	}
		// };

		return {
			user,
			userId,
			userName,
			isLogin,
			getUser,
			getUserNumber,
			getUserId,
			onLogin,
			onLogout,
		};
	},
	{ persist: true },
);
