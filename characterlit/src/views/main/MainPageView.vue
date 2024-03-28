<template>
    <div>
        <div> 
            캐릭터릿
        </div>
        <div>
            <SearchBarView />
        </div>
        <div>
            <p @click="gotoproductlist" class="productlist">시작하기</p>
        </div>    

    </div>
</template>

<script setup>
  import { ref, onMounted,computed } from 'vue';
  import { useProductStore } from '../../stores/product';
  import SearchBarView from '../product/SearchBarView.vue'
  
  import { useRoute,useRouter } from 'vue-router';
  const router = useRouter();
  const route = useRoute();
  const productStore = useProductStore();

  const gotoproductlist = () => {
    router.push({ name: 'productList'})
  }
</script>

<style scoped>
.productlist{
    cursor: pointer;
}
</style>