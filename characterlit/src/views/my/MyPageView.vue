<template>
    <div class="product-view">
      
      <header>
        <h1>헤더가 들어갈자리</h1>
      </header>
      <div class="searchbar">
        <SearchBarView />
        
      </div>
      <div class="buttonbox">
        <button class="buttons" @click="goToCreate">상품등록</button>
        <button class="buttons" @click="viewAll">전체보기</button>
    </div>     
  
      <!-- 카테고리 네비게이션 바 -->
      <div class="category-navbar">
        <ul>
            <li v-for="myPage in myPagelist" :key="myPage.id" @click="selectCategory(category.id)">
              <i class="fa-solid fa-paper-plane"></i>  {{ category.name }}
            </li>
        </ul>
      </div>
      
      <!-- 상품 목록 -->
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
            <p class="product-price">시작가: ₩ {{ product.startBid }}</p>
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
  const productStore = useProductStore();
  const selectedSortOption = ref('newest');

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

  
  const myPagelist = [
    { id: 0, name: '내 정보' },
    { id: 1, name: '입찰 내역' },
    { id: 2, name: '판매 내역' },
    { id: 3, name: '페이 내역' },   
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
  }
};
  </script>
  
  <style scoped>
  .product-view {
    display: flex;
    flex-direction: column;
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

  
.product-list {
  margin-top: 3%;
  margin-left: calc(280px + 2%); /* 네비게이션 바 너비와 간격 조정 */
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: flex-start; /* 시작점에서 정렬 */
  align-content: flex-start; /* 여러 줄 정렬시 상단부터 시작 */
  padding-right: 2%; /* 오른쪽 여백 추가 */
}

.product-card {
  width: calc(33.3333% - 16px - 2%); /* 너비 조정, 추가된 margin-left 고려 */
  margin: 8px 0; /* 상단과 하단에만 마진을 추가 */
  cursor: pointer;
}

@media (max-width: 768px) {
  .category-navbar {
    display: none;
  }

  .product-list {
    margin-left: 2%; /* 좌측 여백 추가 */
    justify-content: center; /* 중앙 정렬로 변경 */
  }

  .product-card {
    width: calc(50% - 16px); /* 모바일 뷰에서 카드의 너비를 두 개씩 나오게 조정 */
  }
}

@media (max-width: 1024px) {
  .category-navbar {
    position: fixed; /* 화면 줄어도 고정 위치 유지 */
    width: 200px; /* 네비게이션 바 너비 고정 */
    /* 필요한 경우 여기에 추가 스타일 속성 */
  }
}  
  
  .product-image {
    /* max-width: 100%; */
    height: 320px;
    width: 270px;
    display: block;
    margin-bottom: 16px;
    text-align: center;
    box-shadow: 0px 4px 4px 0px rgb(63, 62, 62);
    border-radius: 10px;
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
  color: #666; /* 회색 */
}

.status-in-auction {
  color: green; /* 초록색 */
}

.status-post-auction {
  color: red; /* 빨간색 */
}

  .searchbar {
  display: flex;
  justify-content: flex-end; /* 요소들을 오른쪽으로 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  margin-top: 3%; /* 상단 여백 */
  margin-right: 13%; /* 우측 여백 - 필요에 따라 조정 가능 */
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
.whiteicon{
  color: azure;
}


.pagination {
  display: flex;
  justify-content: center; /* 중앙 정렬 */
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
  display: flex; /* Flexbox를 활성화합니다 */
  justify-content: center; /* 수평 정렬을 중앙으로 설정합니다 */
  align-items: center; /* 수직 정렬을 중앙으로 설정합니다 */
  text-align: center; /* 텍스트를 중앙으로 정렬합니다 */
  border-radius: 5px
}

.pagination button:disabled {
  background-color: grey;
  cursor: not-allowed;
}

  </style>
  