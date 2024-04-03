<template>
  <!-- 채팅방화면 -->
  <div v-if="givenChatroomId" class="chatroomBox">
    <!-- 채팅 내역 -->
    <div id="messageBox">
      <ul style="list-style:none;">
        <h1>{{givenChatroomId}}번 채팅방 화면</h1>
        <h2>{{ givenBidId }}번 물품</h2>
        <h2>접속한 유저:{{ givenUserNumber }}</h2>
        <li v-for="item in chat_logs" :key="item">
          <div class="bubble">
            <p :class="[item.senderId==this.givenUserNumber ? 'from-me':'from-them']">{{item.content}}</p>
          </div>
        </li>
        <li v-for="item in store_messages" :key="item">
          <div class="bubble">
            <p :class="[item.senderId==this.givenUserNumber ? 'from-me':'from-them']">{{item.content}}</p>
          </div>
        </li>
          <!-- input message form -->
    <div class="mb-3 inputmsg">
      <div class="input-group">
        <input @keyup.enter="send" type="text" id="message" v-model="content" class="form-control" placeholder="메세지를 입력하세요."/>
          <button @click="send">
            <div class="svg-wrapper-1">
              <div class="svg-wrapper">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  width="24"
                  height="24"
                >
                  <path fill="none" d="M0 0h24v24H0z"></path>
                  <path
                    fill="currentColor"
                    d="M1.946 9.315c-.522-.174-.527-.455.01-.634l19.087-6.362c.529-.176.832.12.684.638l-5.454 19.086c-.15.529-.455.547-.679.045L12 14l6-8-8 6-8.054-2.685z"
                  ></path>
                </svg>
              </div>
            </div>
          </button>
      </div>
    </div>
      </ul>
    </div>
  </div>
  <div v-else class="chatroomBox">
    <h1>채팅방을 선택해 주세요. 😪</h1>
  </div>
    </template>
    
  <script>
    import SockJS from "sockjs-client/dist/sockjs.min.js";
    import Stomp from "webstomp-client";
    export default {
      props: ['givenChatroomId', 'givenBidId', 'givenUserNumber'],
      watch:{
        givenChatroomId(chatroomId, oldChatroomId){
          console.log("채팅방 값이 변경되었습니다."+chatroomId);
          this.connect(chatroomId, oldChatroomId);
          console.log("채팅 내역을 가져옵니다:"+chatroomId);
          this.getMessageLogs(chatroomId);
        },
      },
      data() {
        return {
          chat_logs:[], // 당장 받아올 채팅 내역
          store_messages:[], // 저장할 채팅 내역
          content: null,
          connected: false,
          send_chatroomId:'',
        };
      },
      methods: {
        send() {
          console.log("Send message:" + this.send_message);
          console.log("소켓 연결할 채팅방 번호:"+this.givenChatroomId);
          console.log("송신자 id:"+this.givenUserNumber);

          if (this.stompClient && this.stompClient.connected) {
            const msg = {
              chatroomId: this.givenChatroomId,
              senderId: this.givenUserNumber,
              content: this.content,
            };
            console.log(JSON.stringify(msg));
            // 보낼 땐 JSON 문자열로. 아니면 spring에서 처리 못함
            this.stompClient.send("/pub/api/chat/"+this.givenChatroomId, JSON.stringify(msg), {});

            this.content="";
          }
        },
        connect(chatroomId, oldChatroomId) { // 채팅방 접속
          if (this.stompClient) { // 연결이 되어 있다면 끊고 다시 연결
            console.log("이전 채팅방 접속을 종료하고 새 커넥션을 생성합니다.");
            this.stompClient.disconnect();
          }
  
          this.chat_logs=[]; // 채팅 데이터 초기화
  
          // 저장할 메시지 값이 있다면 저장
          if(this.store_messages.length){
            console.log(oldChatroomId+"번째 방의 채팅방 내역을 저장합니다.");
            this.saveMessageLogs(oldChatroomId);
            this.store_messages=[]; // 추가되었던 채팅 데이터 초기화
          }
  
          this.socket = new SockJS("http://localhost:8080/ws");
          // 주어진 WebSocket 객체를 STOMP 클라이언트로 변환하여 STOMP 프로토콜 사용 가능
          this.stompClient = Stomp.over(this.socket);
          this.stompClient.connect(
            {}, // 연결에 필요한 옵션
            frame => { // 연결에 성공했을 때 실행되는 콜백 함수
              this.connected = true;
              console.log(chatroomId+"번 채팅방에 접속합니다.");
              console.log(frame);
  
              console.log("subscribing: "+"/sub/"+chatroomId);
  
              // gpt피셜 subscribe 파트가 서버에서 새로운 메세지 올 때마다 실행된다고..
              // 링크에 chatroomId를 줘야함
              this.stompClient.subscribe("/sub/"+chatroomId, tick => {
                console.log("tick:"+tick); // JSON 문자열
                console.log("tick.body:"+tick.body);
                console.log("tick.body.content:"+JSON.parse(tick.body).content);
                this.store_messages.push(JSON.parse(tick.body));
              });
            },
            error => {
              console.log("커넥트 실패 😮");
              console.log(error);
              this.connected = false;
            }
          );
        },
        disconnect() {
          if (this.stompClient) {
            this.stompClient.disconnect();
          }
          this.connected = false;
        },
        tickleConnection() {
          this.connected ? this.disconnect() : this.connect();
        },
        saveMessageLogs(){
          // n번 채팅방 내역 저장
          fetch("http://localhost:8080/api/chat/save", {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
            },
            body: JSON.stringify(this.store_messages)
          })  
          .then((response) => {
              if (response.ok) {
                console.log("저장 프로세스 성공했어요.");
              }else{
                throw new Error("Network response was not ok.");
              }
          })
          .catch((error) => {
              console.error("저장 프로세스에서 문제가 생겼어요.", error);
          });
        },
        getMessageLogs(chatroomId){
          // n번 채팅방 채팅내역 불러오기
          fetch("http://localhost:8080/api/chat/list/"+chatroomId, {
            method: "GET",
            headers: {
            "Content-Type": "application/json",
            },
          })  
          .then((response) => {
              if (response.ok) {
              return response.json();
              }
              throw new Error("Network response was not ok.");
          })
          .then((data) => { // chat message array
            console.log("채팅 메시지 내역 불러오기 성공");

              for(var i=0;i<data.length;i++){
                  this.chat_logs.push({
                      chatroomId: data[i].chatroomId,
                      senderId: data[i].senderId,
                      content: data[i].content,
                });
              }
          })  
          .catch((error) => {
              console.error("채팅 메시지 가져오기 실패.", error);
          });
        }
      },
      mounted() { // 페이지 진입 시 작동
        // this.connect();
      }
    };
  </script>
  
  <style scopped>
  #messageBox{
    width:500px;
  }
  .chatroomBox{
    border:1px solid blue;
    width:300px;
    height:100%;
  }
  .bubble {
    border-radius: 0.25rem;
    display: flex;
    flex-direction: column;
    font-family: "SanFrancisco";
    font-size: 1.25rem;
    margin: 0 auto 1rem;
    max-width: 600px;
    padding: 0.6rem 1.5rem;
  }
  
  .bubble p {
    border-radius: 1.15rem;
    line-height: 1.25;
    max-width: 75%;
    padding: 0.5rem 0.875rem;
    position: relative;
    word-wrap: break-word;
  }
  
  .bubble p::before,
  .bubble p::after {
    bottom: -0.1rem;
    content: "";
    height: 1rem;
    position: absolute;
  }
  
  p.from-me {
    align-self: flex-end;
    background-color: #ffb3be;
    color: #000;
  }
  
  p.from-me::before {
    border-bottom-left-radius: 0.8rem 0.7rem;
    border-right: 1rem solid #ffb3be;
    right: -0.35rem;
    transform: translate(0, -0.1rem);
  }
  
  p.from-me::after {
    background-color: #fff7f1;
    border-bottom-left-radius: 0.5rem;
    right: -40px;
    transform: translate(-30px, -2px);
    width: 10px;
  }
  
  p[class^="from-"] {
    margin: 0.5rem 0;
    width: fit-content;
  }
  
  p.from-me ~ p.from-me {
    margin: 0.25rem 0 0;
  }
  
  p.from-me ~ p.from-me:not(:last-child) {
    margin: 0.25rem 0 0;
  }
  
  p.from-me ~ p.from-me:last-child {
    margin-bottom: 0.5rem;
  }
  
  p.from-them {
    align-items: flex-start;
    background-color: #ffebbb;
    color: #000;
    z-index: 1;
  }
  
  p.from-them:before {
    border-bottom-right-radius: 0.8rem 0.7rem;
    border-left: 1rem solid #ffebbb;
    left: -0.35rem;
    transform: translate(0, -0.1rem);
  }
  
  p.from-them::after {
    background-color: #fff7f1;
    border-bottom-right-radius: 0.5rem;
    left: 20px;
    transform: translate(-30px, -2px);
    width: 10px;
  }
  
  </style>