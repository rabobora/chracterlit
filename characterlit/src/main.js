import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

function initializeSession() {
    if (!sessionStorage.getItem('userSession')) {
      const sessionId = Math.floor(1000000000000000 + Math.random() * 9000000000000000);
      sessionStorage.setItem('userSession', sessionId);
      }
    }  
  
    initializeSession();

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')