<template>
    <div id="chatroomListBox">
        <h1>105번 사용자의 채팅방 목록</h1>
        <ul style="list-style:none;">
            <li v-for="item in chatroomList" :key="item">
                <div class="bubble">
                    <p class="roomCard">{{item.chatroomId}}번 채팅방 물품 번호 {{ item.bidId }}/ 구매자: {{ item.buyerId }}, 판매자: {{ item.sellerId }}</p>
                </div>
            </li>
        </ul>
    </div>
</template>
<script>
const API_URL="http://localhost:8080/api/chatroomlist/";

export default{
    mounted(){
        this.getChatrooms();
    },
    data(){
        return {
            chatroomList:[],
        }
    },

    methods:{
        getChatrooms(){
            // n번 user의 채팅방 목록 불러오기
            fetch(API_URL + "105", {
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
        }
    }
}
</script>
<style scopped>
#chatroomListBox{
    border:1px solid pink;
}
.roomCard{
    border-radius:20px;
    border:1px solid black;
}
</style>