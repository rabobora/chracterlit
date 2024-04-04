<template>
    <div class="bidding-list-container">
      <h2>나의 구매 및 입찰 내역</h2>
      <h1 v-if="combinedList.length===0" style="color: rgb(63, 62, 62);">입찰하거나 구매한 상품이 없습니다</h1>
      <div v-for="item in combinedList" :key="item.bidId" class="bid-item">
        <div class="item-thumbnail" @click="navigateToItem(item.bidId)">
          <img :src="item.thumbnail || defaultImage" alt="Item Thumbnail">
        </div>
        <div class="item-details" @click="navigateToItem(item.bidId)">
          <h2 :title="item.title">{{ item.title }}</h2>
          <p>시작 가격: {{ item.startBid }} 원</p>
          <p class="presentBid">현재 가격: {{ item.presentBid }} 원</p>
          <p class="requestBid" :class="{'bid-state': item.presentBid === item.requestBid}">나의 입찰: {{ item.requestBid }} 원</p>
          <p>종료 시간: {{ formatDateTime(item.endDate) }}</p>
          <p>조회 수: {{ item.viewCount }}</p>
        </div>
        <div class="status-box">
        <div class="item-status" :class="{'pre-auction': item.bidStatus === 0, 'in-auction': item.bidStatus === 1, 'post-auction': item.bidStatus === 2}" @click="navigateToItem(item.bidId)">
          <span v-if="item.bidStatus === 0">경매 이전</span>
          <span v-else-if="item.bidStatus === 1">경매 진행</span>
          <span v-else-if="item.bidStatus === 2">경매 종료</span>
    </div>
    <div class="item-status" :class="{'hidden-element': item.bidStatus !== 2, 'pre-auction': item.winnerNumber !== usersStore.loginUser.userNumber, 'in-auction': !item.isPaid, 'post-auction': item.isPaid}" @click="paypay(item, $event)">
          <span v-if="item.bidStatus === 2 && !item.isPaid && item.winnerNumber === usersStore.loginUser.userNumber">결제 하기</span>
          <span v-else-if="item.bidStatus === 2 && item.isPaid && item.winnerNumber === usersStore.loginUser.userNumber">결제 완료</span>
          <span v-else-if="item.bidStatus === 2 && item.winnerNumber !== usersStore.loginUser.userNumber">입찰 실패</span>
        </div>
      </div>
      </div>
    </div>
  </template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import defaultImage from '@/assets/default_image.png';
import { useUsersStore } from '@/stores/users';
const usersStore = useUsersStore();
const router = useRouter();
const itemsList = ref([]);
const logList = ref([]);
const nowbidList = ref([]);

onMounted(async () => {
  await fetchBiddingList();
  await usersStore.fetchLoginUser();
});
  
  function formatDateTime(isoString) {
        const date = new Date(isoString);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const hour = date.getHours().toString().padStart(2, '0');
        const minute = date.getMinutes().toString().padStart(2, '0');

        return `${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분`;
      }

//결제 함수 작동 전 임시 코드
const paypay = (item, event) => {
  if (!(item.bidStatus === 2 && !item.isPaid && item.winnerNumber === usersStore.loginUser.userNumber)) {
    event.preventDefault();
    return;
  }
  router.push({ name: 'buyItem', params: { number: item.bidId } });
  console.log(item.bidId);
  //라우터 푸시 추가 구현 필요
};

const navigateToItem = (bidId) => {
  router.push({ name: 'ReadView', params: { number: bidId } });
};

const fetchBiddingList = async () => {
  try {
    const response = await fetch(`http://j10b105.p.ssafy.io:8080/api/bid/mybid`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'access_token': localStorage.getItem('access-token')
        },
        credentials: 'include',
    });
    const data = await response.json();

    itemsList.value = data.itemsList;
    logList.value = data.logList;
    nowbidList.value = data.nowbidList;

    console.log("My Bid List fetched successfully");
  } catch (error) {
    console.error("Failed to fetch my bid list:", error);
  }
};

const combinedList = computed(() => {
  const mergedList =  itemsList.value.map(item => {
    const log = logList.value.find(log => log.bidId === item.bidId);
    const nowbid = nowbidList.value.find(bid => bid.bidId === item.bidId);

    return {
      ...item,
      requestBid: log?.requestBid,
      presentBid: nowbid?.presentBid
    };
  });
  return mergedList.sort((a, b) => b.bidId - a.bidId);
});


</script>

<style>
.bidding-list-container {
    display: flex;
    flex-direction: column;
  max-width: 800px;
  margin: auto;
  padding: 20px;
  gap: 20px; 
}

.bid-item {
  display: flex;
  align-items: center; 
  justify-content: flex-start;
  gap: 16px; 
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  margin-bottom: 0px;
}

.item-thumbnail img {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border: 1px solid #e5e7eb; 
  box-shadow: 0px 4px 4px 0px rgb(63, 62, 62);
  border-radius: 10px;
}

.item-details {
  display: grid;
  flex-grow: 1;
  gap: 6px;
}

.item-details h2 {
  font-size: 20px; 
  font-weight: 600; 
  margin: 0 0 15px 0;
  word-break: break-word;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px; 
}

.item-details p {
  font-size: 0.875rem; 
  line-height: 1; 
  margin: 5px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 400px; 
}

.bidding-list-container > .bid-item:not(:last-child) {
  border-bottom: 1px solid #e5e7eb;
}

.presentBid, .requestBid {
  font-weight: 600;
  color: #007bff; 
}
.bid-state {
  animation: burning 1s infinite;
}

.bid-item:hover {
  background-color: #f9fafb; 
  cursor: pointer;
}

.status-box .item-status {
  margin-bottom: 10px;
}

.status-box .item-status:last-child {
  margin-bottom: 0;
}

.item-status {
  flex: none;  
  padding: 24px;
  border-radius: 4px;
  color: white;
  white-space: nowrap; 
  overflow: hidden; 
  text-overflow: ellipsis;
  max-width: 100px; 
}

.pre-auction {
  background-color: #b4b4b4;
}

.in-auction {
  background-color: #ff4500;
  animation: burning 1s infinite; 
}

@keyframes burning {
  0% { opacity: 1; }
  50% { opacity: 0.5; }
  100% { opacity: 1; }
}

.post-auction {
  background-color: #000000;
}

.hidden-element {
  visibility: hidden;
}
</style>
