<template>
  <div class="Admin_ServerManagement">
    <Header />
    <Admin_Sidebar />
    <main class="main-content">
      <section class="features">
        <div class="features-box">
          <h1>服务器管理</h1>
          <p>此功能正在开发中...</p>
        </div>
      </section>
      <Footer />
    </main>
  </div>
</template>

<script>
import Header from '@/modules/common/components/Header.vue';
import Admin_Sidebar from '@/modules/admin/components/Admin_Sidebar.vue';
import Footer from '@/modules/common/components/Footer.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';

export default {
  name: 'ServerManagement',
  components: {
    Header,
    Admin_Sidebar,
    Footer
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
  padding: 20px;
}
</style>