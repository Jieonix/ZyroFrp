<script setup lang="ts">
import SvgIcon from './SvgIcon.vue';

</script>

<template>
  <nav class="sidebar">
    <ul>
      <li>
        <router-link to="/Home" active-class="active">
          <SvgIcon name="Home" class="icon"></SvgIcon>仪表盘
        </router-link>
      </li>
      <li>
        <router-link to="/check_in" active-class="active">
          <SvgIcon name="CheckIn" class="icon"></SvgIcon>签到
        </router-link>
      </li>
      <li>
        <router-link to="/auth" active-class="active">
          <SvgIcon name="Auth" class="icon"></SvgIcon>身份认证
        </router-link>
      </li>
      <li>
        <router-link to="/tunnels" active-class="active">
          <SvgIcon name="Tunnels" class="icon"></SvgIcon>隧道列表
        </router-link>
      </li>
      <li>
        <router-link to="/add_tunnels" active-class="active">
          <SvgIcon name="AddTunnels" class="icon"></SvgIcon>添加隧道
        </router-link>
      </li>
      <li>
        <router-link to="/conf" active-class="active">
          <SvgIcon name="Conf" class="icon"></SvgIcon>配置文件
        </router-link>
      </li>
      <li>
        <router-link to="/app" active-class="active">
          <SvgIcon name="App" class="icon"></SvgIcon>应用程序
        </router-link>
      </li>
      <li>
        <router-link to="/usingTutorials" active-class="active">
          <SvgIcon name="Using" class="icon"></SvgIcon>使用教程
        </router-link>
      </li>
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
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 5px;
}

.icon {
  margin-right: 0.7rem;
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