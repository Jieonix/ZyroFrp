<template>
  <svg class="icon" aria-hidden="true">
    <use :xlink:href="`#icon-${name}`" />
  </svg>
</template>

<script>
export default {
  props: ['name'],
}
</script>

<style>
.icon {
  width: 1rem;
  height: 1rem;
  fill: currentColor;
}
</style>