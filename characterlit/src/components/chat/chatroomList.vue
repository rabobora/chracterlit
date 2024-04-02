<template>
    <div id="chatroomListBox" class="component">
        <div>
            <h1> login 한 user Number 테스트{{ store.getLoginUser.userNumber }}</h1>
            <h1>{{store.getLoginUser.userNumber}}번 사용자의 채팅방 목록</h1>
            <ul style="list-style:none;">
                <li v-for="item in chatroomList" :key="item">
                    <div class="roomCard" @click="selectChatroom(item.chatroomId, item.bidId)">
                        <p>{{item.chatroomId}}번 채팅방</p>
                        <p>물품 번호 {{ item.bidId }}</p>
                        <p>구매자: {{ item.buyerId }}</p>
                        <p>판매자: {{ item.sellerId }}</p>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div>
            <chatConversation v-bind:givenChatroomId="chatroomId"
            v-bind:givenBidId="bidId" class="component"/>
    </div>     
</template>

<script>
import chatConversation from "./chatConversation.vue";
import { useUsersStore } from '@/stores/users'; 

const API_URL="http://localhost:8080/api/chatroomlist";

export default{
    components:{
        chatConversation,
    },
    mounted(){
        this.getChatrooms();
    },
    data(){
        return {
            chatroomList:[],
            chatroomId: null,
            bidId:null,
            store: useUsersStore(),
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
        }
    }
}
</script>
<style scopped>
.component{
    display:flex;
    flex-direction: row;
}
#chatroomListBox{
    border:1px solid pink;
}
.roomCard{
    padding:20px;
    border-radius:15px;
    border:1px solid black;
}
</style>