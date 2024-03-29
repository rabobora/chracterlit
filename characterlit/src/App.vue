<template>
  <div class="container">
    <div class="component">
      <chatroomList />
    </div>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
// import chatConversation from './components/chatConversation.vue'
import chatroomList from './components/chatroomList.vue'
export default {
  name: 'App',
  components: {
    chatroomList,
    // chatConversation
  }
}
</script>

<style scopped>

</style>
