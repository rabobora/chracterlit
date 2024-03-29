import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useBiddingStore = defineStore(
  "bidding",
  () => {
    // =========== STATE ===============

    const bidId = ref(null);
    const itemDetail = ref({
      title: null,
      startBid: null,
      thumbnail: null,
      content: null,
      startBidFormatted: null
    });
    const latestEvent = ref({
      requestBid: null,
      requestBidFormatted: null
    });
    const requestBid = ref('');
    const showMessage = ref(false);
    const popMessage = ref('');
    let messageTimeout = null;
    let eventSource = null;
    const isBidValid = ref(null);
    const isLoggedIn = ref(null);
    const sendRequestStatus = ref(null);

    // =========== GETTER ===============

    const getRequestBid = computed({
      get: () => requestBid.value,
      set: (value) => requestBid.value = value
    });

    const getIsBidValid = computed(() => {
      if (!requestBid.value || isNaN(requestBid.value)) {
          return false;
      }
          const minBid = Number(latestEvent.value.requestBidFormatted.replace(/[₩,]/g, ''));
          return requestBid.value > minBid;
  });
    
  const getIsLoggedIn = computed(() => isLoggedIn.value = !!localStorage.getItem('access-token'));

  const getSendRequestStatus = computed(() => sendRequestStatus.value = isLoggedIn.value ? '입찰' : '로그인해주세요');


    // =========== ACTION ===============

    // 서버로부터 bidId에 해당하는 글 정보 호출
    const fetchItemDetail = async () => {
        try {
          const response = await fetch(`http://localhost:8080/api/bid/read/${bidId.value}`, {
            method: 'GET',            
            credentials: 'include'
          });
          const data = await response.json();
          itemDetail.value = data;
          itemDetail.value.startBidFormatted = `₩${itemDetail.value.startBid.toLocaleString()}`;
          console.log("Item detail fetched successfully", data);
        } catch (error) {
          console.error("Failed to fetch item detail:", error);
        }
      };
      
    // 서버로부터 현재 입찰가를 가져오는 함수
    const loadInitialEvent = async () => {
        try {
          const response = await fetch(`http://localhost:8080/api/bid/read/now/${bidId.value}`, {
            credentials: 'include'
          });
          const data = await response.json();
          latestEvent.value.requestBid = data;
          latestEvent.value.requestBidFormatted = `₩${data.toLocaleString()}`;
          console.log("Initial current bid loaded:", data);
        } catch (error) {
          console.error("Error loading initial current bid:", error);
        }
      };
      
    // 서버 SSE 연결 함수
    const subscribe = () => {
            const sessionId = sessionStorage.getItem('userSession');
            const url = `http://localhost:8080/api/sse/subscribe/${bidId.value}?sessionId=${sessionId}`;
            eventSource = new EventSource(url, { withCredentials: true });
      
            eventSource.addEventListener('first connect', (event) => {  
              console.log("Received connection success");
            });
            
            // 실시간 입찰가 갱신 리스너
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
            
            // 입찰 성공 리스너
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
    
    // SSE 연결 해제 함수      
    const disconnect = () => {
        const sessionId = sessionStorage.getItem('userSession');
        if (eventSource) {
            eventSource.close(); // EventSource 연결 종료
            }
          
        const data = {
        bidId: Number(bidId.value), // 확실히 숫자로 변환
        sessionId: sessionId,
        };
        const payload = new Blob([JSON.stringify(data)], { type: 'application/json' });
        const url = 'http://localhost:8080/api/sse/disconnect';
        console.log("request sent:", bidId.value);
        const sent = navigator.sendBeacon(url, payload);
        console.log("Disconnect request sent:", sent ? "Success" : "Failed");
        };  

    // 입찰 함수    
    const sendRequest = async () => {
        if (!requestBid.value) {
          console.error("RequestBid is required");
          return;
        }
        const sessionId = sessionStorage.getItem('userSession');
        const data = {
          sessionId: sessionId,
          requestBid: requestBid.value  
        };
      
        console.log("Request sent", data);
        console.log(localStorage.getItem('access-token'));
      
        try {
          const response = await fetch(`http://localhost:8080/api/bid/read/${bidId.value}`, {
            method: 'POST', // HTTP 메소드 지정
            headers: {
              'Content-Type': 'application/json', // 콘텐츠 타입 지정
              // 'Authorization': `Bearer ${localStorage.getItem('access-token')}`,
              'access_token': localStorage.getItem('access-token')
            },
            body: JSON.stringify(data), // 요청 본문에 데이터 첨부
            credentials: 'include', // 쿠키를 포함시키기 위해 credentials 옵션 사용
          });
          
          const responseData = await response.text();
          console.log("Request sent successfully", responseData);
        } catch (error) {
          console.error("Request error:", error);
        }
      };  

    return {
      bidId,
      itemDetail,
      latestEvent,
      requestBid,
      showMessage,
      popMessage,
      messageTimeout,
      eventSource,
      isBidValid,
      isLoggedIn,
      sendRequestStatus,
      getIsLoggedIn,
      getIsBidValid,
      getSendRequestStatus,
      getRequestBid,
      fetchItemDetail,
      loadInitialEvent,
      subscribe,
      disconnect,
      sendRequest,
    };
  }, { persist: true })