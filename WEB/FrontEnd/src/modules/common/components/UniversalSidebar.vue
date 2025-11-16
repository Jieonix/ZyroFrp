<template>
  <nav class="sidebar">
    <ul>
      <template v-for="item in menuItems" :key="item.name">
        <li v-if="item.type === 'group'" class="menu-group">
          {{ item.label }}
        </li>

        <li v-else>
          <router-link :to="{ name: item.route }" active-class="active" :class="item.class">
            <SvgIcon v-if="item.icon" :name="item.icon" :class="['icon', item.iconClass]"></SvgIcon>
            {{ item.label }}
          </router-link>
        </li>
      </template>
    </ul>
  </nav>
</template>

<script setup>
import { computed } from 'vue'
import SvgIcon from '@/modules/common/components/SvgIcon.vue'

const props = defineProps({
  type: {
    type: String,
    required: true,
    validator: (value) => ['user', 'admin'].includes(value)
  }
})

const userMenuItems = [
  {
    name: 'dashboard',
    type: 'item',
    label: '仪表盘',
    route: 'UserDashboard',
    icon: 'Home',
    class: 'with-icon'
  },
  {
    name: 'checkin',
    type: 'item',
    label: '签到',
    route: 'UserCheckIn',
    icon: 'CheckIn',
    class: 'with-icon'
  },
  {
    name: 'auth',
    type: 'item',
    label: '身份认证',
    route: 'RealNameAuth',
    icon: 'Auth',
    class: 'with-icon'
  },
  {
    name: 'tunnels',
    type: 'item',
    label: '隧道列表',
    route: 'TunnelList',
    icon: 'Tunnels',
    class: 'with-icon'
  },
  {
    name: 'add-tunnel',
    type: 'item',
    label: '添加隧道',
    route: 'AddTunnel',
    icon: 'AddTunnels',
    class: 'with-icon'
  },
  {
    name: 'tunnel-config',
    type: 'item',
    label: '配置文件',
    route: 'TunnelConfig',
    icon: 'Conf',
    class: 'with-icon'
  },
  {
    name: 'applications',
    type: 'item',
    label: '应用程序',
    route: 'FRPDownloads',
    icon: 'App',
    class: 'with-icon'
  },
  {
    name: 'tutorials',
    type: 'item',
    label: '使用教程',
    route: 'UserTutorials',
    icon: 'Using',
    class: 'with-icon'
  }
]

const adminMenuItems = [
  {
    name: 'site-info',
    type: 'item',
    label: '站点信息',
    route: 'Dashboard'
  },
  {
    name: 'user-management',
    type: 'item',
    label: '用户信息管理',
    route: 'UserManagement'
  },
  {
    name: 'tunnel-management',
    type: 'item',
    label: '隧道管理',
    route: 'TunnelManagement'
  },
  {
    name: 'auth-management',
    type: 'item',
    label: '实名管理',
    route: 'AuthManagement'
  },
  {
    name: 'announcement-management',
    type: 'item',
    label: '公告管理',
    route: 'AnnouncementManagement'
  },
  {
    name: 'answer-management',
    type: 'item',
    label: '答题管理',
    route: 'AnswerManagement'
  },
  {
    name: 'question-management',
    type: 'item',
    label: '题目管理',
    route: 'QuestionManagement'
  },
  {
    name: 'email-management',
    type: 'item',
    label: '邮件群发',
    route: 'EmailManagement'
  },
  {
    name: 'email-code-management',
    type: 'item',
    label: '验证码管理',
    route: 'EmailCodeManagement'
  },
  {
    name: 'server-management',
    type: 'item',
    label: '服务器管理',
    route: 'ServerManagement'
  },
  {
    name: 'log-management',
    type: 'item',
    label: '操作日志',
    route: 'LogManagement'
  },
  {
    name: 'statistics',
    type: 'item',
    label: '日志统计',
    route: 'Statistics'
  }
]

const menuItems = computed(() => {
  return props.type === 'admin' ? adminMenuItems : userMenuItems
})

const sidebarClass = computed(() => {
  return {
    'sidebar': true,
    'sidebar-user': props.type === 'user',
    'sidebar-admin': props.type === 'admin'
  }
})
</script>

<style scoped>
.sidebar {
  width: 18rem;
  background-color: #ffffff;
  padding: 8px;
  border-right: 1px solid #e6e6e6;
  height: 100vh;
  position: fixed;
  padding-top: 68px;
  box-sizing: border-box;
}

.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar ul li {
  margin-bottom: 5px;
  transition: all 0.3s ease;
  cursor: pointer;
  border-radius: 5px;
  padding: 0;
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
}

.sidebar ul li.menu-group {
  font-weight: bold;
  font-size: 16px;
  color: #666;
  padding: 15px 20px 5px 20px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 2px;
  cursor: default;
  background: none;
  border-radius: 0;
}

.sidebar ul li.menu-group:hover {
  background: none;
  color: #666;
}

.sidebar ul li:hover {
  background-color: #f3f3f5;
}

.sidebar ul li:hover.menu-group {
  background-color: transparent;
}

.sidebar ul li a {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 5px;
  transition: all 0.2s ease;
}

.sidebar-admin .sidebar ul li a {
  display: block;
}

.icon {
  margin-right: 0.7rem;
  flex-shrink: 0;
  font-size: 16px;
}

.sidebar ul li a.active {
  color: #00910c;
  background-color: #e7f5ee;
}

.sidebar ul li a:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.sidebar ul li a.active:hover {
  background-color: #d4ede1;
}

@media (prefers-color-scheme: dark) {
  .sidebar {
    background-color: var(--sidebar-bg-dark, #18181c);
    border-right: 1px solid var(--sidebar-border-dark, #232323);
  }

  .sidebar ul li a {
    color: var(--sidebar-text-dark, #ededed);
  }

  .sidebar ul li:hover {
    background-color: var(--sidebar-hover-dark, #464646);
  }

  .sidebar ul li.menu-group {
    color: var(--sidebar-group-text-dark, #999);
  }

  .sidebar ul li.menu-group:hover {
    color: var(--sidebar-group-text-dark, #999);
    background: none;
  }

  .sidebar ul li a.active {
    color: var(--sidebar-active-text-dark, #5ccea8);
    background-color: var(--sidebar-active-bg-dark, #233633);
  }

  .sidebar ul li a:hover {
    background-color: rgba(255, 255, 255, 0.05);
  }

  .sidebar ul li a.active:hover {
    background-color: rgba(92, 206, 168, 0.15);
  }
}

@media (prefers-color-scheme: dark) {
  .sidebar ul li a:focus-visible {
    outline-color: #5ccea8;
  }
}
</style>