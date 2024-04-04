<template>
    <div id="chatroomListBox">
        <!-- List-넘겨받은 bidid:{{ givenBidId }} -->
            <!-- <h1> login 한 user Number 테스트{{ store.getLoginUser.userNumber }}</h1> -->
            <!-- <h1>{{store.getLoginUser.userNumber}}번 사용자의 채팅방 목록</h1> -->
            
            <ul style="list-style:none; padding-inline-start: 0px;">
                <li v-for="item in chatroomList" :key="item">
                    <div :class="{ 'roomCard': true, 'selected': selectedItem === item }"
                    @click="selectChatroom(item)">
                        <div id="Thumbnail">
                            <img class="bidThumbnail" :src="item.thumbnail" alt="Thumbnail">
                        </div>
                        <div class="cardInfo">
                            <span class="cardName">
                                <h3 class="itemTitle">{{item.title}}</h3>
                                <p class="itemNickName">{{item.nickname}}</p>
                            </span>
                        </div>
                        <!-- <p>구매자: {{ item.buyerId }}</p> -->
                    </div>
                </li>
            </ul>
    </div>
    <div>
            <chatConversation
            v-bind:givenChatroomId="chatroomId"
            v-bind:givenBidId="bidId"
            v-bind:givenUserNumber="store.getLoginUser.userNumber"
            v-bind:givenBidTitle="bidTitle"
            class="component"/>
    </div>     
</template>

<script>
import chatConversation from "./chatConversation.vue";
import { useUsersStore } from '@/stores/users'; 

const API_URL="http://j10b105.p.ssafy.io:8080/api/chatroomlist";

export default{
    props:['givenBidId'],
    watch:{
        chatroomList: {
          handler() {
            this.scrollToBottom();
          },
          deep: true // 중첩된 객체도 감시
        },
    },
    components:{
        chatConversation,
    },
    mounted(){
        // 넘겨받은 bid id가 0이 아니라면 채팅방 생성 후 채팅방 불러오기
        console.log("chatroomList-bidId:"+this.givenBidId);
        console.log("로그인중인 user number:"+this.store.getLoginUser.userNumber);

        // this.userNumber=this.store.getLoginUser.userNumber;

        if(this.givenBidId!=0 && this.store.getLoginUser.userNumber){
            this.createChatrooms(this.givenBidId, this.store.getLoginUser.userNumber);
        }else{
            this.getChatrooms();
        }
    },
    data(){
        return {
            chatroomList:[],
            chatroomId: null,
            bidId:null,
            bidTitle:null,
            store: useUsersStore(),
            selectedItem: null,
        }
    },

    methods:{
        scrollToBottom() {
          // Vue.nextTick()을 사용하여 DOM 업데이트 이후에 스크롤을 아래로 이동
          this.$nextTick(() => {
            const cards = document.getElementById('chatroomCards');
            cards.scrollTop = cards.scrollHeight;
          });
        },
        getChatrooms(){
            // n번 user의 채팅방 목록 불러오기
            fetch(API_URL, {
                method: "GET",
                headers: {
                    'access_token': localStorage.getItem('access-token'),
                    "Content-Type": "application/json",
                },
                credentials: 'include',
            })
            .then((response) => {
                if (response.ok) {
                return response.json();
                }
                throw new Error("Network response was not ok.");
            })
            .then((data) => { // chatroom array
                for(var i=0;i<data.length;i++){
                    console.log("s3:"+data[i].item.thumbnail);
                    this.chatroomList.push({
                        chatroomId: data[i].chatroomId,
                        bidId: data[i].item.bidId,
                        buyerId: data[i].user.userNumber, // 구매자
                        sellerId: data[i].item.userNumber, // 판매자
                        thumbnail: data[i].item.thumbnail,
                        title: data[i].item.title,
                        nickname: data[i].item.nickname,
                });
                }
            })
            .catch((error) => {
                console.error("에러가 났어요.", error);
            });
        },
        selectChatroom(item){
            console.log("item:"+item.chatroomId);
            this.chatroomId=item.chatroomId;
            this.bidId=item.bidId;
            this.bidTitle=item.title;
            this.selectedItem=item;
        },
        createChatrooms(bidId, buyerId){
            fetch("http://j10b105.p.ssafy.io:8080/api/chatroom/create", {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
            },
            body: JSON.stringify({
                bidId: bidId,
                buyerId: buyerId
            })
          })  
          .then((response) => {
              if (response.ok) {
                console.log(buyerId+"번 user에 대한 "+bidId+"번 물품 문의방 생성 완료.");

                this.getChatrooms(); // 채팅방 생성 후 불러오기
              }else{
                if(response.status===400){
                    console.log("이미 존재하는 채팅방입니다.");
                    window.location.href="http://j10b105.p.ssafy.io:5173/chatPage";
                }
                throw new Error("Network response was not ok.");
              }
          })
          .catch((error) => {
              console.error("채팅방 생성 프로세스에서 문제가 생겼어요.", error);
          });
        },
    }
}
</script>
<style scopped>
#chatroomListBox{
    width:300px;
    border-right:1px solid black;
    overflow-x: hidden;
    overflow-y: scroll;
    transition: scroll-behavior 0.5s ease-in-out;
    /* border:1px solid pink; */
}
#chatroomListBox::-webkit-scrollbar {
    display: none;
}
.itemNickName{
    margin: 10px 0;
    font-size:small;
}
.itemTitle{
    margin: 0;
}
.cardInfo{

}
#Thumbnail{
    float:left;
}
.bidThumbnail{
    border-radius:50px;
    width:80px;
    height:80px;
    margin-right:20px;
}
.cardName{
    font-weight: bold;
    font-size: 18px;
    margin-bottom: 10px;
}
.cardBid{
    margin-top:10px;
}
#cardSeller{

}
.chatComponent{
    display:flex;
    flex-direction: row;
}
.roomCard{
    padding:20px;
    width:250px;
    height:70px;
    /* border-radius:15px; */
    border-bottom:1px solid rgba(0, 0, 0, 0.179);
    transition: background-color 0.3s ease;
}
.roomCard:hover, .roomCard.selected { /* 호버 및 선택 상태에 따른 스타일을 지정합니다. */
    background: linear-gradient(to right, #d7d7d7, #ffffff); /* 호버 및 선택 시 배경색을 변경합니다. */
}
</style>