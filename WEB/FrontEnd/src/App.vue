<script setup>
import { RouterView } from 'vue-router'
import AnnouncementModal from './components/AnnouncementModal.vue'
</script>

<template>
  <div id="app">
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
