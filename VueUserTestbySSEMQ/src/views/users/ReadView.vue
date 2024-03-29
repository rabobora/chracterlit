<template>

  <div class="read-page-container">
    <div class="header">
      <h1>This is Read Page {{ bidId }}</h1>
      <router-link to="/" class="home-button">HomeView로 돌아가기</router-link>
    </div>
    <div v-if="showMessage" class="message-overlay">
      <div class="message-box">
        {{ popMessage }}
      </div>
    </div>
    <div class="content">
      <div class="item-image">
        <!-- 이미지가 있을 때 -->
        <img v-if="itemDetail.thumbnail" :src="itemDetail.thumbnail" alt="Item image" class="image-style" />
        <!-- 이미지가 없을 때 -->
        <div v-else class="image-placeholder"></div>
      </div>

      <div class="item-info">
        <div class="item-details">
          <h1 class="title">{{ itemDetail.title || 'Loading...' }}</h1>
          <p class="start-price">시작가: <span>{{ itemDetail.startBidFormatted || 'Loading...' }}</span></p>
          <p class="current-price">현재가: <span>{{ latestEvent.requestBidFormatted || 'Loading...' }}</span></p>
        </div>
        <div class="bid-input">
          <input type="number" v-model="requestBid" placeholder="Enter requestBid">
          <button @click="sendRequest">Send Request</button>
        </div>
      </div>
    </div>
    <div class="description">
      <h2>상세설명</h2>
      <p>{{ itemDetail.content || 'Loading...' }}</p>
    </div>
  </div>
  </template>
  
  <script>
  import { ref, onMounted, onBeforeUnmount } from 'vue';
  import axios from 'axios';
  import { useRoute } from 'vue-router';
  
  export default {
    name: 'ReadView',
    setup() {
      const route = useRoute();
      const bidId = ref(route.params.number);  
      const nickname = sessionStorage.getItem('nickname'); // 세션에서 닉네임 가져옴
      const requestBid = ref(null); // 사용자가 입력한 requestBid 값을 저장할 ref 변수
      const latestEvent = ref('');
      const itemDetail = ref({
                      title: null,
                      startBid: null,
                      thumbnail: null,
                      content: null
      });
      let eventSource = null; // EventSource 인스턴스를 저장할 변수
      const showMessage = ref(false);
      const popMessage = ref('');
      let messageTimeout;

      const fetchItemDetail = async () => {
      try {
        const response = await axios.get(`http://j10b105.p.ssafy.io:8080/api/bid/read/${bidId.value}`,{
        withCredentials: true
      });
        itemDetail.value = response.data;
        if(typeof itemDetail.value.startBid === 'number' && !isNaN(itemDetail.value.startBid)) {
        itemDetail.value.startBidFormatted = `₩${itemDetail.value.startBid.toLocaleString()}`;
        } else {
        itemDetail.value.startBidFormatted = 'Loading...';
      }
        console.log("Item detail fetched successfully", response.data);
      } catch (error) {
        console.error("Failed to fetch item detail:", error);
      }
    };
    const loadInitialEvent = async () => {
      try {
        const response = await axios.get(`http://j10b105.p.ssafy.io:8080/api/bid/read/now/${bidId.value}`,{
        withCredentials: true
      });
        latestEvent.value = {
        requestBid: response.data,
        requestBidFormatted: `₩${response.data.toLocaleString()}`
      };
        console.log("Initial current bid loaded:", response.data);
      } catch (error) {
        console.error("Error loading initial current bid:", error);
      }
    };
  
      const subscribe = () => {
      const url = `http://j10b105.p.ssafy.io:8080/api/sse/subscribe/${bidId.value}`;
      eventSource = new EventSource(url, { withCredentials: true });

      eventSource.addEventListener('first connect', (event) => {
        const data = JSON.parse(event.data);        
        console.log("Received connection success : ", data);
      });

      eventSource.addEventListener('bid price update', (event) => {
        const data = JSON.parse(event.data);

        latestEvent.value = {
        ...latestEvent.value, // 기존의 latestEvent 데이터를 유지
        requestBid: data.requestBid,
        requestBidFormatted: `₩${data.requestBid.toLocaleString()}`,
        memberName: data.memberName,
        bidId: data.bidId
      };
        console.log("Received bid price update:", data);
      });

      eventSource.addEventListener('bidding success', (event) => {
        const data = JSON.parse(event.data);
        popMessage.value = `${data.requestBid}원에 입찰 성공`;
        showMessage.value = true;
        clearTimeout(messageTimeout);
        messageTimeout = setTimeout(() => {
          showMessage.value = false;
        }, 3000);
      });

      eventSource.onopen = (event) => {
         console.log("SSE opened:", event);
     };

      eventSource.onmessage = (event) => {
        console.log("SSE message:", event.data);
        const data = JSON.parse(event.data);
        latestEvent.value = `Bid ID: ${data.bidId}, Member Name: ${data.memberName}, Request Bid: ${data.requestBid}`;
        console.log("Received event:", data);
      };

      eventSource.onerror = (error) => {
        console.error("EventSource failed:", error);
        eventSource.close(); // 에러 발생 시 연결 종료
      };
    };

const disconnect = () => {
  if (eventSource) {
        eventSource.close(); // EventSource 연결 종료
      }
    const data = {
    bidId: Number(bidId.value), // 확실히 숫자로 변환
    nickname: nickname,
  };
  const payload = new Blob([JSON.stringify(data)], { type: 'application/json' });
  const url = 'http://j10b105.p.ssafy.io:8080/api/sse/disconnect';
  console.log("request sent:", bidId.value);
  navigator.sendBeacon(url, payload);
  console.log("Disconnect request sent");
};  

const sendRequest = async () => {

      // 사용자가 입력한 requestBid 값이 유효한지 확인
      if (!requestBid.value) {
        console.error("RequestBid is required");
        return;
      }

      const data = {
        requestBid: requestBid.value  
      };
      console.log("Request sent", data);
      try {
        const response = await axios.post(`http://j10b105.p.ssafy.io:8080/api/bid/read/${bidId.value}`, data, {
          withCredentials: true
        });
        console.log("Request sent successfully", response.data);
      } catch (error) {
        console.error("Request error:", error);
      }
    };

        onMounted(async () => {
          await fetchItemDetail();
          await loadInitialEvent();
          subscribe();
          window.addEventListener('beforeunload', disconnect);
      });  
      onBeforeUnmount(() => {
        // 페이지를 떠나기 전에 disconnect 요청
        disconnect();
          // 더 이상 필요하지 않으므로 이벤트 리스너 제거
        window.removeEventListener('beforeunload', disconnect);
      });
  
      return {
        bidId,
        requestBid,
        sendRequest,
        latestEvent,
        itemDetail,
        showMessage,
        popMessage
      };
    }
  };
  </script>
  
  <style>
.read-page-container {
  max-width: 800px; /* 컨테이너의 최대 너비 */
  margin: auto; /* 중앙 정렬 */
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.item-image {
  text-align: center; /* 이미지를 중앙 정렬 */
}

.image-style {
  width: 100%;
  max-width: 300px;
  height: auto;
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

.title {
  font-size: 1.5em;
  color: #333;
  text-align: center; /* 타이틀 중앙 정렬 */
}

.price-container {
  text-align: center; /* 가격 정보 중앙 정렬 */
  margin: 20px 0;
}

.start-price, .current-price {
  font-size: 1em;
  color: #666;
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
  text-align: center; /* 입력 필드 중앙 정렬 */
  margin-top: 20px;
}

.bid-input input {
  padding: 5px;
  margin-right: 10px;
}

.bid-input button {
  padding: 5px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
}

.bid-input button:hover {
  background-color: #0056b3;
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
}

.bid-input {
  display: flex;
  justify-content: space-between; /* 입력 필드와 버튼 사이의 공간을 최대로 */
}

.bid-input input {
  flex: 1; /* 가능한 모든 공간을 차지 */
  margin-right: 10px; /* 버튼과의 간격 */
}

  </style>