<template>
    <header>
		<TheHeader />
	</header>
  chatView-받은 비드아이디: {{ bidId }}
    <div class="container">
        <div class="component">
            <chatroomList :givenBidId="bidId" />
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
.container{
  border:1px solid black;
  border-radius:17px;
}
.component{
  border:1px solid royalblue;
  border-radius:17px;
  height:500px;
}
</style>