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
  padding: 20px;
  width: 100%;
  margin-top: 20px;
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
  background-color: #101014;
  border-top: 1px solid #232323;
  color: #ededed;
}

  .footer a {
    color: #ededed;
  }

}

</style>