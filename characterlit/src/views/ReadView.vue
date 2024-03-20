<template>
    <div>
      <h1>This is Read Page {{ bidId }}</h1>
      <router-link to="/" class="home-button">HomeView로 돌아가기</router-link>
    </div>
    <div>
      <input type="number" v-model="requestBid" placeholder="Enter requestBid">
    <button @click="sendRequest">Send Request</button>
    </div>
        <!-- 이벤트 메시지 표시 영역 -->
        <div v-if="latestEvent">
      <h2>Event Messages:</h2>
      <p>{{ latestEvent }}</p>
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
      const latestEvent = ref(null);
      let eventSource = null; // EventSource 인스턴스를 저장할 변수
  
const subscribe = () => {
      const url = `http://localhost:8080/v1/sse/subscribe/${bidId.value}?nickname=${encodeURIComponent(nickname)}`;
      eventSource = new EventSource(url);

      eventSource.addEventListener('bid price update', (event) => {
        const data = JSON.parse(event.data);
        latestEvent.value = `Bid ID: ${data.bidId}, Member Name: ${data.memberName}, Request Bid: ${data.requestBid}`;
        console.log("Received bid price update:", data);
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
  const url = 'http://localhost:8080/v1/sse/disconnect';
  console.log("request sent:", bidId.value);
  navigator.sendBeacon(url, payload);
  console.log("Disconnect request sent");
};  

const sendRequest = async () => {
      const bidTime = new Date().toISOString(); // 현재 시간을 ISO 문자열로 변환하여 bidTime으로 사용
      const userId = 4; // 현재 사용자의 nickname을 userId로 사용

      // 사용자가 입력한 requestBid 값이 유효한지 확인
      if (!requestBid.value) {
        console.error("RequestBid is required");
        return;
      }

      const data = {
        userId: userId,
        requestBid: requestBid.value,
        bidTime: bidTime
      };
      console.log("Request sent", data);
      try {
        const response = await axios.post(`http://localhost:8080/api/bid/read/${bidId.value}`, data, {
          withCredentials: true
        });
        console.log("Request sent successfully", response.data);
      } catch (error) {
        console.error("Request error:", error);
      }
    };

      onMounted(() => {
        // 페이지에 진입하면 subscribe 요청
        subscribe();

      });
  
      onBeforeUnmount(() => {
        // 페이지를 떠나기 전에 disconnect 요청
        disconnect();
      });
  
      return {
        bidId,
        requestBid,
        sendRequest,
        latestEvent
      };
    }
  };
  </script>
  
  <style>
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
  </style>