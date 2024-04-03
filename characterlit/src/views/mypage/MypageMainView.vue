// MypageMainView.vue

<template>
    <div class="mypage-container">
        <div class="point-section">
            <div class="mypage-welcome">
                <h3>{{ userStore.getLoginUser.nickname }}님, 좋은 하루 되세요!</h3>
            </div>
            <div class="mypage-point">
                <div class="point-title">
                    <h2 class="point-name">포인트</h2>
                    <div>
                        <button @click="$router.push('/charge')" class="point-button">
                            충전하기
                        </button>
                        <button @click="$router.push('/withdraw')" class="point-button">
                            출금하기
                        </button>
                    </div>
                </div>
                <hr />
                <div class="point-first">
                    <h4>보유 포인트</h4>
                    <h4>{{ payStore.userPoint.allPoint }} p</h4>
                </div>
                <div class="point-second">
                    <h4>사용 가능 포인트</h4>
                    <h4>{{ payStore.userPoint.usablePoint }} p</h4>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { usePayStore } from '@/stores/pay';
import { useUsersStore } from '@/stores/users';
import { onMounted } from 'vue';

const payStore = usePayStore();
const userStore = useUsersStore();

onMounted(() => {
    payStore.pointCheck();
    // userStore.getLoginUser();
});
</script>

<style>
.mypage-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 20px;
}

.mypage-welcome,
.mypage-point {
    background-color: #f5f5f5;
    border-radius: 10px;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
    width: 100vh;
}

.mypage-welcome {
    text-align: center;
}

/* .point-section {
    background-color: #f9f9f9;
    border-radius: 10px;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
} */

.point-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.point-button {
    background-color: #000;
    color: white;
    border: none;
    border-radius: 5px;
    padding: 10px 10px;
    margin-left: 10px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.point-button:hover {
    background-color: #0056b3;
}

hr {
    border: 0;
    border-top: 1px solid #ccc;
    margin: 20px 0;
}

.point-first,
.point-second {
    display: flex;
    justify-content: space-between;
}

.link-section {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.mypage-link {
    background-color: #f9f9f9;
    padding: 10px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    transition: background-color 0.3s ease;
}

.mypage-link:hover {
    background-color: #e6e6e6;
}

.link-icon {
    font-size: 20px;
    margin-right: 10px;
}

.link-section {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.mypage-link {
    color: white;
    text-decoration: none;
}

.mypage-link:hover {
    text-decoration: none;
}

.link-icon {
    font-size: 20px;
    margin-right: 10px;
}
</style>