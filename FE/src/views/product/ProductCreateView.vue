<template>
    <div class="product-create-view">
      <input id="title" type="text" placeholder="상품명을 입력하세요" v-model="product.title" class="titleformcolor">
  
      <div class="file-upload-group">
        <div class="file-upload">
            <label for="image-upload">이미지 업로드 </label>
            <input id="image-upload" type="file" multiple @change="previewImages">
            <button @click="uploadImagesToS3" class="uploadbutton">업로드</button> 
        </div>
        <div class="file-upload">
          <label for="thumbnail-upload">썸네일 업로드 </label>
          <input id="thumbnail-upload" type="file" @change="handleThumbnailUpload">
        </div>
      </div>
  
      <div class="date-time-group">
        <div class="date-time">
          <label for="start-date">시작일 </label>
          <input id="start-date" type="date" v-model="startDate">   
          <select v-model="startTime" class="datemargin">
            <option v-for="hour in Array.from({ length: 24 }, (_, i) => i.toString().padStart(2, '0'))"
                    :key="hour"
                    :value="`${hour}:00`">
                {{ `${hour}:00` }}
            </option>            
          </select> 시
        </div>
        <div class="date-time">
          <label for="end-date">종료일 </label>
          <input id="end-date" type="date" v-model="endDate">
          <select v-model="endTime" class="datemargin">
            <option v-for="hour in Array.from({ length: 24 }, (_, i) => i.toString().padStart(2, '0'))"
                    :key="hour"
                    :value="`${hour}:00`">
                {{ `${hour}:00` }}
            </option>
          </select> 시

        </div>
      </div>
  
      <div class="bid-category-group">
        <div class="bid-input">
          <label for="start-bid">시작가 </label>
          <input
            id="start-bid"
            type="number"
            placeholder="시작 입찰 가격"
            v-model.number="product.startBid"
            @input="validateStartBid"
            min="0"
            > ₩
        </div>
        <div class="category-select">
          <label for="category">카테고리 </label>
          <select id="category" v-model="product.category">
            <option value="0">피규어</option>
            <option value="1">인형</option>
            <option value="2">의류</option>
            <option value="3">음반/서적/블루레이</option>
            <option value="4">카드 및 지류</option>
            <option value="5">팬시/문구</option>
            <option value="6">생활/인테리어</option>
            <option value="7">기타</option>
          </select>
        </div>
      </div>  
     
      <textarea id="content" placeholder="상품의 상세정보를 입력하세요" v-model="product.content"></textarea>
  
      <div class="submit-button">
        <button @click="submitProduct" class="submit-button-margin">등록하기</button>
        <button @click="backtolist" class="bakcbutton">뒤로가기</button>
      </div>
    </div>
  </template>
  
  
  <script setup>
  import { ref } from 'vue';
  import { useProductStore } from '../../stores/product';
  import axios from 'axios';
  import router from '@/router';
  const store = useProductStore();

  const product = ref({
    title: '',
    content: '',
    image: [],
    thumbnail: '',
    startDate: '',
    endDate: '',
    startBid: 0,
    category: 0
  });
  
  const startDate = ref('');
  const startTime = ref('00:00');
  const endDate = ref('');
  const endTime = ref('00:00');
  const selectedImages = ref([]);
  
  const previewImages = (event) => {
    // 새로 선택된 파일을 기존 파일 배열에 추가
    const newImages = Array.from(event.target.files);

    // 새로운 이미지와 기존 이미지의 합이 5개를 초과하는지 검사
    if (selectedImages.value.length + newImages.length > 5) {
        alert('최대 5장의 이미지만 첨부할 수 있습니다.');
        return;
    }

    selectedImages.value = [...selectedImages.value, ...newImages];

    // 중복 제거: 파일 이름으로 중복을 확인
    selectedImages.value = selectedImages.value.reduce((acc, current) => {
        const x = acc.find(item => item.name === current.name);
        if (!x) {
        return acc.concat([current]);
        } else {
        return acc;
        }
    }, []);

    };


    const uploadImagesToS3 = async () => {
        if (selectedImages.value.length > 0) {
            try {
            const urls = await store.s3ImageUpload(selectedImages.value);

            if (urls.length > 0) {
                
                product.value.image = urls;
                
                alert('첨부완료되었습니다.');
            } else {
                
                throw new Error('No URLs returned');
            }
            } catch (error) {            
            alert('이미지 업로드에 실패했습니다.');            
            console.error('Upload failed:', error);
            }
        } else {
            alert('업로드할 이미지를 선택해주세요.');
        }
     };
  
  const handleThumbnailUpload = async (event) => {
    const thumbnail = event.target.files[0];
    let url = await store.s3ThumbnailUpload(thumbnail);
    if (url) {
      url = url.replace('images', 'resized-images');
      product.value.thumbnail = url;
    }
  };
  
  const submitProduct = () => {

    if (!product.value.title || !product.value.content || product.value.startBid === 0 || !startDate.value || !endDate.value || product.value.category === 0) {
    alert('모든 정보를 완벽하게 입력해주세요.');
    return;
  }
    const startDateTime = `${startDate.value}T${startTime.value}:00Z`;
    const endDateTime = `${endDate.value}T${endTime.value}:00Z`;

    const start = new Date(startDateTime);
    const end = new Date(endDateTime);
    const now = new Date();
  
    if (start <= now) {
      alert('경매 시작일은 현재 시간보다 이후로 설정해야 합니다.');
      return;
    }

    // 경매 종료일이 경매 시작일보다 이후인지 확인
    if (end <= start) {
      alert('경매 종료일은 경매 시작일보다 늦게 설정해야 합니다.');
      return;
  }

  product.value.startDate = startDateTime;
  product.value.endDate = endDateTime;

  store.createProduct(product.value);
  router.push('/product/list');
};



  const backtolist = () => {
    router.push('/product/list'); 
    
  }

  const validateStartBid = () => {
      if (product.value.startBid < 0) {
        alert('입찰 가격은 0부터 입력해야 합니다.');
        product.value.startBid = 0;
      }
    };

  </script>
  

  <style>
  .titleformcolor{
    background-color: rgb(238, 236, 236);
    color: black;
  }
  .datemargin{
    margin-left: 5%;
  }
  .product-create-view {
    display: flex;
    flex-direction: column;
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    background-color: white;
  }
  
  .file-upload-group,
  .date-time-group,
  .bid-category-group {
    display: flex;
    justify-content: space-between; 
    align-items: center;
    margin-bottom: 20px;
    margin-left: 3%;
  }
  
  .file-upload,
  .date-time,
  .bid-input,
  .category-select {
    flex: 1;
    margin-right: 20px; 
  }
  
  .file-upload:last-child,
  .date-time:last-child,
  .bid-category-group > div:last-child {
    margin-right: 0;
  }  
  
  .file-upload input[type="file"],
  .date-time input,
  .bid-input input,
  .category-select select {
    width: calc(50% - 20px);
    padding: 10px;
    margin-top: 5px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }  
  
  textarea#content {
    height: 300px; 
    width: 98%; 
    background-color: rgb(238, 236, 236);
    color: black;
  }  

  .submit-button {
    display: flex;
    justify-content: flex-end; 
  }
  
  .submit-button button {
    width: auto;
    padding: 10px 20px;    
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  .product-create-view input,
  .product-create-view textarea,
  .product-create-view select {
    margin-bottom: 20px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  

  .product-create-view input[type="file"] {
    border: none;
    background-color: #f8f8f8;
    border: 1px dashed #ccc;
    text-align: center;
    padding: 10px;
    cursor: pointer;
  }  
  
  .product-create-view > *:not(:last-child) {
    margin-bottom: 1rem;
  }
  .submit-button-margin{
    margin-right: 10px;
    background-color: black;
    color: white;
  }
  .bakcbutton{
    background-color: brown;
    color: yellow;
  }
  .uploadbutton{
    color: white;
    background-color: black;
    border-radius: 15px;
    margin-left: 10px;
  }
  
  </style>
  
  
  