<!-- <div class="mb-3">
						<label for="profileImage" class="form-label">프로필 이미지</label>
						<input
							class="form-control"
							type="file"
							id="profileImage"
							ref="profileImageInput"
							@change="handleImageChange"
						/>
						<div v-if="loginUser.profileImage" class="mt-2">
							<img
								:src="loginUser.profileImage"
								alt="Profile Image Preview"
								class="img-thumbnail"
								width="100"
							/>
							<button
								type="button"
								class="btn btn-danger ms-2"
								@click="deleteImage"
							>
								삭제
							</button>
						</div>
					</div> -->
<template>
	<div class="update-profile container my-5">
		<div class="row">
			<div class="col-lg-6 offset-lg-3">
				<h2 class="mb-4 text-start">회원정보 수정</h2>
				<form @submit.prevent="submitForm">
					<div class="mb-3">
						<label for="nickname" class="form-label">닉네임</label>
						<div class="input-group">
							<input
								type="text"
								class="form-control"
								id="nickname"
								v-model="loginUser.nickname"
							/>
							<button
								class="btn btn-outline-secondary"
								type="button"
								@click="checkNickname"
							>
								중복 확인
							</button>
						</div>
					</div>
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
					<div class="mb-3">
						<label for="name" class="form-label">이름</label>
						<input
							type="text"
							class="form-control"
							id="name"
							v-model="loginUser.name"
						/>
					</div>
					<div class="mb-3">
						<label for="phone" class="form-label">전화번호</label>
						<input
							type="tel"
							class="form-control"
							id="phone"
							v-model="loginUser.phone"
						/>
					</div>
					<div class="mb-3">
						<label for="address" class="form-label">주소</label>
						<div class="input-group">
							<input
								type="text"
								class="form-control"
								id="address"
								v-model="loginUser.address"
							/>
							<button
								class="btn btn-outline-secondary"
								type="button"
								@click="findAddress"
							>
								주소 찾기
							</button>
						</div>
					</div>
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

<script setup>
import { ref, onMounted } from 'vue';
import { useUsersStore } from '@/stores/users';
import router from '@/router';
const usersStore = useUsersStore();

const loginUser = ref({
	userNumber: '',
	nickname: '',
	email: '',
	name: '',
	phone: '',
	address: '',
	// profileImage: '',
});

onMounted(async () => {
	await usersStore.fetchLoginUser();
	const user = usersStore.getLoginUser;
	loginUser.value.userNumber = user.userNumber;
	loginUser.value.nickname = user.nickname;
	loginUser.value.email = user.email;
	loginUser.value.name = user.name;
	loginUser.value.phone = user.phone;
	loginUser.value.address = user.address;
});

// const profileImageInput = ref(null); // 파일 입력 필드를 위한 ref 추가

// function handleImageChange(event) {
// 	const file = event.target.files[0];
// 	form.value.profileImage = URL.createObjectURL(file);
// }

// function deleteImage() {
// 	form.value.profileImage = ''; // 프로필 이미지 삭제
// 	if (profileImageInput.value) {
// 		profileImageInput.value.value = ''; // 파일 입력 필드 초기화
// 	}
// }

function checkNickname() {
	console.log('닉네임 중복 체크:', form.value.nickname);
	// 닉네임 중복 체크
}

function findAddress() {
	console.log('주소 찾기 기능 실행');
	// 주소 찾기
}

// 사용자 정보 수정 폼 제출 처리
const submitForm = () => {
	usersStore
		.updateLoginUser(loginUser.value)
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
