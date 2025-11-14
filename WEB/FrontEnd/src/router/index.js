import { createRouter, createWebHistory } from 'vue-router';
import LandingPage from '../modules/landing/views/LandingPage.vue';
import Home from '../modules/user/views/UserDashboard.vue';
import check_in from '../modules/user/views/UserCheckIn.vue';
import auth from '../modules/user/views/RealNameAuth.vue';
import tunnels from '../modules/user/views/TunnelList.vue';
import add_tunnels from '../modules/user/views/AddTunnel.vue';
import conf from '../modules/user/views/TunnelConfig.vue';
import app from '../modules/user/views/FRPDownloads.vue';
import usingTutorials from '../modules/user/views/UserTutorials.vue';
import Login from '../modules/auth/views/Login.vue';
import Register from '../modules/auth/views/Register.vue';
import ResetPassword from '../modules/auth/views/ResetPassword.vue';
import Payment from '../modules/pages/payment/views/Payment.vue';
import Admin_Home from '../modules/admin/views/Dashboard.vue';
import Admin_Users from '../modules/admin/views/UserManagement.vue';
import Admin_Email from '../modules/admin/views/EmailManagement.vue';
import Admin_Logs from '../modules/admin/views/LogManagement.vue';
import Admin_LogStatistics from '../modules/admin/views/Statistics.vue';
import Admin_Login from '../modules/auth/views/Admin_Login.vue';

// 导入缺失的管理员组件（占位）
import TunnelManagement from '../modules/admin/views/TunnelManagement.vue';
import AuthManagement from '../modules/admin/views/AuthManagement.vue';
import AnnouncementManagement from '../modules/admin/views/AnnouncementManagement.vue';
import AnswerManagement from '../modules/admin/views/AnswerManagement.vue';
import QuestionManagement from '../modules/admin/views/QuestionManagement.vue';
import EmailCodeManagement from '../modules/admin/views/EmailCodeManagement.vue';
import ServerManagement from '../modules/admin/views/ServerManagement.vue';

const routes = [
  {
    path: '/',
    name: 'LandingPage',
    component: LandingPage,
  },
  {
    path: '/user-dashboard',
    name: 'UserDashboard',
    component: Home,
  },
  {
    path: '/user-check-in',
    name: 'UserCheckIn',
    component: check_in,
  },
  {
    path: '/real-name-auth',
    name: 'RealNameAuth',
    component: auth,
  },
  {
    path: '/tunnel-list',
    name: 'TunnelList',
    component: tunnels,
  },
  {
    path: '/add-tunnel',
    name: 'AddTunnel',
    component: add_tunnels,
  }, {
    path: '/tunnel-config',
    name: 'TunnelConfig',
    component: conf,
  },
  {
    path: '/frp-downloads',
    name: 'FRPDownloads',
    component: app,
  }, {
    path: '/user-tutorials',
    name: 'UserTutorials',
    component: usingTutorials,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
  },
  {
    path: '/reset-password',
    name: 'ResetPassword',
    component: ResetPassword,
  },
  {
    path: '/payment',
    name: 'Payment',
    component: Payment,
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Admin_Home,
  },
  {
    path: '/user-management',
    name: 'UserManagement',
    component: Admin_Users,
  },
  {
    path: '/email-management',
    name: 'EmailManagement',
    component: Admin_Email,
  },
  {
    path: '/admin-login',
    name: 'Admin_Login',
    component: Admin_Login,
  },
  {
    path: '/log-management',
    name: 'LogManagement',
    component: Admin_Logs,
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Admin_LogStatistics,
  },
  {
    path: '/tunnel-management',
    name: 'TunnelManagement',
    component: TunnelManagement,
  },
  {
    path: '/auth-management',
    name: 'AuthManagement',
    component: AuthManagement,
  },
  {
    path: '/announcement-management',
    name: 'AnnouncementManagement',
    component: AnnouncementManagement,
  },
  {
    path: '/answer-management',
    name: 'AnswerManagement',
    component: AnswerManagement,
  },
  {
    path: '/question-management',
    name: 'QuestionManagement',
    component: QuestionManagement,
  },
  {
    path: '/email-code-management',
    name: 'EmailCodeManagement',
    component: EmailCodeManagement,
  },
  {
    path: '/server-management',
    name: 'ServerManagement',
    component: ServerManagement,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  base: 'http://localhost:8085/',
  routes,
});

export default router;