<template>
    <header>
		<TheHeader />
	</header>
    <div class="container">
        <div class="component">
            <chatroomList />
        </div>
  </div>
</template>

<script>
import chatroomList from '@/components/chat/chatroomList.vue';
import TheHeader from '@/components/common/TheHeader.vue';

export default {
  name: 'App',
  components: {
    chatroomList,
    TheHeader,
    // chatConversation
  }
}
</script>

<style scopped>

</style>