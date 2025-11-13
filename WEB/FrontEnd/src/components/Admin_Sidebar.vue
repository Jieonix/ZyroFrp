<template>
  <nav class="sidebar">
    <ul>
      <li><router-link to="/Admin_Home" active-class="active">站点信息</router-link></li>
      <li><router-link to="/Admin_Users" active-class="active">用户信息管理</router-link></li>
      <li><router-link to="/Admin_Tunnels" active-class="active">隧道管理</router-link></li>
      <li><router-link to="/Admin_Auth" active-class="active">实名管理</router-link></li>
      <li><router-link to="/Admin_Announcements" active-class="active">公告管理</router-link></li>
      <li><router-link to="/Admin_Answer" active-class="active">答题管理</router-link></li>
      <li><router-link to="/Admin_Questions" active-class="active">题目管理</router-link></li>
      <li><router-link to="/Admin_EmailCode" active-class="active">验证码管理</router-link></li>
      <li><router-link to="/Admin_Servers" active-class="active">服务器管理</router-link></li>
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
    background-color: #1f1f1f;
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