import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import router from '@/router';
import axios from 'axios';

export const usePayStore = defineStore('pay', () => {
	//  =========== STATE ===============
	const userPoint = ref({});
	const allPoint = ref(0);
	const usablePoint = ref(0);

	const statementList = ref([]);

	const kakaoResponse = ref({});
	const tid = ref('');
	const next_redirect_pc_url = ref('');
	const created_at = ref('');
	// =========== GETTER ===============
	const getUserPoint = computed(() => {
		return userPoint.value;
	});

	const getAllPoint = computed(() => {
		return allPoint.value;
	});

	const getUsablePoint = computed(() => {
		return usablePoint.value;
	});

	const getStatementList = computed(() => {
		return statementList.value;
	});

	// =========== ACTION ===============

	// 포인트 조회
	const pointCheck = function () {
		axios({
			url: `${import.meta.env.VITE_REST_POINT_API}`,
			method: 'GET',
			headers: {
				'access_token': localStorage.getItem('access-token'),
				'Content-type': 'application/json',
			},
			withCredentials: true,
		})
			.then((res) => {
				userPoint.value = res.data;
				allPoint.value = userPoint.value.allPoint;
				usablePoint.value = userPoint.value.usablePoint;
			})
			.catch((err) => {});
	};

	// 포인트 내역 조회
	const statementRead = function () {
		axios({
			url: `${import.meta.env.VITE_REST_POINT_API}/statement`,
			method: 'GET',
			headers: {
				'access_token': localStorage.getItem('access-token'),
				'Content-type': 'application/json',
			},
			withCredentials: true,
		})
			.then((res) => {
				if(res.data){
					return statementList.value = res.data;
				} else{
					return null;
				}
			})
			.catch((err) => {});
	};

	// 계좌로 충전하기
	const chargeAccount = function (request) {
		axios({
			url: `${import.meta.env.VITE_REST_POINT_API}/charge`,
			method: 'PUT',
			data: request,
			headers: {
				'access_token': localStorage.getItem('access-token'),
				'Content-type': 'application/json',
			},
			withCredentials: true,
		})
			.then((res) => {
				window.alert('충전이 완료되었습니다.');
				router.push('/mypage');
			})
			.catch((err) => {});
	};

	// 카카오 결제 준비
	const readyKakao = async function (request) {
		await axios({
			url: `${import.meta.env.VITE_REST_KAKAO_API}/ready`,
			method: 'POST',
			data: request,
			headers: {
				'access_token': localStorage.getItem('access-token'),
				'Content-type': 'application/json',
			},
			withCredentials: true,
		})
			.then((res) => {
				kakaoResponse.value = res.data;
				tid.value = kakaoResponse.value.tid;
				next_redirect_pc_url.value = kakaoResponse.value.next_redirect_pc_url;
				created_at.value = kakaoResponse.value.created_at;
				location.href = next_redirect_pc_url.value;
			})
			.catch((err) => {
				console.log(err);
			});
	};

	// 카카오 결제 승인
	const approveKakao = async function (order_id, pg_token) {
		await axios({
			url: `${import.meta.env.VITE_REST_KAKAO_API}/success`,
			method: 'GET',
			params: { order_id: order_id, pg_token: pg_token },
			headers: {
				'access_token': localStorage.getItem('access-token'),
				'Content-type': 'application/json',
			},
			withCredentials: true,
		})
			.then((res) => {
				window.alert('충전이 완료되었습니다.');
				router.push('/mypage');
			})
			.catch((err) => {
				console.log(err);
				window.alert('충전을 실패하였습니다.');
				router.push('/mypage');
			});
	};

	// 포인트 출금하기
	const withdrawPoint = function (request) {
		axios({
			url: `${import.meta.env.VITE_REST_POINT_API}/withdraw`,
			method: 'PUT',
			data: request,
			headers: {
				'access_token': localStorage.getItem('access-token'),
				'Content-type': 'application/json',
			},
			withCredentials: true,
		})
			.then((res) => {
				window.alert('출금이 완료되었습니다.');
				router.push('/mypage')
			})
			.catch((err) => {});
	};

	// 구매하기
	const buyItem = async function (request) {
		console.log(request)
		await axios({
			url: `${import.meta.env.VITE_REST_POINT_API}/buy`,
			method: 'PUT',
			data: request,
			headers: {
				'access_token': localStorage.getItem('access-token'),
				'Content-type': 'application/json',
			},
			withCredentials: true,
		})
			.then((res) => {
				window.alert("결제가 완료되었습니다.");
				router.push("/mypage");
			})
			.catch((err) => {
				window.alert("결제가 실패하였습니다.");
				console.log(err);
			});
	};

	// 구매확정
	const buyComplete = function () {
		axios({
			url: `${import.meta.env.VITE_REST_POINT_API}/complete/${budId}`,
			method: 'PUT',
			data: item,
			headers: {
				'access_token': localStorage.getItem('access-token'),
				'Content-type': 'application/json',
			},
			withCredentials: true,
		})
			.then((res) => {})
			.catch((err) => {});
	};

	return {
		userPoint,
		allPoint,
		usablePoint,
		statementList,
		kakaoResponse,
		tid,
		next_redirect_pc_url,
		created_at,
		getUserPoint,
		getAllPoint,
		getUsablePoint,
		getStatementList,
		pointCheck,
		statementRead,
		chargeAccount,
		readyKakao,
		approveKakao,
		withdrawPoint,
		buyItem,
		buyComplete,
	};
});
