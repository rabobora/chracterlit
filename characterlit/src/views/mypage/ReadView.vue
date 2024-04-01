<template>
  <div class="product-detail" v-if="productDetail">    
    
    {{ productDetail }}
    <div class="image-gallery">
      <img v-for="imageUrl in productDetail.image" :key="imageUrl" :src="imageUrl" alt="Product Image" class="product-image"/>
    </div>

    <img :src="productDetail.thumbnail" alt="상품 이미지">
    <button @click="deleteSelectedProduct" class="deletebutton">상품 삭제</button>
    <button @click="updateproduct" class="updatebutton">상품수정</button>
    
  </div>
  <div v-else>    
    Loading...
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useProductStore } from '../../stores/product';
import { useRoute,useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();
const productStore = useProductStore();
const productDetail = ref(null); 

onMounted(async () => {
    const bidId = route.params.number;
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
</style>