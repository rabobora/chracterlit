<template>
  <div class="read-page-container">
    <header>
      <h1>헤더 자리입니다 {{ biddingStore.bidId }}</h1>
      <!-- <router-link to="/" class="home-button">HomeView로 돌아가기</router-link> -->
    </header>
    <div class="search-box">
    <div class="searchbar">
        <SearchBarView />        
      </div>
      <div class="buttonbox">
        <button class="buttons" @click="goToCreate">상품등록</button>
        <button class="buttons" @click="viewAll">전체보기</button>        
    </div>
    </div>
    <div v-if="biddingStore.showMessage" class="message-overlay">
      <div class="message-box">
        {{ biddingStore.popMessage }}
      </div>
    </div>

    <div class="category-navbar">
        <ul>
            <li v-for="category in categories" :key="category.id" @click="selectCategory(category.id)">
              <i class="fa-solid fa-paper-plane"></i>  {{ category.name }}
            </li>
        </ul>
      </div>

    <div class="content">
      <div class="item-image">
        <img v-if="biddingStore.itemDetail.thumbnail" :src="biddingStore.itemDetail.thumbnail" alt="Item image" class="image-style" />
        <div v-else class="image-placeholder"></div>
      </div>

      <div class="item-info">
        <div class="item-details">
          <h1 class="title">{{ biddingStore.itemDetail.title || 'Loading...' }}</h1>
          <div class="seller-info">
          <p class="nick-name">판매자: <span>{{ biddingStore.itemDetail.nickname ||  'Loading...'  }}</span></p>
          <button class="chat-request-button" @click="sendChatToSeller">문의하기</button>
          </div>
          <p class="bid-time">시작 시간: <span>{{ biddingStore.formattedStartDate || 'Loading...' }}</span></p>
          <p class="bid-time">종료 시간: <span>{{ biddingStore.formattedEndDate || 'Loading...' }}</span></p>
          <p class="start-price">시작가: <span class="bid-price">{{ biddingStore.itemDetail.startBidFormatted || 'Loading...' }}</span></p>
          <p class="current-price" :class="{'hidden-element': biddingStore.itemDetail.bidStatus !== 1}" >현재가: <span class="bid-price">{{ biddingStore.latestEvent.requestBidFormatted || 'Loading...' }}</span></p>
          <p class="final-price" :class="{'hidden-element': biddingStore.itemDetail.bidStatus !== 2 &&  biddingStore.itemDetail.finalBid === null }">낙찰가: <span class="bid-price">{{ biddingStore.itemDetail.finalBid }}원</span></p>
        </div>
        <div class="bid-input">
          <input type="number" v-model="biddingStore.getRequestBid" :placeholder="`${biddingStore.latestEvent.requestBidFormatted}`">
          <button @click="biddingStore.sendRequest" :disabled="!biddingStore.getIsLoggedIn || !biddingStore.getIsBidValid">{{ biddingStore.getSendRequestStatus }}</button>
        </div>
        <div class="modify-button-box">
        <button @click="deleteSelectedProduct" class="deletebutton">상품 삭제</button>
        <button @click="updateproduct" class="updatebutton">상품수정</button>
        </div>
      </div>
    </div>
    <div class="description">
      <h2>상세설명</h2>
      <div class="image-gallery">
      <img v-for="imageUrl in biddingStore.itemDetail.image" :key="imageUrl" :src="imageUrl" alt="Product Image" class="product-image"/>
    </div>
      <p>{{ biddingStore.itemDetail.content || 'Loading...' }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useProductStore } from '../../stores/product';
import { useBiddingStore } from '../../stores/bidding';
import { useUsersStore } from '@/stores/users';
import { useRoute,useRouter } from 'vue-router';
import SearchBarView from './SearchBarView.vue';
const router = useRouter();
const route = useRoute();
const productStore = useProductStore();
const biddingStore = useBiddingStore();
const usersStore = useUsersStore();
const productDetail = ref(null); 

// const isOwner = computed(() => {
//   console.log("check", biddingStore.itemDetail.userNumber, usersStore.getUserNumber);
//   return biddingStore.itemDetail.userNumber === usersStore.getUserNumber;
// });

const viewAll = () => {
  router.push({ name: 'productList' });
};
const goToCreate = () => {
  router.push({ name: 'productCreate' });
};


onMounted(async () => {
    const bidId = route.params.number;
    biddingStore.bidId = route.params.number;
    // await usersStore.getLoginUser();
await biddingStore.fetchItemDetail();
await biddingStore.loadInitialEvent();
biddingStore.subscribe();
window.addEventListener('beforeunload', biddingStore.disconnect);
    try {
        const detail = await productStore.researchProductDetail(bidId);
        if (detail) {
            productDetail.value = detail;
            console.log("추가요청");

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

const categories = [
    { id: 0, name: '피규어' },
    { id: 1, name: '인형' },
    { id: 2, name: '의류' },
    { id: 3, name: '음반/서적/블루레이' },
    { id: 4, name: '카드 및 지류' },
    { id: 5, name: '팬시/문구' },
    { id: 6, name: '생활/인테리어' },
    { id: 7, name: '기타' }    
  ];




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

.product-view {
    display: flex;
    flex-direction: column;
  }

.search-box {
  margin-bottom: 50px;
}

.searchbar {
  display: flex;
  justify-content: flex-end; /* 요소들을 오른쪽으로 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  margin-top: 3%; /* 상단 여백 */
  margin-right: 0%; /* 우측 여백 - 필요에 따라 조정 가능 */
}  

.buttonbox{
  padding-top: 10px;
    margin-top: 5px;
    margin-right: 0%;    
    text-align: right;
    
}

.buttons{
    margin-right: 10px;
    background-color: black;
    color: white;
    border-radius: 90px;
    cursor: pointer;
    width: 83px;
    height: 25px;
    text-align: center;
    
}

.category-navbar {
position: fixed; /* 고정 위치 */
top: 50%; /* 뷰포트의 중앙에 배치 */
transform: translateY(-25%); /* 요소의 중심을 정확히 뷰포트 중앙에 맞춤 */
left: 0; /* 왼쪽 가장자리에 배치 */
width: 200px; /* 네비게이션 바의 너비 */
background-color: #000; /* 검정색 배경 */
color: #fff; /* 텍스트 색상을 흰색으로 */
box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
z-index: 1000; 
margin-left: 2%;
border-radius: 2%;
}

.category-navbar ul {
  list-style: none; /* 목록 스타일 제거 */
  padding: 0; /* 내부 여백 제거 */
  margin: 0; /* 외부 여백 제거 */
}

.category-navbar li {
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
  padding: 10px; /* 패딩으로 여백 추가 */
  border-bottom: 1px solid #fff; /* 경계선 추가 */
}

.category-navbar li:last-child {
  border-bottom: none; /* 마지막 요소의 하단 경계선 제거 */
}

@media (max-width: 768px) {
  .category-navbar {
    display: none;
  }
}

@media (max-width: 1024px) {
  .category-navbar {
    position: fixed; /* 화면 줄어도 고정 위치 유지 */
    width: 200px; /* 네비게이션 바 너비 고정 */
    /* 필요한 경우 여기에 추가 스타일 속성 */
  }
}  




.deletebutton {
 background-color: brown;
 color: bisque;
 cursor: pointer;
 margin-right: 10px;
 border-radius: 15px;
}
.updatebutton{
  background-color: blueviolet;
  cursor: pointer;
  border-radius: 15px;
  
}

.read-page-container {
    max-width: 900px; /* 컨테이너의 최대 너비 */
    margin: auto; /* 중앙 정렬 */
    padding: 20px;
    font-family: 'Arial', sans-serif;
  }
  
  .item-image {
    text-align: center; /* 이미지를 중앙 정렬 */
    margin-top: 5%;
  }
  
  .image-style {
    width: 100%;
    max-width: 300px;
    height: auto;
    box-shadow: 0px 4px 4px 0px rgb(63, 62, 62);
    border-radius: 10px;
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

  .item-details p {
  min-width: 400px;
  margin-bottom: 15px;
}
  
  .title {
    font-size: 1.5em;
    color: #333;
    text-align: left;
    border-bottom: 1px solid #ccc;
    padding-bottom: 15px;
    margin-bottom: 30px;
  }

  .seller-info {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .chat-request-button {
  padding: 5px 10px;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 5px;  
  width: 83px;
  background-color: #000; /* 검은색 배경 */
  color: white;
  border: none;
  border-radius: 90px;
  cursor: pointer; /* 활성화 상태일 때 마우스 커서를 포인터로 변경 */
  transition: background-color 0.3s, box-shadow 0.3s; /* 부드러운 전환 효과 */
}
  
  .price-container {
    text-align: center; 
    margin: 20px 0;
  }
  
  .start-price, .current-price, .bid-time, .nick-name, .final-price {
    font-size: 1em;
    color: #333;
  }

  .bid-price {
    font-weight: bold;
    font-size: large;
  }

  .description {
    padding-top: 50px;
    text-align: center;
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
    display: flex;
    justify-content: space-between; /* 입력 필드와 버튼 사이의 공간을 최대로 */
    text-align: center; /* 입력 필드 중앙 정렬 */
    margin-top: 20px;
  }
  
  .bid-input input {
    flex: 1; /* 가능한 모든 공간을 차지 */
    margin-right: 10px; /* 버튼과의 간격 */
    padding: 5px;
    border: none;
    border-bottom: 2px solid #000; 
    outline: none;
  }
  
  .bid-input button {
  margin-top: 5px;  
  padding: 15px 25px;
  width: 83px;
  background-color: #000; /* 검은색 배경 */
  color: white;
  border: none;
  border-radius: 90px;
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
    margin-left: 15px;
  }

  .modify-button-box {
    margin-top: 35px;
    margin-left: 70%;    
  }

  .image-gallery {
    margin-top: 25px;
  }
  
  .hidden-element {
  visibility: hidden;
}


</style>