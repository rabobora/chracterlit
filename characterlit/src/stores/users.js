import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
// import router from '@/routers';

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
		const getLoginUser = async () => {
			// 로컬 스토리지에서 토큰을 저장하는 함수 호출
			// saveTokenToLcalStorage();
		  
			// 사용자 정보를 가져오는 API 요청
			try {
			  const response = await fetch('http://localhost:8080/api/users/id', {
				method: 'POST',
				headers: {
				  'access_token': localStorage.getItem('access-token'),
				  'Content-type': 'application/json',
				},
				credentials: 'include',
			  });
		  
			  // 응답을 JSON 형식으로 변환
			  const userData = await response.json();
		  
			  // userData에서 userNumber를 추출하여 상태에 할당
			  if (userData && userData.userNumber) {
				userNumber.value = userData.userNumber;
				user.value = userData; // 전체 사용자 데이터를 user 상태에 할당
			  }
			} catch (error) {
			  console.error(error);
			}
		  };
		// const getLoginUser = () => {
		// 	saveTokenToLcalStorage();
		// 	fetch('http://localhost:8080/api/users/id', {
		// 		method: 'GET',
		// 		headers: {
		// 			'access_token': localStorage.getItem('access-token'),
		// 			'Content-type': 'application/json',
		// 		},
		// 		credentials: 'include',
		// 	})
		// 		.then((res) => console.log(JSON.stringify(res.data)))
		// 		.catch((error) => console.log(error));
		// };
		

		
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
			// userName,
			isLogin,
			getUser,
			getUserNumber,
			getUserId,
			getLoginUser,
			onLogin,
			onLogout,
		};
	},
	{ persist: true },
);
