<template>
  <footer class="footer">
    <p>© 2025 zyrooTeam 保留所有权利</p>
    <p><a href="">服务政策</a> ｜ <a href="">隐私条款</a></p>
  </footer>
</template>

<style scoped>
.footer {
  background-color: #fff;
  border-top: 1px solid #e6e6e6;
  color: #333;
  text-align: center;
  padding: 0.7rem;
  width: 100vw;
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 1000;
  height: 3.5rem;
  font-size: 0.6rem;
  box-sizing: border-box;
}

footer a {
  color: #333;
  text-decoration: none;
  transition: all 0.3s;
  font-weight: bold;
  z-index: 100;
}

footer a:hover {
  color: #ff9900;
}

@media (prefers-color-scheme: dark) {

  .footer {
    background-color: #18181c;
    border-top: 1px solid #232323;
    color: #ededed;
  }

  .footer a {
    color: #ededed;
  }

}
</style>