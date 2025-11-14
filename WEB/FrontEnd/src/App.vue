<script setup>
import { RouterView } from 'vue-router'
import AnnouncementModal from './modules/common/components/AnnouncementModal.vue'
</script>

<template>
  <div id="app" v-activity-track="['click', 'touchstart', 'keydown', 'scroll']">
    <AnnouncementModal />
    <RouterView />
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style></style>
