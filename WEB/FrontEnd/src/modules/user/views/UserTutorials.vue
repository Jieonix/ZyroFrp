<script setup>
import Loading from '@/modules/common/components/Loading.vue'
</script>

<template>
  <div class="auth">
    <Loading />
    <Header />
    <Sidebar />
    <main class="main-content">
      <section class="welcome">
        <h2>欢迎来到 FRP 管理平台</h2>
        <p>在这里你可以查看 FRPC 客户端使用教程</p>
      </section>
      <section class="features">
        <div class="feature-box fb1">
          <div class="h3">
            <h3 class="h3_1">使用帮助</h3>
          </div>

          <div class="line">
            <strong class="dowm">
              <SvgIcon name="Confirm" class="icon" />
            </strong>
            <p class="tooltip"><strong>创建隧道</strong></p>
          </div>

          <div class="arrow">
            |
          </div>

          <div class="line">
            <strong class="dowm">
              <SvgIcon name="Confirm" class="icon" />
            </strong>
            <p class="tooltip"><strong>软件下载</strong></p>
          </div>

          <div class="arrow">
            |
          </div>

          <div class="line">
            <strong class="dowm">
              <SvgIcon name="Confirm" class="icon" />
            </strong>
            <p class="tooltip"><strong>启动隧道</strong></p>
          </div>

          <div class="arrow">
            |
          </div>

          <div class="line">
            <strong class="dowm">
              <SvgIcon name="Confirm2" class="icon2" />
            </strong>
            <p class="tooltip"><strong>开始使用</strong></p>
          </div>
        </div>
        <div class="feature-box fb2">
          <div class="h3">
            <h3 class="h3_2">保姆级教程</h3>
          </div>
          <div class="tutorial-grid">
            <div class="tutorial-card">
              <div class="tutorial-title">
                <h4>Linux amd64</h4>
                <span class="chip">amd64</span>
              </div>
              <div v-if="tutorialMedias.linux_amd64 && tutorialMedias.linux_amd64.length" class="media-preview"
                @click="openMedia('linux_amd64', 0)">
                <img :src="tutorialMedias.linux_amd64[0].src" :alt="tutorialMedias.linux_amd64[0].alt" loading="lazy" />
                <button class="play-btn" type="button" aria-label="播放演示">▶</button>
              </div>
              <p class="media-note" v-if="tutorialMedias.linux_amd64 && tutorialMedias.linux_amd64.length">演示系统：Ubuntu
                22.04.5</p>
              <ol>
                <li>在<router-link class="app-link" :to="{ name: 'FRPDownloads' }">“应用程序”</router-link>页面获取
                  <code>ZyroFrp_linux_amd64</code>，直接解压，或执行
                  <code>tar -zxvf ZyroFrp_linux_amd64.tar.gz</code> 解压。
                </li>
                <li>在解压目录 amd64 内找到 <code>frpc.toml</code>，将面板生成的隧道配置原样粘贴保存。</li>
                <li>（1）后台运行（推荐）：
                  <code>nohup ./frpc -c ./frpc.toml &gt;/var/log/frpc.log 2&gt;&amp;1 &amp;</code>
                  终端关闭也不受影响。
                </li>
                <li>（报错解决）若提示 <code>nohup: command not found</code>，先安装（按系统任选其一）：
                  <ul>
                    <li>Debian/Ubuntu：
                      <code>sudo apt update && sudo apt install coreutils -y</code>
                    </li>
                    <li>CentOS/Rocky：
                      <code>sudo yum install coreutils -y</code>
                    </li>
                  </ul>
                </li>
                <li>（查看状态）查看后台日志并退出查看：
                  <ul>
                    <li>实时查看日志输出：
                      <code>tail -f /var/log/frpc.log</code>
                    </li>
                    <li>退出日志查看（按键）：
                      <code>Ctrl + C</code>
                    </li>
                  </ul>
                </li>
                <li>（2）前台运行测试（终端不能关，不推荐）：
                  <code>cd amd64 && chmod +x ./frpc && ./frpc -c ./frpc.toml</code>
                </li>
                <li style="font-weight: 900;">都不行？还是有问题？找我们解决，QQ群：738146595</li>
              </ol>
            </div>
            <div class="tutorial-card">
              <div class="tutorial-title">
                <h4>Linux arm64</h4>
                <span class="chip">arm64</span>
              </div>
              <div v-if="tutorialMedias.linux_arm64 && tutorialMedias.linux_arm64.length" class="media-preview"
                @click="openMedia('linux_arm64', 0)">
                <img :src="tutorialMedias.linux_arm64[0].src" :alt="tutorialMedias.linux_arm64[0].alt" loading="lazy" />
                <button class="play-btn" type="button" aria-label="播放演示">▶</button>
              </div>
              <p class="media-note" v-if="tutorialMedias.linux_arm64 && tutorialMedias.linux_arm64.length">演示系统：Ubuntu
                22.04.5</p>
              <ol>
                <li>在<router-link class="app-link" :to="{ name: 'FRPDownloads' }">“应用程序”</router-link>页面获取
                  <code>ZyroFrp_linux_arm64</code>，直接解压，或执行
                  <code>tar -zxvf ZyroFrp_linux_arm64.tar.gz</code> 解压。
                </li>
                <li>在解压目录 arm64 内找到 <code>frpc.toml</code>（或自行新建），将面板生成的隧道配置原样粘贴保存。</li>
                <li>（1）后台运行（推荐）：
                  <code>nohup ./frpc -c ./frpc.toml &gt;/var/log/frpc.log 2&gt;&amp;1 &amp;</code>
                  终端关闭也不受影响。
                </li>
                <li>（报错解决）若提示 <code>nohup: command not found</code>，先安装（按系统任选其一）：
                  <ul>
                    <li>Debian/Ubuntu：
                      <code>sudo apt update && sudo apt install coreutils -y</code>
                    </li>
                    <li>CentOS/Rocky：
                      <code>sudo yum install coreutils -y</code>
                    </li>
                  </ul>
                </li>
                <li>（查看状态）查看后台日志并退出查看：
                  <ul>
                    <li>实时查看日志输出：
                      <code>tail -f /var/log/frpc.log</code>
                    </li>
                    <li>退出日志查看（按键）：
                      <code>Ctrl + C</code>
                    </li>
                  </ul>
                </li>
                <li>（2）前台运行测试（终端不能关，不推荐）：
                  <code>cd arm64 && chmod +x ./frpc && ./frpc -c ./frpc.toml</code>
                </li>
                <li style="font-weight: 900;">都不行？还是有问题？找我们解决，QQ群：738146595</li>
              </ol>
            </div>
            <div class="tutorial-card">
              <div class="tutorial-title">
                <h4>mac amd64</h4>
                <span class="chip">Intel Mac</span>
              </div>
              <div v-if="tutorialMedias.mac_amd64 && tutorialMedias.mac_amd64.length" class="media-preview"
                @click="openMedia('mac_amd64', 0)">
                <img :src="tutorialMedias.mac_amd64[0].src" :alt="tutorialMedias.mac_amd64[0].alt" loading="lazy" />
                <button class="play-btn" type="button" aria-label="播放演示">▶</button>
              </div>
              <p class="media-note" v-if="tutorialMedias.mac_amd64 && tutorialMedias.mac_amd64.length">演示系统：macOS</p>
              <ol>
                <li>在<router-link class="app-link" :to="{ name: 'FRPDownloads' }">“应用程序”</router-link>页面获取
                  <code>ZyroFrp_darwin_amd64</code>，访达双击解压。
                </li>
                <li>在解压目录（如 amd64）内找到 <code>frpc.toml</code> ，将<router-link class="app-link" :to="{ name: 'TunnelConfig' }">”配置文件“</router-link>页面内的配置粘贴并保存。</li>
                <li>（1）后台运行（推荐）：访达空白处右键“在文件夹中打开终端”，执行
                  <code>nohup ./frpc -c ./frpc.toml &gt;frpc.log 2&gt;&amp;1 &amp;</code>，终端关闭也不受影响。
                </li>
                <li>（查看状态）实时查看：
                  <code>tail -f frpc.log</code>
                  可查看实时日志信息，退出可按 <code>Ctrl + C</code>；若被系统阻止，到“系统设置 → 隐私与安全”允许运行。
                </li>
                <li>（2）前台运行测试（终端不能关，极不推荐）：
                  <code>chmod +x ./frpc && ./frpc -c ./frpc.toml</code>
                </li>
                <li style="font-weight: 900;">都不行？还是有问题？找我们解决，QQ群：738146595</li>
              </ol>
            </div>
            <div class="tutorial-card">
              <div class="tutorial-title">
                <h4>mac arm64</h4>
                <span class="chip">Apple Silicon</span>
              </div>
              <div v-if="tutorialMedias.mac_arm64 && tutorialMedias.mac_arm64.length" class="media-preview"
                @click="openMedia('mac_arm64', 0)">
                <img :src="tutorialMedias.mac_arm64[0].src" :alt="tutorialMedias.mac_arm64[0].alt" loading="lazy" />
                <button class="play-btn" type="button" aria-label="播放演示">▶</button>
              </div>
              <p class="media-note" v-if="tutorialMedias.mac_arm64 && tutorialMedias.mac_arm64.length">演示系统：macOS</p>
              <ol>
                <li>在<router-link class="app-link" :to="{ name: 'FRPDownloads' }">“应用程序”</router-link>页面获取
                  <code>ZyroFrp_darwin_arm64</code>，访达双击解压。
                </li>
                <li>在解压目录（如 arm64）内找到 <code>frpc.toml</code> ，将<router-link class="app-link" :to="{ name: 'TunnelConfig' }">”配置文件“</router-link>页面内的配置粘贴并保存。</li>
                <li>（1）后台运行（推荐）：访达空白处右键“在文件夹中打开终端”，执行
                  <code>nohup ./frpc -c ./frpc.toml &gt;frpc.log 2&gt;&amp;1 &amp;</code>，终端关闭也不受影响。
                </li>
                <li>（查看状态）实时查看：
                  <code>tail -f frpc.log</code>
                  可查看实时日志信息，退出可按 <code>Ctrl + C</code>；若被系统阻止，到“系统设置 → 隐私与安全”允许运行。
                </li>
                <li>（2）前台运行测试（终端不能关，极不推荐）：
                  <code>chmod +x ./frpc && ./frpc -c ./frpc.toml</code>
                </li>
                <li style="font-weight: 900;">都不行？还是有问题？找我们解决，QQ群：738146595</li>
              </ol>
            </div>
            <div class="tutorial-card">
              <div class="tutorial-title">
                <h4>Windows amd64</h4>
                <span class="chip">amd64</span>
              </div>
              <div v-if="tutorialMedias.win_amd64 && tutorialMedias.win_amd64.length" class="media-preview"
                @click="openMedia('win_amd64', 0)">
                <img :src="tutorialMedias.win_amd64[0].src" :alt="tutorialMedias.win_amd64[0].alt" loading="lazy" />
                <button class="play-btn" type="button" aria-label="播放演示">▶</button>
              </div>
              <p class="media-note" v-if="tutorialMedias.win_amd64 && tutorialMedias.win_amd64.length">演示系统：Windows</p>
              <ol>
                <li>在<router-link class="app-link" :to="{ name: 'FRPDownloads' }">“应用程序”</router-link>页面获取
                  <code>ZyroFrp_win_amd64</code>，资源管理器右键“解压到当前文件夹”。
                </li>
                <li>在解压目录创建 <code>frpc.ini</code>，将面板隧道配置粘贴保存（ANSI/UTF-8 均可）。</li>
                <li>（1）前台图形化验证：双击 <code>frpc.exe</code>；或在地址栏输入 <code>powershell</code> 后运行
                  <code>./frpc.exe -c ./frpc.ini</code> 方便看输出。
                </li>
                <li>（2）后台/开机自启（推荐）：任务计划程序 → 创建基本任务，触发器选“登录时”，操作填
                  <code>frpc.exe -c frpc.ini</code>，勾选“使用最高权限运行”。
                </li>
                <li>（查看状态）查看目录下 <code>frpc.log</code>；若报 token/端口/占用错误，回面板核对配置或改端口。</li>
                <li style="font-weight: 900;">都不行？还是有问题？找我们解决，QQ群：738146595</li>
              </ol>
            </div>
            <div class="tutorial-card">
              <div class="tutorial-title">
                <h4>Windows arm64</h4>
                <span class="chip">arm64</span>
              </div>
              <div v-if="tutorialMedias.win_arm64 && tutorialMedias.win_arm64.length" class="media-preview"
                @click="openMedia('win_arm64', 0)">
                <img :src="tutorialMedias.win_arm64[0].src" :alt="tutorialMedias.win_arm64[0].alt" loading="lazy" />
                <button class="play-btn" type="button" aria-label="播放演示">▶</button>
              </div>
              <p class="media-note" v-if="tutorialMedias.win_arm64 && tutorialMedias.win_arm64.length">演示系统：Windows</p>
              <ol>
                <li>在<router-link class="app-link" :to="{ name: 'FRPDownloads' }">“应用程序”</router-link>页面获取
                  <code>ZyroFrp_win_arm64</code> 并解压。
                </li>
                <li>在解压目录创建 <code>frpc.ini</code>，把面板配置粘贴保存。</li>
                <li>（1）前台验证：双击 <code>frpc.exe</code>，或在 PowerShell 执行
                  <code>./frpc.exe -c ./frpc.ini</code>。
                </li>
                <li>（2）后台/自启：任务计划程序同 amd64；或用 NSSM 安装服务（Path 选 <code>frpc.exe</code>，Arguments 填
                  <code>-c frpc.ini</code>），在 <code>services.msc</code> 查看并设置失败自动重启。
                </li>
                <li>（查看状态）查看目录下 <code>frpc.log</code>，出现 “login/start proxy success” 即正常；异常多为配置或网络问题。</li>
                <li style="font-weight: 900;">都不行？还是有问题？找我们解决，QQ群：738146595</li>
              </ol>
            </div>
            <div class="tutorial-card">
              <div class="tutorial-title">
                <h4>飞牛 OS</h4>
                <span class="chip">amd64</span>
              </div>
              <div v-if="tutorialMedias.feiniu_amd64 && tutorialMedias.feiniu_amd64.length" class="media-preview"
                @click="openMedia('feiniu_amd64', 0)">
                <img :src="tutorialMedias.feiniu_amd64[0].src" :alt="tutorialMedias.feiniu_amd64[0].alt"
                  loading="lazy" />
                <button class="play-btn" type="button" aria-label="播放演示">▶</button>
              </div>
              <p class="media-note" v-if="tutorialMedias.feiniu_amd64 && tutorialMedias.feiniu_amd64.length">演示系统：飞牛 OS
              </p>
              <ol>
                <li>在<router-link class="app-link" :to="{ name: 'FRPDownloads' }">“应用程序”</router-link>页面获取
                  <code>ZyroFrp_linux_amd64</code>/<code>ZyroFrp_linux_amd64</code>，执行
                  <code>tar -zxvf</code> 解压。
                </li>
                <li>在解压目录创建 <code>frpc.toml</code>，把面板隧道配置原样粘贴保存。</li>
                <li>（1）后台运行（推荐）：
                  <code>nohup ./frpc -c ./frpc.toml &gt;/var/log/frpc.log 2&gt;&amp;1 &amp;</code>
                  终端关闭也不受影响。
                </li>
                <li>（报错解决）若提示 <code>nohup: command not found</code>，先安装（按系统任选其一）：
                  <ul>
                    <li>Debian/Ubuntu：
                      <code>sudo apt update && sudo apt install coreutils -y</code>
                    </li>
                    <li>CentOS/Rocky：
                      <code>sudo yum install coreutils -y</code>
                    </li>
                  </ul>
                </li>
                <li>（查看状态）实时查看日志：
                  <code>tail -f /var/log/frpc.log</code>
                  退出按 <code>Ctrl + C</code>；若认证失败比对 token、端口。
                </li>
                <li>（2）前台运行测试（终端不能关，不推荐）：
                  <code>chmod +x ./frpc && ./frpc -c ./frpc.toml</code>
                </li>
                <li style="font-weight: 900;">都不行？还是有问题？找我们解决，QQ群：738146595</li>
              </ol>
            </div>
            <div class="tutorial-card">
              <div class="tutorial-title">
                <h4>飞牛 OS</h4>
                <span class="chip">arm64</span>
              </div>
              <div v-if="tutorialMedias.feiniu_arm64 && tutorialMedias.feiniu_arm64.length" class="media-preview"
                @click="openMedia('feiniu_arm64', 0)">
                <img :src="tutorialMedias.feiniu_arm64[0].src" :alt="tutorialMedias.feiniu_arm64[0].alt"
                  loading="lazy" />
                <button class="play-btn" type="button" aria-label="播放演示">▶</button>
              </div>
              <p class="media-note" v-if="tutorialMedias.feiniu_arm64 && tutorialMedias.feiniu_arm64.length">演示系统：飞牛 OS
              </p>
              <ol>
                <li>在<router-link class="app-link" :to="{ name: 'FRPDownloads' }">“应用程序”</router-link>页面获取
                  <code>ZyroFrp_linux_arm64</code>/<code>ZyroFrp_linux_arm64</code>，执行
                  <code>tar -zxvf</code> 解压。
                </li>
                <li>在解压目录创建 <code>frpc.toml</code>，把面板隧道配置原样粘贴保存。</li>
                <li>（1）后台运行（推荐）：
                  <code>nohup ./frpc -c ./frpc.toml &gt;/var/log/frpc.log 2&gt;&amp;1 &amp;</code>
                  终端关闭也不受影响。
                </li>
                <li>（报错解决）若提示 <code>nohup: command not found</code>，先安装（按系统任选其一）：
                  <ul>
                    <li>Debian/Ubuntu：
                      <code>sudo apt update && sudo apt install coreutils -y</code>
                    </li>
                    <li>CentOS/Rocky：
                      <code>sudo yum install coreutils -y</code>
                    </li>
                  </ul>
                </li>
                <li>（查看状态）实时查看日志：
                  <code>tail -f /var/log/frpc.log</code>
                  退出按 <code>Ctrl + C</code>；若认证失败比对 token、端口。
                </li>
                <li>（2）前台运行测试（终端不能关，不推荐）：
                  <code>chmod +x ./frpc && ./frpc -c ./frpc.toml</code>
                </li>
                <li style="font-weight: 900;">都不行？还是有问题？找我们解决，QQ群：738146595</li>
              </ol>
            </div>
          </div>
        </div>
        <div v-if="lightbox.visible" class="lightbox">
          <div class="lightbox__mask" @click="closeLightbox"></div>
          <div class="lightbox__content">
            <button class="lightbox__close" type="button" @click="closeLightbox">×</button>
            <button class="lightbox__nav prev" type="button" @click="prevMedia">‹</button>
            <div class="lightbox__body">
              <img :src="currentMedia.src" :alt="currentMedia.alt" loading="lazy" />
              <p class="lightbox__caption">{{ currentMedia.alt }}</p>
            </div>
            <button class="lightbox__nav next" type="button" @click="nextMedia">›</button>
          </div>
        </div>
      </section>
      <Footer />
    </main>
  </div>
</template>

<script>
import Header from '@/modules/common/components/Header.vue';
import Sidebar from '@/modules/user/components/Sidebar.vue';
import Footer from '@/modules/common/components/Footer.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import { useLoadingStore } from '@/modules/common/stores/loading'
import { commonMethods } from '@/modules/common/utils/common.js'
import '@/modules/common/assets/styles/common.css'


export default {
  name: 'auth',
  components: {
    Header,
    Sidebar,
    Footer
  },
  data() {
    return {
      mediaConfigs: {
        linux_amd64: { dir: '/user_utorials/linux', label: 'Linux amd64' },
        linux_arm64: { dir: '/user_utorials/linux', label: 'Linux arm64' },
        mac_amd64: { dir: '/user_utorials/mac', label: 'Mac Intel' },
        mac_arm64: { dir: '/user_utorials/mac', label: 'Mac ARM' },
        win_amd64: { dir: '/user_utorials/win', label: 'Windows amd64' },
        win_arm64: { dir: '/user_utorials/win', label: 'Windows arm64' },
        feiniu_amd64: { dir: '/user_utorials/feiniuOS', label: '飞牛 OS amd64' },
        feiniu_arm64: { dir: '/user_utorials/feiniuOS', label: '飞牛 OS arm64' }
      },
      tutorialMedias: {
        linux_amd64: [],
        linux_arm64: [],
        mac_amd64: [],
        mac_arm64: [],
        win_amd64: [],
        win_arm64: [],
        feiniu_amd64: [],
        feiniu_arm64: []
      },
      mediaConfigs: {
        linux_amd64: { dir: '/user_utorials/linux/amd64', label: 'Linux amd64' },
        linux_arm64: { dir: '/user_utorials/linux/arm64', label: 'Linux arm64' },
        mac_amd64: { dir: '/user_utorials/mac/amd64', label: 'Mac Intel' },
        mac_arm64: { dir: '/user_utorials/mac/arm64', label: 'Mac ARM' },
        win_amd64: { dir: '/user_utorials/win/amd64', label: 'Windows amd64' },
        win_arm64: { dir: '/user_utorials/win/arm64', label: 'Windows arm64' },
        feiniu_amd64: { dir: '/user_utorials/feiniuOS/amd64', label: '飞牛 OS amd64' },
        feiniu_arm64: { dir: '/user_utorials/feiniuOS/arm64', label: '飞牛 OS arm64' }
      },
      lightbox: {
        visible: false,
        key: '',
        index: 0
      }
    }
  },
  methods: {
    ...commonMethods,
    async initMedias() {
      for (const [key, cfg] of Object.entries(this.mediaConfigs)) {
        this.tutorialMedias[key] = await this.loadMedias(cfg.dir, cfg.label)
      }
    },
    async loadMedias(dir, label) {
      const maxCount = 20
      const list = []
      for (let i = 1; i <= maxCount; i++) {
        const num = String(i).padStart(3, '0')
        const url = `${dir}/${num}.png`
        const ok = await this.probeImage(url)
        if (ok) {
          list.push({ src: url, alt: `${label} 演示图 ${i}` })
        }
      }
      return list
    },
    probeImage(url) {
      return new Promise(resolve => {
        const img = new Image()
        img.onload = () => resolve(true)
        img.onerror = () => resolve(false)
        img.src = url
      })
    },
    openMedia(key, idx) {
      if (!this.tutorialMedias[key] || !this.tutorialMedias[key].length) return
      this.lightbox.visible = true
      this.lightbox.key = key
      this.lightbox.index = idx
      document.body.style.overflow = 'hidden'
    },
    closeLightbox() {
      this.lightbox.visible = false
      document.body.style.overflow = ''
    },
    prevMedia() {
      const list = this.tutorialMedias[this.lightbox.key] || []
      if (!list.length) return
      this.lightbox.index = (this.lightbox.index - 1 + list.length) % list.length
    },
    nextMedia() {
      const list = this.tutorialMedias[this.lightbox.key] || []
      if (!list.length) return
      this.lightbox.index = (this.lightbox.index + 1) % list.length
    }
  },
  computed: {
    currentMedia() {
      const list = this.tutorialMedias[this.lightbox.key] || []
      return list[this.lightbox.index] || { src: '', alt: '' }
    }
  },
  mounted() {
    this.initMedias();
    this.checkTokenValidity();
  }
}
</script>

<style scoped>
.fb1,
.fb2 {
  width: calc(100vw - 18rem - 2rem);
  padding: 3rem;
  margin: 1rem;
}

.fb2 {
  padding: 1rem;
}

.h3_1 {
  font-size: 2rem;
  font-weight: 500;
  margin-bottom: 10px;
  margin-left: 0 !important;
}

.h3_2 {
  font-size: 2rem;
  font-weight: 500;
  margin-top: 1.5rem;
  margin-left: 2.2rem;
}

pre {
  background-color: #f4f4f4;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  font-family: Consolas, monospace;
  margin: 20px 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  width: 10vw;
  padding-left: 20px;
}

code {
  color: #e83e8c;
  font-size: 0.9rem;
}

strong {
  font-size: 1.5em;
  font-weight: 350;
  cursor: pointer;
}

section.features {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.h3 {
  display: flex;
  align-items: end;
  margin-bottom: 40px;
}

.h3 p {
  margin-bottom: 16px;
}

.tooltip {
  position: relative;
  cursor: pointer;
  display: inline-block;
}

.tooltip .tooltiptext {
  visibility: hidden;
  min-width: 500px;
  background-color: #ebebeb;
  color: #1c1c1c;
  text-align: left;
  padding: 12px;
  border-radius: 6px;
  font-size: 14px;
  line-height: 1.5;
  position: absolute;
  z-index: 99;
  bottom: 120%;
  left: 0;
  opacity: 0;
  transition: opacity 0.3s;
}

.tooltip:hover .tooltiptext {
  visibility: visible;
  opacity: 1;
}

strong.dowm {
  display: flex;
}

strong {
  font-size: 1rem;
}

.icon {
  color: #63e2b7;
  width: 3rem;
  height: 3rem;
  margin-right: 1rem;
}

.icon2 {
  color: #63e2b7;
  width: 2.74rem;
  height: 2.74rem;
  margin-right: 1rem;
  margin-left: 0.2rem;
}

.line {
  display: flex;
  align-items: center;
}

.arrow {
  font-size: 2rem;
  margin-left: 1.3rem;
  color: #63e2b7;
}

.tutorial-grid {
  display: grid;
  gap: 20px;
  grid-template-columns: 1fr;
}

.tutorial-card {
  border: 1px solid #e5e5e5;
  border-radius: 12px;
  padding: 2.5rem;
  margin: 0 1rem;
  background: #fff;
  text-align: left;
  width: calc(100% - 2rem);
  display: flex !important;
  flex-direction: column !important;
  align-items: flex-start !important;
  justify-content: flex-start !important;
  box-shadow: none;
  transition: none;
  cursor: default;
}

.tutorial-card::before {
  display: none;
  content: none;
}

.tutorial-title {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 10px;
  margin-bottom: 12px;
}

.tutorial-card h4 {
  margin: 0;
  font-size: 1.3rem;
  font-weight: 700;
}

.chip {
  background: #e7f5ef;
  color: #1eac71;
  border-radius: 12px;
  padding: 4px 10px;
  font-size: 0.85rem;
  white-space: nowrap;
}

.tutorial-card ol {
  padding-left: 18px;
  margin: 0;
  display: grid;
  gap: 10px;
  color: #444;
  line-height: 1.6;
}

.tutorial-card code {
  background: #f5f5f5;
  padding: 2px 6px;
  border-radius: 6px;
  color: #c7254e;
  font-size: 0.9rem;
}

.app-link {
  color: #1eac71;
  text-decoration: none;
}

.media-preview {
  position: relative;
  width: 18rem;
  max-height: 240px;
  overflow: hidden;
  border-radius: 12px;
  margin: 2rem 0 2rem 0;
}

.media-preview img {
  width: 100%;
  height: auto;
  object-fit: contain;
  display: block;
  background: #f7f7f7;
}

.play-btn {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: none;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  font-size: 20px;
  cursor: pointer;
}

.media-note {
  margin: -0.5rem 0 1.5rem 0;
  color: #666;
  font-size: 0.95rem;
}

.lightbox {
  position: fixed;
  inset: 0;
  z-index: 9999;
}

.lightbox__mask {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.72);
}

.lightbox__content {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  pointer-events: none;
}

.lightbox__body {
  background: #111;
  padding: 12px;
  border-radius: 12px;
  max-width: 90vw;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  gap: 8px;
  pointer-events: auto;
}

.lightbox__body img {
  width: 80vw;
  height: auto;
  max-height: 80vh;
  object-fit: contain;
  background: #000;
}

.lightbox__caption {
  margin: 0;
  color: #ddd;
  font-size: 0.95rem;
}

.lightbox__close {
  position: absolute;
  top: 24px;
  right: 28px;
  border: none;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  font-size: 22px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  cursor: pointer;
  pointer-events: auto;
}

.lightbox__nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  border: none;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  font-size: 28px;
  width: 44px;
  height: 72px;
  cursor: pointer;
  pointer-events: auto;
}

.lightbox__nav.prev {
  left: 18px;
}

.lightbox__nav.next {
  right: 18px;
}

.tutorial-card:hover,
.tutorial-card:active,
.tutorial-card:focus {
  transform: none !important;
  box-shadow: none !important;
  border-color: #e5e5e5 !important;
}

@media (prefers-color-scheme: dark) {
  p {
    color: #a3a3a3;
  }

  .fb1 {
    background-color: #18181c;
  }

  .tutorial-card {
    background: #18181c;
    border-color: #2a2a2a;
  }

  .tutorial-card code {
    background: #222;
    color: #f198ab;
  }
}

@media (prefers-color-scheme: dark) {
  p {
    color: #a3a3a3;
  }

  .fb1 {
    background-color: #18181c;
  }
}
</style>
