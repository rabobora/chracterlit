<template>
    <header>
		<TheHeader />
	</header>
    <div class="container">
        <div class="chatComponent">
            <chatroomList :givenBidId="bidId"/>
        </div>
  </div>
</template>

<script>
import chatroomList from '@/components/chat/chatroomList.vue';
import TheHeader from '@/components/common/TheHeader.vue';

export default {
  props: {
    bidId: {
      type: Number,
      default:0
    }
  },
  methods:{
    mounted(){
      console.log("received bid id: at chatView"+this.bidId);
    },
  },
  name: 'App',
  components: {
    chatroomList,
    TheHeader,
    // chatConversation
  },
  mounted(){
    console.log("chatview-bidid:"+this.bidId);
  },
}
</script>

<style scopped>
#chatroomListComponent{
  float:left;
}
.container{
  border:1px solid black;
  border-radius:17px;
}
.chatComponent{
  border:1px solid black;
  border-radius:17px;
  height:500px;
}
</style>