<template>
    <div class="search-container">       
    <input v-model="searchQuery" placeholder="원하시는 상품을 찾아보세요" class="search-input" @keyup.enter="doSearch">
    <button @click="doSearch" class="search-button">
      <i class="fa-solid fa-magnifying-glass"></i>      
    </button>
  </div>    
</template>

<script setup>
import { ref } from 'vue';
import { useProductStore } from '../../stores/product';
import router from '@/router';

const productStore = useProductStore();
const searchQuery = ref('');

const doSearch = async () => {
  const query = searchQuery.value.trim();
  if (query) {
    const hasResults = await productStore.researchSearchResult(query);
    if (hasResults) {
      router.push({ name: 'productList', params: { keyword: query } });
    } else {
      alert('검색 결과가 없습니다.');
    }
  } else {
    window.location.reload();
  }
};
</script>

<style>
.search-input {

}

.search-button {

}

.search-container {
  display: flex;
  align-items: center;
  position: relative; /* 버튼을 입력 필드에 대해 절대 위치시키기 위함 */
  width: 100%;
  max-width: 550px; /* 최대 너비 설정, 필요에 따라 조정 */
}

.search-input {
  width: 100%;
  border-radius: 10px; 
  border: none; /* 기존 테두리 제거 */
  border-bottom: 1px solid #EFEFEF; /* 아래쪽 테두리만 설정 */
  padding: 10px 40px 10px 20px; /* 오른쪽 패딩을 늘려서 버튼 공간 확보 */
  font-size: 1rem;
  box-shadow: 0px 4px 0px 0px black; /* 박스 그림자 */
  outline: none; 
}


.search-button {
  background: none;
  border: none;
  position: absolute;
  right: 0; 
  padding: 10px; 
  cursor: pointer;
  
}

</style>