<template>
  <div class="container">
    <div class="component">
      <chatroomList />
    </div>
    <div class="component">
      <chatConversation />
    </div>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import chatConversation from './components/chatConversation.vue'
import chatroomList from './components/chatroomList.vue'
export default {
  name: 'App',
  components: {
    chatroomList,
    chatConversation
  }
}
</script>

<style scopped>
.container{
  display:flex;
}

.component{
  flex:1;
  margin-right:10px;
}
</style>
