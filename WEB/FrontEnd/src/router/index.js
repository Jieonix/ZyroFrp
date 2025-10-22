import { createRouter, createWebHistory } from 'vue-router';
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
import Admin_Home from '@/views/Admin/Admin_Home.vue';
import Admin_Users from '@/views/Admin/Admin_Users.vue';
import Admin_Login from '@/views/Auth/Admin_Login.vue';

const routes = [
  {
    path: '/',
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
    path: '/Admin_Login',
    name: 'Admin_Login',
    component: Admin_Login,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  base: 'http://localhost:8085/',
  routes,
});

export default router;