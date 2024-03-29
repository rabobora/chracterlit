<template>
  <div class="read-page-container">
    <div class="header">
      <h1>This is Read Page {{ biddingStore.bidId }}</h1>
      <router-link to="/" class="home-button">HomeView로 돌아가기</router-link>
    </div>
    <div v-if="biddingStore.showMessage" class="message-overlay">
      <div class="message-box">
        {{ biddingStore.popMessage }}
      </div>
    </div>
    <div class="content">
      <div class="item-image">
        <!-- 이미지가 있을 때 -->
        <img v-if="biddingStore.itemDetail.thumbnail" :src="biddingStore.itemDetail.thumbnail" alt="Item image" class="image-style" />
        <!-- 이미지가 없을 때 -->
        <div v-else class="image-placeholder"></div>
      </div>

      <div class="item-info">
        <div class="item-details">
          <h1 class="title">{{ biddingStore.itemDetail.title || 'Loading...' }}</h1>
          <p class="start-price">시작가: <span>{{ biddingStore.itemDetail.startBidFormatted || 'Loading...' }}</span></p>
          <p class="current-price">현재가: <span>{{ biddingStore.latestEvent.requestBidFormatted || 'Loading...' }}</span></p>
        </div>
        <div class="bid-input">
          <input type="number" v-model="biddingStore.getRequestBid" placeholder="입찰가를 입력해주세요">
          <button @click="biddingStore.sendRequest" :disabled="!biddingStore.getIsLoggedIn || !biddingStore.getIsBidValid">{{ biddingStore.getSendRequestStatus }}</button>
        </div>
        <button @click="deleteSelectedProduct" class="deletebutton">상품 삭제</button>
        <button @click="updateproduct" class="updatebutton">상품수정</button>
      </div>
    </div>
    <div class="description">
      <h2>상세설명</h2>
      <p>{{ biddingStore.itemDetail.content || 'Loading...' }}</p>
    </div>
    <!-- <p>{{biddingStore.getIsBidValid}}</p>
    <p>{{ biddingStore.isLoggedIn }}</p>
    <p>{{ biddingStore.getIsLoggedIn }}</p>
    <p>{{ biddingStore.getSendRequestStatus }}</p> -->
  </div>





  <div class="product-detail" v-if="productDetail">    
    
    {{ productDetail }}
    <div class="image-gallery">
      <img v-for="imageUrl in productDetail.image" :key="imageUrl" :src="imageUrl" alt="Product Image" class="product-image"/>
    </div>

    <!-- <img :src="productDetail.thumbnail" alt="상품 이미지"> -->
    <button @click="deleteSelectedProduct" class="deletebutton">상품 삭제</button>
    <button @click="updateproduct" class="updatebutton">상품수정</button>
    
  </div>
  <div v-else>    
    Loading...
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useProductStore } from '../../stores/product';
import { useBiddingStore } from '../../stores/bidding';
import { useRoute,useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();
const productStore = useProductStore();
const biddingStore = useBiddingStore();
const productDetail = ref(null); 

onMounted(async () => {
    const bidId = route.params.number;
    biddingStore.bidId = route.params.number;
await biddingStore.fetchItemDetail();
await biddingStore.loadInitialEvent();
biddingStore.subscribe();
window.addEventListener('beforeunload', biddingStore.disconnect);
    try {
        const detail = await productStore.researchProductDetail(bidId);
        if (detail) {
            productDetail.value = detail;


        } else {
            console.error('No product:', bidId);
        }
    } catch (error) {
        console.error('Error:', error);
    }
});

onBeforeUnmount(() => {
  biddingStore.disconnect();
  window.removeEventListener('beforeunload', biddingStore.disconnect);
});


// 선택한 상품 삭제 메소드
const deleteSelectedProduct = async () => {
    const bidId = route.params.number;
    try {
        await productStore.deleteProduct(bidId);        
        
    } catch (error) {
        console.error('Failed to delete product:', error);
    }
    
};

// 상품 수정 버튼 클릭 시
const updateproduct = () => {
  if (productDetail.value && productDetail.value.bidId) {
    // 상품 ID를 사용하여 라우트 이동
    router.push({ name: 'productUpdate', params: { bidId: productDetail.value.bidId } });
  } else {
    console.error('상품 정보가 없습니다.');
  }
};
</script>


<style scoped>
.deletebutton {
 background-color: brown;
 color: bisque;
 cursor: pointer;
}
.updatebutton{
  background-color: blueviolet;
  cursor: pointer;
}

.read-page-container {
    max-width: 800px; /* 컨테이너의 최대 너비 */
    margin: auto; /* 중앙 정렬 */
    padding: 20px;
    font-family: 'Arial', sans-serif;
  }
  
  .item-image {
    text-align: center; /* 이미지를 중앙 정렬 */
  }
  
  .image-style {
    width: 100%;
    max-width: 300px;
    height: auto;
  }
  
  .image-placeholder {
    width: 300px;
    height: 400px;
    background-color: #f3f3f3;
    display: inline-block;
  }
  
  .item-details {
    margin-top: 20px;
  }
  
  .title {
    font-size: 1.5em;
    color: #333;
    text-align: center; /* 타이틀 중앙 정렬 */
  }
  
  .price-container {
    text-align: center; /* 가격 정보 중앙 정렬 */
    margin: 20px 0;
  }
  
  .start-price, .current-price {
    font-size: 1em;
    color: #666;
  }
  
  .description h2 {
    font-size: 1.2em;
    border-top: 1px solid #ccc;
    padding-top: 10px;
  }
  
  .description p {
    font-size: 1em;
    color: #666;
  }
  
  .bid-input {
    text-align: center; /* 입력 필드 중앙 정렬 */
    margin-top: 20px;
  }
  
  .bid-input input {
    padding: 5px;
    margin-right: 10px;
  }
  
  .bid-input button {
  padding: 5px 15px;
  background-color: #000; /* 검은색 배경 */
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer; /* 활성화 상태일 때 마우스 커서를 포인터로 변경 */
  transition: background-color 0.3s, box-shadow 0.3s; /* 부드러운 전환 효과 */
}

.bid-input button:hover {
  background-color: #333; /* 호버 상태의 배경색은 더 어두운 톤의 검은색 */
  box-shadow: 0 0 15px rgba(255, 255, 255, 0.6); /* 빛나는 효과 */
}

/* 비활성화된 버튼 스타일 */
.bid-input button:disabled {
  background-color: #cccccc; /* 비활성화 상태의 배경색 */
  color: #666666; /* 비활성화 상태의 텍스트 색상 */
  cursor: not-allowed; /* 비활성화 상태일 때 마우스 커서를 not-allowed로 변경 */
}
  
  .home-button {
    display: inline-block;
    margin-top: 20px;
    padding: 10px 20px;
    color: #fff;
    background-color: #007bff;
    text-decoration: none;
    border-radius: 5px;
  }
  
  .home-button:hover {
    background-color: #0056b3;
  }
  
  .message-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    animation: fadeInOut 4s;
    pointer-events: none; /* 오버레이에서 마우스 이벤트 무시 */
  }
  
  .message-box {
    padding: 20px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    pointer-events: auto; /* 메시지 박스에서는 마우스 이벤트 활성화 */
  }
  
  /* 페이드 인 아웃 애니메이션 */
  @keyframes fadeInOut {
    0%, 100% {
      opacity: 0;
      visibility: hidden;
    }
    10%, 90% {
      opacity: 1;
      visibility: visible;
    }
  }
  .content {
    display: flex;
    align-items: flex-start; /* 이미지와 내용을 위로 정렬 */
    gap: 20px; /* 간격 추가 */
  }
  
  .item-info {
    flex: 1; /* 나머지 공간을 모두 차지 */
    display: flex;
    flex-direction: column; /* 세로 방향으로 스택 */
    justify-content: space-between; /* 시작가와 현재가, 입력 필드를 분리 */
  }
  
  .bid-input {
    display: flex;
    justify-content: space-between; /* 입력 필드와 버튼 사이의 공간을 최대로 */
  }
  
  .bid-input input {
    flex: 1; /* 가능한 모든 공간을 차지 */
    margin-right: 10px; /* 버튼과의 간격 */
  }


</style>