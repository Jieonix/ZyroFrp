<template>
  <header class="header">
    <h1>ZyroFrp</h1>
  </header>
</template>

<style scoped>
.header {
  background-color: #ffffff;
  padding: 10px;
  text-align: left;
  font-size: 0.8em;
  padding-left: 20px;
  color: #006412;
  font-weight: 1500;
  border-bottom: 1px solid #e6e6e6;
  position: fixed;
  z-index: 100;
  width: 100vw;
  box-sizing: border-box;
}

@media (prefers-color-scheme: dark) {

  .header {
    background-color: #18181c;
    border-bottom: 1px solid #232323;
    color: #5ccea8;
  }

}
</style>