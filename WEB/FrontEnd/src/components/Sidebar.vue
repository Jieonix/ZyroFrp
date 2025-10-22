<template>
  <nav class="sidebar">
    <ul>
      <li><router-link to="/" active-class="active">仪表盘</router-link></li>
      <li><router-link to="/check_in" active-class="active">签到</router-link></li>
      <li><router-link to="/auth" active-class="active">身份认证</router-link></li>
      <li><router-link to="/tunnels" active-class="active">隧道列表</router-link></li>
      <li><router-link to="/add_tunnels" active-class="active">添加隧道</router-link></li>
      <li><router-link to="/conf" active-class="active">配置文件</router-link></li>
      <li><router-link to="/app" active-class="active">应用程序</router-link></li>
      <li><router-link to="/usingTutorials" active-class="active">使用教程</router-link></li>
    </ul>
  </nav>
</template>

<style scoped>
.sidebar {
  width: 18rem;
  background-color: #ffffff;
  padding: 8px;
  border-right: 1px solid #e6e6e6;
  height: 100vh;
  position: fixed;
  padding-top: 68px;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar ul li {
  margin-bottom: 5px;
  transition: all 0.3s;
  cursor: pointer;
  border-radius: 5px;
  padding: 10px 20px;
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
  padding: 0;
}

.sidebar ul li:hover {
  background-color: #f3f3f5;
}

.sidebar ul li a {
  display: block;
  text-decoration: none;
  color: #333;
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 5px;
}

.sidebar ul li a.active {
  color: #00910c;
  background-color: #e7f5ee;
}

@media (prefers-color-scheme: dark) {

  .sidebar {
    background-color: #18181c;
    border-right: 1px solid #232323;
  }

  .sidebar ul li a {
    color: #ededed;
  }

  .sidebar ul li:hover {
    background-color: #464646;
  }

  .sidebar ul li a.active {
    color: #5ccea8;
    background-color: #233633;
  }

}

@media (max-width: 768px) {}
</style>