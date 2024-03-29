<template>
    <div id="chatroomListBox" class="component">
        <div>
            <h1>1번 사용자의 채팅방 목록</h1>
            <ul style="list-style:none;">
                <li v-for="item in chatroomList" :key="item">
                    <div class="roomCard" @click="selectChatroom(item.chatroomId)">
                        <p>{{item.chatroomId}}번 채팅방 물품 번호 {{ item.bidId }}/ 구매자: {{ item.buyerId }}, 판매자: {{ item.sellerId }}</p>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div>
            <chatConversation v-bind:givenChatroomId="chatroomId" class="component"/>
    </div>     
</template>

<script>
import chatConversation from "./chatConversation.vue";
const API_URL="http://localhost:8080/api/chatroomlist/";

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
        }
    },

    methods:{
        getChatrooms(){
            // n번 user의 채팅방 목록 불러오기
            fetch(API_URL + "1", {
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
            .then((data) => { // chatroom array
                for(var i=0;i<data.length;i++){
                    this.chatroomList.push({
                        chatroomId: data[i].chatroomId,
                        isPaid: data[i].isPaid,
                        bidId: data[i].bidId,
                        buyerId: data[i].buyerId,
                        sellerId: data[i].sellerId,
                });
                }
            })
            .catch((error) => {
                console.error("에러가 났어요.", error);
            });
        },
        selectChatroom(chatroomId){
            this.chatroomId=chatroomId;
            console.log("선택찬 채팅방 번호:"+chatroomId);
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