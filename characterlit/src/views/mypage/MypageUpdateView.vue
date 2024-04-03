<template>
	<div class="update-profile container">
		<div class="row">
			<div class="col">
				<h2 class="form-title">회원정보 수정</h2>
				<form @submit.prevent="submitForm" class="form-body">
					<div class="form-group">
						<label for="nickname">닉네임 (Required)</label>
						<div class="input-group">
							<input
								type="text"
								id="nickname"
								v-model="loginUser.nickname"
								@input="nicknameChanged = true"
							/>
							<button
								type="button"
								:disabled="!nicknameChanged"
								@click="checkNickname"
							>
								중복 확인
							</button>
							<p
								v-if="nicknameChanged && !nicknameValidated"
								class="text-danger"
							>
								닉네임 중복 확인이 필요합니다.
							</p>
						</div>
						<p
							v-if="nicknameMessage"
							:class="{
								'text-danger': !isNicknameAvailable,
								'text-success': isNicknameAvailable,
							}"
						>
							{{ nicknameMessage }}
						</p>
					</div>
					<div class="form-group">
						<label for="email">이메일</label>
						<input type="email" id="email" v-model="loginUser.email" disabled />
					</div>
					<div class="form-group">
						<label for="name">이름</label>
						<input type="text" id="name" v-model="loginUser.name" />
					</div>
					<div class="form-group">
						<label for="phoneNumber">전화번호</label>
						<input
							type="text"
							id="phoneNumber"
							v-model="loginUser.phoneNumber"
							@input="onPhoneNumberInput"
						/>
					</div>
					<div class="form-group">
						<label for="zonecode">주소 (Required)</label>
						<div class="input-group">
							<input type="text" id="zonecode" v-model="zonecode" readonly />
							<input type="text" id="address" v-model="address" readonly />
							<button type="button" @click="findAddress">주소 찾기</button>
						</div>
						<p
							v-if="!zonecode && !address && detailAddress"
							class="text-danger"
						>
							주소를 입력해주세요.
						</p>
					</div>
					<div class="form-group">
						<label for="detailAddress">상세주소</label>
						<input type="text" id="detailAddress" v-model="detailAddress" />
					</div>

					<div class="form-actions">
						<button type="submit">제출</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</template>

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

function enforceNumeric(value) {
	return value.replace(/[^\d]/g, '');
}

function onPhoneNumberInput(event) {
	const input = event.target;
	input.value = enforceNumeric(input.value);
	loginUser.value.phoneNumber = input.value;
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

<style>
.container {
	max-width: 600px;
	margin: 50px auto;
	padding: 20px;
	border-radius: 8px;
	background-color: #fff;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.row,
.col {
	display: flex;
	flex-direction: column;
}

.form-title {
	margin-bottom: 1.5rem;
	color: #333;
	font-size: 1.5rem;
	text-align: center;
}

.form-body {
	display: flex;
	flex-direction: column;
	gap: 20px;
}

.form-group {
	display: flex;
	flex-direction: column;
}

.form-group label {
	margin-bottom: 0.5rem;
	font-weight: bold;
	color: #333;
}

.form-group input,
.form-group button {
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	font-size: 1rem;
}

.input-group {
	display: flex;
	gap: 10px;
	margin-top: 0.5rem;
}

.input-group input[readonly] {
	background-color: #f0f0f0;
	cursor: not-allowed;
}

.form-group button,
.form-actions button {
	cursor: pointer;
	background-color: #007bff;
	color: white;
	border: none;
	text-transform: uppercase;
	font-weight: bold;
	transition: background-color 0.3s ease;
}

.form-group button:disabled,
.form-actions button:disabled {
	background-color: #ccc;
}

.form-group button:hover,
.form-actions button:hover {
	background-color: #0056b3;
}

.text-danger {
	color: #dc3545;
}

.text-success {
	color: #28a745;
}

.form-actions {
	display: flex;
	justify-content: flex-end;
}

.form-actions button {
	width: 100%;
	padding: 10px 0;
	font-size: 1.1rem;
}
</style>
