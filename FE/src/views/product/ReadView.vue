<template>
  <header>
    <TheHeader/>
  </header>
  <div class="read-page-container">
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

    <!-- <div class="category-navbar">
        <ul>
            <li v-for="category in categories" :key="category.id" @click="selectCategory(category.id)">
              <i class="fa-solid fa-paper-plane"></i>  {{ category.name }}
            </li>
        </ul>
      </div> -->

    <div class="content">
      <div class="item-image">
        <img v-if="biddingStore.itemDetail.thumbnail" :src="biddingStore.itemDetail.thumbnail" alt="Item image" class="image-style" />
        <div v-else class="image-placeholder"></div>
      </div>

      <div class="item-info">
        <div class="item-details">
          <h1 class="title">{{ biddingStore.itemDetail.title || 'Loading...' }}</h1>        
          <div>
            <div class="seller-info">
            <div  class="nick-name">
              판매자
            </div>
            
            <div  class="usernickname">
              {{ biddingStore.itemDetail.nickname ||  'Loading...'  }}
            </div>
            <button class="chat-request-button" :class="{'hidden-element': isOwner}" @click="sendChatToSeller">문의하기</button>
            </div>
            <div class = "margins">
            <p class="bid-time">시작 시간 <span class="bidtimevalue">{{ biddingStore.formattedStartDate || 'Loading...' }}</span></p>
            </div>
            <div class = "margins">
            <p class="bid-time">종료 시간 <span class="bidtimevalue">{{ biddingStore.formattedEndDate || 'Loading...' }}</span></p>
           </div>
           <div class = "margins">
            <p class="start-price">시작가  <span class="bid-price bidtimevalue2">{{ biddingStore.itemDetail.startBidFormatted || 'Loading...' }}</span></p>
          </div>
            <div>
          </div>
            <p v-if="biddingStore.itemDetail.bidStatus === 1" class="current-price" :class="{'hidden-element': biddingStore.itemDetail.bidStatus !== 1}" >현재가 <span class="bid-price bidtimevalue2">{{ biddingStore.latestEvent.requestBidFormatted || 'Loading...' }}</span></p>
            <p v-else-if="biddingStore.itemDetail.bidStatus === 2" class="final-price" :class="{'hidden-element': biddingStore.itemDetail.bidStatus !== 2 &&  biddingStore.itemDetail.finalBid === null }">낙찰가 <span class="bid-price bidtimevalue2">{{ biddingStore.itemDetail.finalBid ? biddingStore.itemDetail.finalBid + '원' : '유찰' }}</span></p>
            <p v-else-if="biddingStore.itemDetail.bidStatus === 0" class="wait-price"><span class="bid-price bidtimevalue2">경매가 아직 열리지 않았습니다.</span></p>
          </div>
        </div>
        <div class="bid-input">
          <input type="number" v-model="biddingStore.getRequestBid" :placeholder="`${biddingStore.latestEvent.requestBidFormatted || ''}`" :disabled="biddingStore.itemDetail.bidStatus !== 1">
          <button @click="biddingStore.sendRequest" :disabled="!biddingStore.getIsLoggedIn || !biddingStore.getIsBidValid || isOwner">{{ biddingStore.getSendRequestStatus }}</button>
        </div>
        <div class="modify-button-box" :class="{'hidden-element': !isOwner || biddingStore.itemDetail.bidStatus !== 0}">
        <button @click="deleteSelectedProduct" class="deletebutton">상품 삭제</button>
        <button @click="updateproduct" class="updatebutton">상품 수정</button>
        </div>
      </div>
    </div>
    <div class="description" style="white-space: pre-wrap;">
      <h1>상세설명</h1>
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
import TheHeader from '@/components/common/TheHeader.vue';
const router = useRouter();
const route = useRoute();
const productStore = useProductStore();
const biddingStore = useBiddingStore();
const usersStore = useUsersStore();
const productDetail = ref(null); 

const isOwner = computed(() => {
  return biddingStore.itemDetail.userNumber === usersStore.loginUser.userNumber;
});

const viewAll = () => {
  router.push({ name: 'productList' });
};
const goToCreate = () => {
  router.push({ name: 'productCreate' });
};


onMounted(async () => {
    const bidId = route.params.number;
    biddingStore.bidId = route.params.number;
    await usersStore.fetchLoginUser();
await biddingStore.fetchItemDetail();

try {
  const detail = await productStore.researchProductDetail(bidId);
  if (detail) {
    productDetail.value = detail;
    console.log("추가요청");
    
  } else {
    console.error('No product:', bidId);
  }
  if(biddingStore.itemDetail.bidStatus === 1){
    await biddingStore.loadInitialEvent();
    biddingStore.subscribe();
    window.addEventListener('beforeunload', biddingStore.disconnect);    
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

// 채팅방 생성
const sendChatToSeller = () => {
  if(usersStore.loginUser.userNumber === null){
    return
  }
  const bidIdQuery = biddingStore.itemDetail.bidId;
  router.push({ name: 'chatPage', params: { bidId : bidIdQuery } })
  console.log(biddingStore.itemDetail.bidId);
};
</script>


<style scoped>
.usernickname{
  margin-right: 275px;
  font-weight: bolder;
}

.product-view {
    display: flex;
    flex-direction: column;
  }

.search-box {
  margin-bottom: 50px;
}

.searchbar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 3%;
  margin-right: 0%;
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

.buttons:hover {
    opacity: 0.7;
}

.category-navbar {
position: fixed; 
top: 50%;
transform: translateY(-25%);
left: 0;
width: 200px;
background-color: #000;
color: #fff; 
box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2); 
z-index: 1000; 
margin-left: 2%;
border-radius: 2%;
}

.category-navbar ul {
  list-style: none; 
  padding: 0; 
  margin: 0;
}

.category-navbar li {
  cursor: pointer; 
  padding: 10px; 
  border-bottom: 1px solid #fff; 
}

.category-navbar li:last-child {
  border-bottom: none; 
}

@media (max-width: 768px) {
  .category-navbar {
    display: none;
  }
}

@media (max-width: 1024px) {
  .category-navbar {
    position: fixed; 
    width: 200px;     
  }
}  

.deletebutton {
 background-color: rgb(161, 0, 0);
 color: rgb(255, 255, 255);
 cursor: pointer;
 margin-right: 10px;
 border-radius: 15px; 
 width: 83px;
}
.updatebutton{
  background-color: rgb(0, 0, 0);
  color: rgb(255, 255, 255);
  cursor: pointer;
  border-radius: 15px;
  width: 83px;  
}

.read-page-container {
    max-width: 900px; 
    margin: auto;
    padding: 20px;
    font-family: 'Arial', sans-serif;
    margin-top: -5%;
  }
  
  .item-image {
    text-align: center; 
    margin-top: 5%;
  }
  
  .image-style {
    width: 100%;
    max-width: 300px;
    height: 400px;
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
    font-size: large;   
    font-family: 'Toss Product Sans';    
  
  }

  .item-details p {
  min-width: 400px;
  margin-bottom: 15px;
}
  
  .title {
    font-size: 1.5em;
    font-weight: 900;
    color: #333;
    text-align: left;
    /* border-bottom: 1px solid #ccc; */
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
  margin-right: 20px;  
  width: 83px;
  background-color: #000;
  color: white;
  border: none;
  border-radius: 90px;
  cursor: pointer; 
  transition: background-color 0.3s, box-shadow 0.3s;
}

.chat-request-button:hover {
    opacity: 0.7;
}
  
  .price-container {
    text-align: center; 
    margin: 20px 0;
  }
  
  .start-price, .current-price, .bid-time, .nick-name,.usernickname, .final-price {
    font-size: 1em;
    color: #333;
    margin-left: 20px;
    margin-bottom: 20px;
  }

  .wait-price {
    padding-top: 2px;
    font-size: 1em;
    color: #b4b4b4;
  }

  .bid-price {
    font-weight: bold;
    font-size: large;
  }

  .description {
    padding-top: 50px;
    text-align: center;
  }
  
  .description h1 {
    border-top: 3px solid #333;
    padding-top: 10px;
  }
  
  .description p {
    font-size: 1em;
    color: #666;
  }

  .product-image {
    max-width: 480px;
    margin-bottom: 15px;
  }
  
  .bid-input {
    display: flex;
    justify-content: space-between;
    text-align: center; 
    margin-top: 20px;
  }
  
  .bid-input input {
    flex: 1; 
    margin-right: 10px; 
    padding: 5px;
    border: none;
    border-bottom: 2px solid #000; 
    outline: none;
  }
  
  .bid-input button {
  margin-top: 5px;  
  padding: 5px 25px;
  width: 83px;
  background-color: #000; 
  color: white;
  border: none;
  border-radius: 90px;
  cursor: pointer; 
  transition: background-color 0.3s, box-shadow 0.3s; 
}

.bid-input button:hover {
  background-color: #333; 
  box-shadow: 0 0 15px rgba(255, 255, 255, 0.6); 
}

.bid-input button:disabled {
  background-color: #cccccc; 
  color: #666666; 
  cursor: not-allowed; 
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
    pointer-events: none; 
  }
  
  .message-box {
    padding: 20px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    pointer-events: auto;
  }
  
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
    align-items: flex-start; 
    gap: 20px; 
  }
  
  .item-info {
    flex: 1; 
    display: flex;
    flex-direction: column;
    justify-content: space-between; 
    margin-left: 15px;
  }

  .modify-button-box {
    margin-top: 35px;
    margin-left: 64.5%;
  }

  .image-gallery {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 25px;
  }
  
  .hidden-element {
  visibility: hidden;
}
.font-all{    
    font-family: 'Toss Product Sans';    
  }

  .bidtimevalue{
    margin-left: 25px;
    font-weight: bolder;
  }
  .bidtimevalue2{
    margin-left: 45px;
    font-weight: bolder;
  }

  .margins{
    margin-bottom: 25px;
  }

  

</style>