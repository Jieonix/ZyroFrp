<script setup>
import Loading from '@/modules/common/components/Loading.vue'
</script>

<template>
  <div class="Admin_Home">
    <Loading />
    <Header />
      <Admin_Sidebar />
      <main class="main-content">
        <Loading />
        <section class="features">
          <div class="features-box">

          </div>
        </section>
        <Footer />
      </main>
  </div>
</template>

<script>
import Header from '@/modules/common/components/Header.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import { ref } from 'vue';
import axios from 'axios';
import Admin_Sidebar from '@/modules/admin/components/Admin_Sidebar.vue';
import Footer from '@/modules/common/components/Footer.vue';
import { useLoadingStore } from '@/modules/common/stores/loading'


export default {
  name: 'Home',
  components: {
    Header,
    Admin_Sidebar,
  },
  data() {
    const isDarkMode = window.matchMedia("(prefers-color-scheme: dark)").matches;

    return {

    };
  },
  methods: {
    checkTokenValidity() {
      const router = useRouter();
      const AdminToken = localStorage.getItem("AdminToken");
      if (!validateToken(router, AdminToken, true)) {
        return;
      }
    },
  },
  mounted() {
    this.checkTokenValidity();
  },
}
</script>


<style scoped>
.features-box {
  min-height: 91vh;
}

</style>