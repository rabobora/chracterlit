<template>
    <div id="app" class="container">
        <div class="row">
          <div class="col-md-6">
            <form class="form-inline">
              <div class="form-group">
                <div class="form-group">
                <label for="name">chatroomId</label>
                <input
                  type="text"
                  id="name"
                  class="form-control"
                  v-model="send_chatroomId"
                  placeholder="들어갈 채팅방 번호는?"
                >
              </div>
                <label for="connect">WebSocket connection:</label>
                <button
                  id="connect"
                  class="btn btn-default"
                  type="submit"
                  :disabled="connected == true"
                  @click.prevent="connect"
                >채팅방 입장</button>
                <button
                  id="disconnect"
                  class="btn btn-default"
                  type="submit"
                  :disabled="connected == false"
                  @click.prevent="disconnect"
                >채팅방 나가기
                </button>
              </div>
            </form>
          </div>
          <div class="col-md-6">
            <form class="form-inline">
              <div class="form-group">
                <label for="name">senderId</label>
                <input
                  type="text"
                  id="name"
                  class="form-control"
                  v-model="sender_id"
                  placeholder="sender id here."
                >
              </div>
            </form>
          </div>
        </div>
        <!-- 채팅방화면-->
        <div id="chatroomBox">
          <h1>채팅방 화면</h1>
          <ul style="list-style:none;">
            <li v-for="item in received_messages" :key="item">
              <div class="bubble">
                <p :class="[item.senderId==this.sender_id ? 'from-me':'from-them']">{{item.content}}</p>
              </div>
            </li>
          </ul>
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
        </div>
    </div>
  </template>
  
<script>
  import SockJS from "sockjs-client";
  import Stomp from "webstomp-client";

  export default {
    data() {
      return {
        received_messages:[],
        sender_id: null,
        content: null,
        connected: false
      };
    },
    methods: {
      send() {
        console.log("Send message:" + this.send_message);
        if (this.stompClient && this.stompClient.connected) {
          const msg = {
            chatroomId: this.send_chatroomId,
            senderId: this.sender_id,
            content: this.content
          };
          console.log(JSON.stringify(msg));
          // 보낼 땐 JSON 문자열로. 아니면 spring에서 처리 못함
          this.stompClient.send("/pub/api/chat/"+this.send_chatroomId, JSON.stringify(msg), {});
        }
      },
      connect() {
        this.socket = new SockJS("http://localhost:8080/ws");
        // 주어진 WebSocket 객체를 STOMP 클라이언트로 변환하여 STOMP 프로토콜 사용 가능
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.connect(
          {}, // 연결에 필요한 옵션
          frame => { // 연결에 성공했을 때 실행되는 콜백 함수
            this.connected = true;
            console.log(frame);

            console.log("subscribing: "+"/sub/"+this.send_chatroomId);

            // gpt피셜 subscribe 파트가 서버에서 새로운 메세지 올 때마다 실행된다고..
            // 링크에 chatroomId를 줘야함
            this.stompClient.subscribe("/sub/"+this.send_chatroomId, tick => {
              console.log("tick:"+tick); // JSON 문자열
              console.log("tick.body:"+tick.body);
              console.log("tick.body.content:"+JSON.parse(tick.body).content);
              this.received_messages.push(JSON.parse(tick.body));
            });
          },
          error => {
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
      }
    },
    mounted() { // 오 ㅅㅂ 페이지 진입 시 자동연결
      // this.connect();
    }
  };
</script>

<style scopped>
#chatroomBox{
  border:1px solid blue;
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