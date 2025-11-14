import { createRouter, createWebHistory } from 'vue-router';
import LandingPage from '../views/LandingPage.vue';
import Home from '../views/Home.vue';
import check_in from '../views/check_in.vue';
import auth from '../views/auth.vue';
import tunnels from '../views/tunnels.vue';
import add_tunnels from '../views/add_tunnels.vue';
import conf from '../views/conf.vue';
import app from '../views/app.vue';
import usingTutorials from '../views/usingTutorials.vue';
import Login from '../views/Auth/Login.vue';
import Register from '../views/Auth/Register.vue';
import ResetPassword from '../views/Auth/ResetPassword.vue';
import Payment from '../views/Payment.vue';
import Admin_Home from '@/views/Admin/Admin_Home.vue';
import Admin_Users from '@/views/Admin/Admin_Users.vue';
import Admin_Email from '@/views/Admin/Admin_Email.vue';
import Admin_Logs from '@/views/Admin/Admin_Logs.vue';
import Admin_LogStatistics from '@/views/Admin/Admin_LogStatistics.vue';
import Admin_Login from '@/views/Auth/Admin_Login.vue';

const routes = [
  {
    path: '/',
    name: 'LandingPage',
    component: LandingPage,
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
  },
  {
    path: '/check_in',
    name: 'check_in',
    component: check_in,
  },
  {
    path: '/auth',
    name: 'auth',
    component: auth,
  },
  {
    path: '/tunnels',
    name: 'tunnels',
    component: tunnels,
  },
  {
    path: '/add_tunnels',
    name: 'add_tunnels',
    component: add_tunnels,
  }, {
    path: '/conf',
    name: 'conf',
    component: conf,
  },
  {
    path: '/app',
    name: 'app',
    component: app,
  }, {
    path: '/usingTutorials',
    name: 'usingTutorials',
    component: usingTutorials,
  },
  {
    path: '/Login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/Register',
    name: 'Register',
    component: Register,
  },
  {
    path: '/ResetPassword',
    name: 'ResetPassword',
    component: ResetPassword,
  },
  {
    path: '/payment',
    name: 'Payment',
    component: Payment,
  },
  {
    path: '/Admin_Home',
    name: 'Admin_Home',
    component: Admin_Home,
  },
  {
    path: '/Admin_Users',
    name: 'Admin_Users',
    component: Admin_Users,
  },
  {
    path: '/Admin_Email',
    name: 'Admin_Email',
    component: Admin_Email,
  },
  {
    path: '/Admin_Login',
    name: 'Admin_Login',
    component: Admin_Login,
  },
  {
    path: '/Admin_Logs',
    name: 'Admin_Logs',
    component: Admin_Logs,
  },
  {
    path: '/Admin_LogStatistics',
    name: 'Admin_LogStatistics',
    component: Admin_LogStatistics,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  base: 'http://localhost:8085/',
  routes,
});

export default router;