<script setup>
import Loading from '@/components/Loading.vue'
</script>

<template>
  <div class="check_in">
    <Loading />
    <Header />
    <div class="container">
      <Sidebar />
      <main class="main-content">
        <section class="welcome">
          <h2>欢迎来到 FRP 管理平台</h2>
          <p>在这里你可以答题获得高额流量，答对答错都有奖励！</p>
        </section>
        <section class="features">
          <div class="feature-box fb1">
            <div v-if="!isQuiz">
              <h3 class="fb1-h3">每日答题（获得流量）</h3>
              <p class="fb1-p">{{ questionText }}</p>
              <pre class="fb1-pre">
A、{{ questionA }}
B、{{ questionB }}
C、{{ questionC }}
D、{{ questionD }}
              </pre>
              <div class="form-group">
                <select v-model="answer" name="question" id="question" class="select-box">
                  <option value="">请选择</option>
                  <option value="A">A</option>
                  <option value="B">B</option>
                  <option value="C">C</option>
                  <option value="D">D</option>
                </select>
                <button @click="submitAnswer" class="primary-button">提交</button>
              </div>
            </div>
            <div v-else>
              <h1 class="Answered_h1">今日已答题 ！</h1>
              <h3 class="Answered_h3">{{ randomMsg }}</h3>
              <img class="Emoticon" src="/public/Emoticon.png" alt="">
            </div>
          </div>
        </section>
        <Footer />
      </main>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import Footer from '@/components/Footer.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '../utils/token.js';
import axios from 'axios';
import { useLoadingStore } from '@/stores/loading'


export default {
  name: 'check_in',
  components: {
    Header,
    Sidebar,
    Footer,
  },
  data() {
    return {
      questionText: '',
      questionA: '',
      questionB: '',
      questionC: '',
      questionD: '',
      answer: 'A',
      isQuiz: false,
      messages: [
        "哥，今天题目你已经拿捏了，别卷啦，留点实力明天用！",
        "今日份脑细胞已消耗完毕，请明天继续充值～",
        "你已经超越全国99%的答题小能手，系统劝你早点休息~",
        "别急，明天的奖励更香，今天的你只配摸鱼！",
        "题目已阵亡，哥请明日再来收割～",
        "今日答题额度已用光，赶紧去喝杯奶茶犒劳自己~",
        "系统提示：答太多会秃头，明天再来不迟~",
        "哥，休战吧，服务器都累得打哈欠了~",
        "你赢了，但今天的题库已经Game Over！",
        "奖励小仓库满了，明天继续薅羊毛！"
      ],
      randomMsg: '',
    }
  },
  methods: {
    checkTokenValidity() {
      const router = useRouter();
      const token = localStorage.getItem("Token");
      if (!validateToken(router, token)) {
        return;
      }
    },
    async fetchQuestion() {
      try {
        const loadingStore = useLoadingStore();
        loadingStore.showLoading();
        const response = await axios.get('/quiz/today');
        loadingStore.hideLoading();

        this.question = response.data.data;
        this.questionText = this.question.question_text;
        this.questionA = this.question.option_a;
        this.questionB = this.question.option_b;
        this.questionC = this.question.option_c;
        this.questionD = this.question.option_d;
      } catch (error) {
        console.error('Error fetching question:', error);
      }
    },
    async cheekTodayQuizStatus() {
      try {
        const token = localStorage.getItem('Token');
        const loadingStore = useLoadingStore();
        loadingStore.showLoading();
        const response = await axios.get('/quiz/todayStatus', {
          headers: {
            Token: token
          }
        })
        loadingStore.hideLoading();

        this.isQuiz = response.data.data;

      } catch (error) {
        console.error('Error fetching question:', error);
      }
    },
    async submitAnswer() {
      if (!this.answer) {
        this.$message.warning('请选择一个答案');
        return;
      }

      const token = localStorage.getItem('Token');
      const userAnswer = this.answer;
      const quizErrorCodes = [
        "QUIZ_4503",
        "QUIZ_4504"
      ];

      try {
        const response = await axios.post('/quiz/answer', {
          token,
          userAnswer,
        });

        if (quizErrorCodes.includes(response.data.code)) {
          this.$message.error(response.data.message);
          return;
        }

        this.$message.success(response.data.data);

        setTimeout(() => {
          window.location.reload()
        }, 1500)

      } catch (error) {
        console.error('提交答案失败:', error);
        this.$message.error('提交失败，请稍后再试');
      }
    },
    created() {
      this.randomMsg = this.messages[Math.floor(Math.random() * this.messages.length)]
    },
  },
  mounted() {
    this.checkTokenValidity();
    this.fetchQuestion();
    this.cheekTodayQuizStatus();
    this.created();
  }
}
</script>


<style scoped>
.fb1 {
  margin: 0 1rem 44.2rem 1rem;
  height: auto;
  padding-left: 3rem;
  width: calc(100vw - 18rem - 2rem);
}

.fb1-p {
  margin-top: 20px;
  font-size: 20px;
  margin-bottom: 5px;
  font-weight: 700;
}

.fb1-pre {
  line-height: 2;
  height: 130px;
}

.form-group {
  display: flex;
  align-items: center;
}

.select-box {
  height: 30px;
  width: 100px;
  padding-left: 25px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px 0 0 5px;
  appearance: none;
  -webkit-appearance: none;
  background-color: #fff;
  box-sizing: border-box;
  margin-top: 20px;
  margin-left: -1px;
}

.select-box:focus {
  outline: none;
}

.primary-button {
  height: 1.8rem;
  width: 3rem;
  font-size: 0.7rem;
  font-weight: 700;
  color: #fff;
  background-color: #42997c;
  border: none;
  border-radius: 0 0.2rem 0.2rem 0;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 20px;
  box-sizing: border-box;
  padding: 0;
}

.primary-button:hover {
  background-color: #5acaa5;
}

#question {
  padding-left: 45px;
}

.Emoticon {
  height: 10rem;
  margin-top: 1rem;
}

.Answered_h1 {
  margin-bottom: 10px;
  font-weight: 500;
}

.Answered_h3 {
  font-weight: 400;
}

@media (prefers-color-scheme: dark) {

  body {
    background-color: #1c1c1c;
    color: #e0e0e0;
  }

  .main-content {
    background-color: #101014;
  }

  h2 {
    color: #ededed;
  }

  .feature-box {
    background-color: #18181c;
    border: 1px solid #232323;
  }

  .select-box {
    background-color: #505050;
    color: #ededed;
    border: 1px solid #333;
  }

  .primary-button {
    color: #fff;
    background-color: #236659;
  }

  .primary-button:hover {
    background-color: #195549;
  }

}
</style>
