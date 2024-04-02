<template>
	<div class="update-profile container">
		<div class="row">
			<div class="col">
				<h2 class="mb-4 text-start">회원정보 수정</h2>
				<form @submit.prevent="submitForm">
					<!-- 닉네임 필드 -->
					<div class="mb-3">
						<label for="nickname" class="form-label">닉네임 (Required)</label>
						<div class="input-group">
							<input
								type="text"
								class="form-control"
								id="nickname"
								v-model="loginUser.nickname"
								@input="nicknameChanged = true"
								maxlength="16"
							/>
							<button
								class="btn"
								type="button"
								:disabled="!nicknameChanged"
								@click="checkNickname"
							>
								중복 확인
							</button>
						</div>
						<div
							v-if="nicknameChanged && !nicknameValidated"
							class="text-danger"
						>
							닉네임 중복 확인이 필요합니다.
						</div>
						<div
							v-if="nicknameMessage"
							:class="{
								'text-danger': !isNicknameAvailable,
								'text-primary': isNicknameAvailable,
							}"
						>
							{{ nicknameMessage }}
						</div>
					</div>

					<!-- 이메일 필드 -->
					<div class="mb-3">
						<label for="email" class="form-label">이메일</label>
						<input
							type="email"
							class="form-control"
							id="email"
							v-model="loginUser.email"
							disabled
						/>
					</div>

					<!-- 이름 필드 -->
					<div class="mb-3">
						<label for="name" class="form-label">이름</label>
						<input
							type="text"
							class="form-control"
							id="name"
							v-model="loginUser.name"
							maxlength="16"
						/>
					</div>

					<!-- 전화번호 필드 -->
					<div class="mb-3">
						<label for="phoneNumber" class="form-label">전화번호</label>
						<input
							type="tel"
							class="form-control"
							id="phoneNumber"
							v-model="loginUser.phoneNumber"
							@input="
								loginUser.phoneNumber = loginUser.phoneNumber.replace(
									/[^0-9]/g,
									'',
								)
							"
							maxlength="16"
						/>
					</div>

					<!-- 주소 필드 -->
					<div class="mb-3">
						<label for="zonecode" class="form-label">주소 (Required)</label>
						<div class="input-group">
							<input
								type="text"
								class="form-control"
								id="zonecode"
								v-model="zonecode"
								readonly
							/>
							<input
								type="text"
								class="form-control"
								id="address"
								v-model="address"
								readonly
							/>
							<button class="btn" type="button" @click="findAddress">
								주소 찾기
							</button>
						</div>
						<div
							v-if="!zonecode && !address && detailAddress"
							class="text-danger"
						>
							주소를 입력해주세요.
						</div>
					</div>

					<!-- 상세주소 필드 -->
					<div class="mb-3">
						<label for="detailAddress" class="form-label">상세주소</label>
						<input
							type="text"
							class="form-control"
							id="detailAddress"
							v-model="detailAddress"
							maxlength="100"
						/>
					</div>

					<!-- 제출 버튼 -->
					<div class="d-flex justify-content-end">
						<button
							type="submit"
							class="btn btn-success"
							style="padding: 0.5rem 2rem; font-size: 1.25rem"
						>
							제출
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</template>
<style>
/* 전체 컨테이너 스타일링 */
.update-profile.container {
	max-width: 800px;
	margin: 20px auto;
	padding: 20px;
	background-color: #f5f5f5;
	border-radius: 8px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* 라벨 스타일링 */
.update-profile .form-label {
	color: #333;
	font-weight: 500;
	margin-bottom: 8px;
}

/* 인풋 필드 스타일링 */
.update-profile .form-control {
	border-radius: 4px;
	border: 1px solid #ced4da;
}

.update-profile .form-control:focus {
	border-color: #80bdff;
	box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

/* 인풋 그룹 버튼 스타일링 */
.update-profile .btn {
	margin-left: 8px;
	background-color: #007bff;
	color: white;
}

.update-profile .btn:disabled {
	background-color: #6c757d;
}

.update-profile .btn:hover {
	background-color: #0056b3;
}

/* 제출 버튼 스타일링 */
.update-profile .btn-success {
	background-color: #28a745;
	border-color: #28a745;
}

.update-profile .btn-success:hover {
	background-color: #218838;
	border-color: #1e7e34;
}

/* 경고 및 메시지 스타일링 */
.update-profile .text-danger {
	color: #dc3545;
}

.update-profile .text-primary {
	color: #007bff;
}

/* 폼 타이틀 스타일링 */
.update-profile h2 {
	color: #333;
}

/* 상세 주소 필드 마진 조정 */
.update-profile #detailAddress {
	margin-top: 8px;
}
</style>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useUsersStore } from '@/stores/users';
import router from '@/router';
const usersStore = useUsersStore();

const loginUser = ref({
	userNumber: '',
	nickname: '',
	email: '',
	name: '',
	phoneNumber: '',
	address: '',
});

const zonecode = ref('');
const address = ref('');
const detailAddress = ref('');
const originalNickname = ref('');
const nicknameChanged = ref(false);
const nicknameValidated = ref(false);
const nicknameMessage = ref('');
const isNicknameAvailable = ref(false);

onMounted(async () => {
	await usersStore.fetchLoginUser();
	const user = usersStore.getLoginUser;
	loginUser.value = { ...loginUser.value, ...user };
	originalNickname.value = user.nickname; // 원래 닉네임을 저장
	const addressParts = user.address.split('$');
	if (addressParts.length === 3) {
		zonecode.value = addressParts[0];
		address.value = addressParts[1];
		detailAddress.value = addressParts[2];
	}
});

// 닉네임 입력 필드에 입력이 있을 때마다 닉네임 변경 여부를 판단
watch(
	() => loginUser.value.nickname,
	(newVal, oldVal) => {
		nicknameChanged.value = newVal !== originalNickname.value;
	},
);

function validateNickname(nickname) {
	const byteLength = new Blob([nickname]).size;
	if (byteLength > 16) {
		nicknameMessage.value = '닉네임은 16바이트를 넘을 수 없습니다.';
		isNicknameAvailable.value = false;
		return false;
	}
	if (nickname.length < 2) {
		nicknameMessage.value = '닉네임은 최소 2자 이상이어야 합니다.';
		isNicknameAvailable.value = false;
		return false;
	}
	if (!/^[가-힣A-Za-z]+$/.test(nickname)) {
		nicknameMessage.value = '닉네임에는 한글과 영어만 사용 가능합니다.';
		isNicknameAvailable.value = false;
		return false;
	}
	return true;
}

async function checkNickname() {
	if (!validateNickname(loginUser.value.nickname)) {
		nicknameValidated.value = false;
		return;
	}

	if (loginUser.value.nickname === originalNickname.value) {
		nicknameMessage.value = '사용 가능한 닉네임입니다.';
		isNicknameAvailable.value = true;
		nicknameValidated.value = true;
		nicknameChanged.value = false;
		return;
	}
	if (await usersStore.isExistNickname(loginUser.value.nickname)) {
		nicknameMessage.value = '중복된 닉네임입니다.';
		isNicknameAvailable.value = false;
		nicknameValidated.value = false;
	} else {
		nicknameMessage.value = '사용 가능한 닉네임입니다.';
		isNicknameAvailable.value = true;
		nicknameValidated.value = true;
		nicknameChanged.value = false;
	}
}

function findAddress() {
	new daum.Postcode({
		oncomplete: function (data) {
			zonecode.value = data.zonecode;
			address.value = data.address;
		},
	}).open();
}

const submitForm = () => {
	if (nicknameChanged.value && !nicknameValidated.value) {
		alert('닉네임 중복 체크를 해주셔야 합니다.');
		return;
	}

	if (!zonecode.value || !address.value) {
		alert('주소를 입력해 주세요.');
		return;
	}

	usersStore
		.updateLoginUser({
			...loginUser.value,
			address: `${zonecode.value}$${address.value}$${detailAddress.value}`,
		})
		.then(() => {
			alert('회원 정보가 성공적으로 수정되었습니다.');
			router.push('/');
			location.reload(true);
		})
		.catch((error) => {
			alert('회원 정보 수정 중 오류가 발생했습니다. 다시 시도해주세요.');
			console.error(error);
		});
};
</script>
