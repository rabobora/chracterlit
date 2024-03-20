import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    nickname: sessionStorage.getItem('nickname') || null,
  }),
  actions: {
    login(nickname) {
      this.nickname = nickname;
      sessionStorage.setItem('nickname', nickname);
    },
    logout() {
      this.nickname = null;
      sessionStorage.removeItem('nickname');
    },
  },
});