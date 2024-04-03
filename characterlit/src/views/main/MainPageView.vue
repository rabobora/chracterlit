<template>
    <header>
        <TheHeader />
    </header>
    <div class="searchandword">
        <div class="fadecomponents">
            <div class="catchpri">
                <h1>세상에 있는 모든 굿즈,<br />캐릭터릿</h1>
            </div>
            <div class="searchbar">
                <SearchBarView />
            </div>
            <div class="gotoitemlist">
                <p @click="gotoproductlist" class="productlist">시작하기</p>
            </div>
        </div>
    </div>

    <div class="top3-container" :class="{ 'visible': top3ContainerVisible }">
        <div>
            <h1 class="ranktitle">현재 인기 있는 상품들</h1>
        </div>
        <div class="items-container">
            <!-- 아이템을 가로로 나열하기 위한 새로운 컨테이너 -->
            <div
                @click="navigateToItem(product.bidId)"
                class="item"
                v-for="(product, index) in productStore.gettop3ProductList"
                :key="product.bidId"
            >
                <div class="rank">{{ index + 1 }}위</div>
                <img class="thumbnail" :src="product.thumbnail" alt="thumbnail" />
                <div class="title">{{ product.title }}</div>
            </div>
        </div>

        <div @click="gotoproductlist" class="moreproduct">
            더 많은 상품이 궁금하다면?
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useProductStore } from '../../stores/product';
import SearchBarView from '../product/SearchBarView.vue';

import { useRoute, useRouter } from 'vue-router';
import TheHeader from '@/components/common/TheHeader.vue';
const router = useRouter();
const route = useRoute();
const productStore = useProductStore();
const top3ContainerVisible = ref(false);

onMounted(() => {
    productStore.researchTop3Product();

    const observer = new IntersectionObserver(
        (entries) => {
            entries.forEach((entry) => {
                // 요소가 뷰포트에 들어오면 true, 아니면 false로 설정
                top3ContainerVisible.value = entry.isIntersecting;
            });
        },
        { threshold: 0.5 },
    );

    const top3ContainerEl = document.querySelector('.top3-container');
    observer.observe(top3ContainerEl);
});

const gotoproductlist = () => {
    router.push({ name: 'productList' });
};

const navigateToItem = (bidId) => {
    router.push({ name: 'ReadView', params: { number: bidId } });
};
</script>

<style scoped>
.fadecomponents {
}

@keyframes fade-in {
    from {
        opacity: 0;
    }
    to {
        opacity: 1;
    }
}

.fadecomponents > div {
    animation: fade-in 2.5s ease-out 0.5s;
    animation-fill-mode: forwards;
}

.productlist {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 10px 20px;
    background-color: black;
    border: 2px solid #dcdcdc;
    border-radius: 5px;
    cursor: pointer;
    text-decoration: none;
    color: white;
    transition: background-color 0.3s, color 0.3s;
    height: 50px;
    width: 100px;
    font-size: x-large;
    font-weight: bold;
    margin-left: 38%;
}

.productlist:hover {
    background-color: #0056b3;
    color: white;
}

.searchandword {
    background-image: url('../../pictures/깃털.png');
    background-size: auto;
    background-repeat: no-repeat;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    height: 90vh;
    padding-top: 7%;
    margin-top: -5%;
}

.catchpri {
    margin-left: -2%;
    width: 110%;
    text-align: center;
    font-size: x-large;
}
.searchbar {
    display: flex;
    align-items: center;
    margin-top: 1%;
    text-align: center;
    width: 500px;
    margin-left: 3%;
    justify-content: center;
}
.gotoitemlist {
    margin-top: 10%;
    text-align: center;
}

.items-container {
    display: flex;
    justify-content: center;
    gap: 20px;
}

.top3-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px;
    background-color: #3e78e5;
    height: 100vh;
}

.item {
    width: 250px;
    height: 300px;
    border-radius: 5px;
    overflow: hidden;
    background: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 15px;
    cursor: pointer;
}

.item:hover {
    background-color: gainsboro;
}

.title {
    margin-top: 8%;
    font-size: 22px;
    font-weight: bold;
}

.thumbnail {
    width: 200px;
    height: 200px;
    /* object-fit: contain;  */
    border-radius: 5px;
}
.rank {
    margin-bottom: 3%;
    font-size: 30px;
    font-weight: bolder;
}
.ranktitle {
    font-size: 41px;
    color: #ffffff;
    margin-bottom: 12%;
    text-align: center;
}

.top3-container {
    opacity: 0;
    transform: translateY(20px);
    transition: opacity 0.5s ease-out, transform 0.5s ease-out;
}

.top3-container.visible {
    opacity: 1;
    transform: translateY(0);
}

@keyframes burning {
    0% {
        opacity: 1;
    }
    50% {
        opacity: 0.5;
    }
    100% {
        opacity: 1;
    }
}
.moreproduct {
    margin-top: 5%;
    font-size: 25px;
    color: #ffffff;
    margin-left: 40%;
    cursor: pointer;
    animation: burning 1s infinite;
}
.moreproduct:hover {
    color: yellow;
    animation: burning 3s infinite;
}
</style>