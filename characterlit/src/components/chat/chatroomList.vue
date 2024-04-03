<template>
    <div id="chatroomListBox" class="component">
        <!-- List-넘겨받은 bidid:{{ givenBidId }} -->
        <div>
            <!-- <h1> login 한 user Number 테스트{{ store.getLoginUser.userNumber }}</h1> -->
            <!-- <h1>{{store.getLoginUser.userNumber}}번 사용자의 채팅방 목록</h1> -->
            
            <ul style="list-style:none; padding-inline-start: 0px;">
                <li v-for="item in chatroomList" :key="item">
                    <div class="roomCard" @click="selectChatroom(item.chatroomId, item.bidId)">
                        <div id="cardName">{{item.chatroomId}}번 채팅방</div>
                        <div id="cardBid">물품 번호{{ item.bidId }}</div>
                        <div id="cardSeller">판매자: {{ item.sellerId }}</div>
                        <!-- <p>구매자: {{ item.buyerId }}</p> -->
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div>
            <chatConversation
            v-bind:givenChatroomId="chatroomId"
            v-bind:givenBidId="bidId"
            v-bind:givenUserNumber="store.getLoginUser.userNumber"
            class="component"/>
    </div>     
</template>

<script>
import chatConversation from "./chatConversation.vue";
import { useUsersStore } from '@/stores/users'; 

const API_URL="http://localhost:8080/api/chatroomlist";

export default{
    props:['givenBidId'],
    components:{
        chatConversation,
    },
    mounted(){
        // 넘겨받은 bid id가 0이 아니라면 채팅방 생성 후 채팅방 불러오기
        console.log("chatroomList-bidId:"+this.givenBidId);
        console.log("로그인중인 user number:"+this.store.getLoginUser.userNumber);

        // this.userNumber=this.store.getLoginUser.userNumber;

        if(this.givenBidId!=0){
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
            store: useUsersStore(),
            // userNumber: null,
        }
    },

    methods:{
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
                    this.chatroomList.push({
                        chatroomId: data[i].chatroomId,
                        bidId: data[i].item.bidId,
                        buyerId: data[i].user.userNumber, // 구매자
                        sellerId: data[i].item.userNumber, // 판매자
                });
                }
            })
            .catch((error) => {
                console.error("에러가 났어요.", error);
            });
        },
        selectChatroom(chatroomId, bidId){
            this.chatroomId=chatroomId;
            this.bidId=bidId;
            console.log("선택찬 채팅방 번호:"+chatroomId);
            console.log("선택한 채팅방의 bid id:"+bidId);
        },
        createChatrooms(bidId, buyerId){
            fetch("http://localhost:8080/api/chatroom/create", {
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
#cardName{
    font-weight: bold;
    font-size: 18px;
    margin-bottom: 10px;
}
#cardBid{

}
#cardSeller{

}
.component{
    display:flex;
    flex-direction: row;
}
#chatroomListBox{
    width:300px;
    border:1px solid pink;
}
.roomCard{
    padding:20px;
    width:250px;
    height:80px;
    border-radius:15px;
    border:1px solid black;
}
</style>