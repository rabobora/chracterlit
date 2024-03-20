<template>
    <div>
      <h1>메인페이지</h1>
      <p>{{ nickname }}님 환영합니다!</p>
    </div>
    <div v-for="num in 5" :key="num">
      <router-link :to="`/read/${num}`">Read Page {{ num }}</router-link>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue';
  
  export default {
    name: 'HomeView',
    setup() {
      const nickname = ref('');
  
      // 닉네임 생성 함수
      function generateNickname() {
        const adjectives = ["fast", "slow", "big", "small", "light", "dark", "happy", "sad"];
        const nouns = ["bear", "fox", "lion", "tiger", "rabbit", "deer", "wolf", "cat"];
        const adjective = adjectives[Math.floor(Math.random() * adjectives.length)];
        const noun = nouns[Math.floor(Math.random() * nouns.length)];
        const number = Math.floor(Math.random() * 100);
        return `${adjective}${noun}${number}`;
      }
  
      // 세션에서 닉네임을 로드하거나 새로운 닉네임을 생성하여 저장
      onMounted(() => {
        let sessionNickname = sessionStorage.getItem('nickname');
        if (!sessionNickname) {
          sessionNickname = generateNickname();
          sessionStorage.setItem('nickname', sessionNickname);
        }
        nickname.value = sessionNickname;
      });
  
      return {
        nickname
      };
    }
  };
  </script>
  