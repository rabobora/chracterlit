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
  border-radius: 45px;
  border: 1px solid #EFEFEF;
  padding: 10px 40px 10px 20px; /* 오른쪽 패딩을 늘려서 버튼의 공간 확보 */
  font-size: 1rem;
  box-shadow: 0px 4px 4px 0px black;
  outline: none; /* 입력 필드 선택 시 테두리 제거 */
}

.search-button {
  background: none;
  border: none;
  position: absolute;
  right: 0; /* 입력 필드의 오른쪽 끝에 맞춤 */
  padding: 10px; /* 클릭 가능 영역 확장 */
  border-radius: 0 45px 45px 0; /* 오른쪽 상단과 하단 모서리 둥글게 */
  cursor: pointer;
}

</style>