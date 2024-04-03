<template>
    <div class="product-view">
      
      <header>
        <TheHeader/>
      </header>
      <div class="searchbar">
        <SearchBarView />
        
      </div>
      <div class="buttonbox">
        <button v-if="accessToken" class="buttons" @click="goToCreate">상품등록</button>
        <button class="buttons" @click="viewAll">전체보기</button>
        
        <select v-model="selectedSortOption" @change="applySort" class="buttons">
            <option value="newest">최신순</option>
            <option value="oldest">오래된순</option>            
        </select>
    </div>            
     
      <div class="category-navbar">
        <ul>
            <li v-for="category in categories" :key="category.id" @click="selectCategory(category.id)">
              <i class="fa-solid fa-paper-plane"></i>  {{ category.name }}
            </li>
        </ul>
      </div>
      
    
    <div class="product-list">
      <div class="product-card"
          v-for="product in paginatedProducts"
          :key="product.bid_index"
          @click="goToProductDetail(product.bidId)">
            <img :src="product.thumbnail" alt="상품 이미지" class="product-image">
            <div class="product-info">
              <h1 class="product-title">{{ product.title }}</h1>            
              <p class="product-status" :class="getStatusClass(product.bidStatus)">
                  {{ getAuctionStatusText(product.bidStatus) }}
              </p>
              <p class="product-price">시작가: ₩ {{ product.startBid.toLocaleString() }}</p>
            </div>
        </div>
      </div>
    </div>   

    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">이전</button>
      <span>페이지 {{ currentPage }} / {{ totalPage }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPage">다음</button>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted,computed } from 'vue';
  import { useProductStore } from '../../stores/product';
  import SearchBarView from './SearchBarView.vue';
  import router from '@/router';
  import TheHeader from '@/components/common/TheHeader.vue';
  const productStore = useProductStore();
  const selectedSortOption = ref('newest');
  const accessToken = localStorage.getItem('access-token')

  onMounted(() => {
    productStore.researchAllProduct();    
    applySort();
  });

  const viewAll = ()=> {
    window.location.reload()
  }

  const goToProductDetail = (bidId) => {
    router.push({ name: 'ReadView', params: { number: bidId } });
};

  
  const categories = [
    { id: 0, name: '　피규어' },
    { id: 1, name: '　인형' },
    { id: 2, name: '　의류' },
    { id: 3, name: '　음반/서적/블루레이' },
    { id: 4, name: '　카드 및 지류' },
    { id: 5, name: '　팬시/문구' },
    { id: 6, name: '　생활/인테리어' },
    { id: 7, name: '　기타' }    
  ];

  const getAuctionStatusText = (bidStatus) => {
    switch (bidStatus) {
    case 1: return '경매 중';
    case 2: return '경매 종료';
    default: return '경매 전';
    }
}; 

const getStatusClass = (bidStatus) => {
  switch (bidStatus) {
    case 0: return 'status-pre-auction';
    case 1: return 'status-in-auction';
    case 2: return 'status-post-auction';
    default: return '';
  }
};

  
  
  const selectCategory = (categoryId) => {
    productStore.researchCategory(categoryId);
  };
  

  const applySort = () => {
  if (selectedSortOption.value === 'newest') {
    productStore.setSortOrder('bidId', 'desc');
  } else if (selectedSortOption.value === 'oldest') {
    productStore.setSortOrder('bidId', 'asc');
  } else {
    // 상태별 필터링
    const status = parseInt(selectedSortOption.value);
    productStore.filterByBidStatus(status);
  }
};


const goToCreate = () => {
  router.push({ name: 'productCreate' });
};

const itemsPerPage = 6; // 한 페이지 당 6개의 상품
const currentPage = ref(1);

// 전체 페이지 수를 계산합니다.
const totalPage = computed(() => {
  return Math.ceil(productStore.getSortedProductList.length / itemsPerPage);
});

// 현재 페이지에 표시할 상품을 계산합니다.
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return productStore.getSortedProductList.slice(start, end);
});

// 페이지 변경 함수
const changePage = (page) => {
  if (page >= 1 && page <= totalPage.value) {
    currentPage.value = page;
    window.scrollTo(0, 0);
  }
};
  </script>
  
  <style scoped>
  .product-view {
    display: flex;
    flex-direction: column;
   
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

.category-navbar li:hover {
    opacity: 0.7;
    color: aqua;
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

  
.product-list {
  margin-top: 3%;
  margin-left: calc(280px + 2%); 
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: flex-start; 
  align-content: flex-start;  
  padding-right: 2%; 
}

.product-card {
  width: calc(33.3333% - 16px - 2%); 
  margin: 8px 0; 
  cursor: pointer;
}

@media (max-width: 768px) {
  .category-navbar {
    display: none;
  }

  .product-list {
    margin-left: 2%; 
    justify-content: center; 
  }

  .product-card {
    width: calc(50% - 16px);
  }
}

@media (max-width: 1024px) {
  .category-navbar {
    position: fixed; 
    width: 200px;
   
  }
}  
  
  .product-image {  
    height: 320px;
    width: 270px;
    display: block;
    margin-bottom: 16px;
    text-align: center;
    box-shadow: 0px 4px 4px 0px rgb(63, 62, 62);
    border-radius: 10px;
  }

  .product-image:hover {
    opacity: 0.7;
}
  
  .product-info {
    text-align: center;
    text-align: left;
  }
  
  .product-title {
    font-size: 1.5em;
    margin: 0;
    text-align: left;
  }
  
  .product-price {
    color: #333;
    text-align: left;
    font-weight: bold;
    
  }
  
  .product-status {
    color: #666;    
  }

  .status-pre-auction {
  color: #666; 
}

.status-in-auction {
  color: green; 
}

.status-post-auction {
  color: red; 
}

.searchbar {
display: flex;
justify-content: flex-end; 
align-items: center; 
margin-top: -2%;
margin-right: 13%; 
}
.buttonbox{
  padding-top: 10px;
    margin-top: 10px;
    margin-right: 13%;    
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

.whiteicon{
  color: azure;
}


.pagination {
  display: flex;
  justify-content: center; 
  padding: 20px;
}

.pagination button {
  margin: 0 10px;
  background-color: black;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  height: 30px;
  width: 67px;
  display: flex;
  justify-content: center; 
  align-items: center;
  text-align: center;
  border-radius: 5px
}

.pagination button:disabled {
  background-color: grey;
  cursor: not-allowed;
}

  </style>
  