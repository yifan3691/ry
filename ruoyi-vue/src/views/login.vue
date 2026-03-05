<template>
  <div class="login">
    <div class="login-bg" aria-hidden="true">
      <span class="orb orb-a" />
      <span class="orb orb-b" />
      <span class="orb orb-c" />
      <span class="scan-line" />
    </div>
    <div class="login-shell">
      <section class="login-brand">
        <p class="brand-kicker">RuoYi Control Matrix</p>
        <h1 class="brand-title">登录你的指挥中心</h1>
        <p class="brand-desc">
          一体化后台入口，统一权限、安全审计与业务监控，专注关键数据与关键动作。
        </p>
        <ul class="brand-feature">
          <li>多维权限控制</li>
          <li>实时业务看板</li>
          <li>安全链路审计</li>
        </ul>
      </section>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <h3 class="title">欢迎回来</h3>
        <p class="sub-title">使用账号继续访问系统</p>
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            placeholder="账号"
          >
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter.native="handleLogin"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item v-if="captchaEnabled" prop="code">
          <div class="captcha-wrap">
            <el-input
              v-model="loginForm.code"
              auto-complete="off"
              placeholder="验证码"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
            </el-input>
            <div class="login-code">
              <img :src="codeUrl" class="login-code-img" @click="getCode">
            </div>
          </div>
        </el-form-item>
        <div class="login-options">
          <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
          <router-link v-if="register" class="link-type register-link" :to="'/register'">立即注册</router-link>
        </div>
        <el-form-item class="login-action">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            class="login-btn"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-{{ currentYear }} ruoyi.vip All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: 'Login',
  data() {
    return {
      codeUrl: '',
      loginForm: {
        username: 'admin',
        password: 'admin123',
        rememberMe: false,
        code: '',
        uuid: ''
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入您的账号' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入您的密码' }
        ],
        code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined,
      currentYear: new Date().getFullYear()
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get('username')
      const password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : rememberMe === 'true',
        code: '',
        uuid: ''
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set('username', this.loginForm.username, { expires: 30 })
            Cookies.set('password', encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch('Login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' }).catch(() => {})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  --bg-deep: #04091f;
  --bg-mid: #0f1e47;
  --accent: #35dbff;
  --accent-soft: #7effbf;
  --text-main: #eaf3ff;
  --text-soft: rgba(234, 243, 255, 0.7);
  --line-color: rgba(133, 173, 255, 0.25);

  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100%;
  padding: 40px 24px 72px;
  overflow: hidden;
  background:
    radial-gradient(circle at 12% 8%, rgba(53, 219, 255, 0.3), rgba(53, 219, 255, 0) 32%),
    radial-gradient(circle at 82% 22%, rgba(126, 255, 191, 0.2), rgba(126, 255, 191, 0) 28%),
    linear-gradient(120deg, var(--bg-deep), #091437 45%, var(--bg-mid));
  font-family: 'Avenir Next', 'Futura', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.login::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(transparent 96%, rgba(130, 166, 255, 0.16) 100%),
    linear-gradient(90deg, transparent 96%, rgba(130, 166, 255, 0.1) 100%);
  background-size: 42px 42px;
  opacity: 0.32;
  pointer-events: none;
}

.login-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(4px);
}

.orb-a {
  width: 240px;
  height: 240px;
  top: 12%;
  left: 10%;
  background: radial-gradient(circle, rgba(53, 219, 255, 0.75) 0%, rgba(53, 219, 255, 0) 72%);
  animation: float-up 10s ease-in-out infinite;
}

.orb-b {
  width: 320px;
  height: 320px;
  right: 8%;
  bottom: 14%;
  background: radial-gradient(circle, rgba(126, 255, 191, 0.56) 0%, rgba(126, 255, 191, 0) 74%);
  animation: float-up 12s ease-in-out infinite reverse;
}

.orb-c {
  width: 180px;
  height: 180px;
  right: 36%;
  top: 12%;
  background: radial-gradient(circle, rgba(124, 165, 255, 0.6) 0%, rgba(124, 165, 255, 0) 70%);
  animation: float-side 11s ease-in-out infinite;
}

.scan-line {
  position: absolute;
  left: -20%;
  width: 140%;
  height: 1px;
  top: 35%;
  background: linear-gradient(90deg, rgba(53, 219, 255, 0), rgba(53, 219, 255, 0.7), rgba(53, 219, 255, 0));
  box-shadow: 0 0 20px rgba(53, 219, 255, 0.6);
  animation: sweep 7s linear infinite;
}

.login-shell {
  z-index: 2;
  display: flex;
  width: min(980px, 100%);
  min-height: 570px;
  border: 1px solid var(--line-color);
  border-radius: 28px;
  overflow: hidden;
  background: linear-gradient(135deg, rgba(4, 14, 40, 0.55), rgba(5, 12, 31, 0.35));
  backdrop-filter: blur(14px);
  box-shadow:
    0 35px 80px rgba(2, 6, 20, 0.65),
    inset 0 0 0 1px rgba(255, 255, 255, 0.04);
}

.login-brand {
  width: 45%;
  padding: 58px 48px;
  color: var(--text-main);
  background: linear-gradient(150deg, rgba(7, 21, 58, 0.84), rgba(10, 28, 73, 0.58));
  border-right: 1px solid rgba(136, 176, 255, 0.18);
}

.brand-kicker {
  display: inline-flex;
  align-items: center;
  height: 28px;
  padding: 0 14px;
  border-radius: 14px;
  margin-bottom: 20px;
  font-size: 12px;
  letter-spacing: 1.6px;
  text-transform: uppercase;
  color: rgba(214, 238, 255, 0.92);
  background: linear-gradient(90deg, rgba(53, 219, 255, 0.2), rgba(126, 255, 191, 0.2));
  border: 1px solid rgba(103, 226, 255, 0.38);
}

.brand-title {
  margin: 0;
  font-size: 38px;
  line-height: 1.25;
  letter-spacing: 1px;
}

.brand-desc {
  margin: 22px 0 30px;
  font-size: 14px;
  line-height: 1.8;
  color: var(--text-soft);
}

.brand-feature {
  margin: 0;
  padding: 0;
  list-style: none;

  li {
    position: relative;
    padding-left: 22px;
    margin-bottom: 14px;
    font-size: 14px;
    color: rgba(234, 243, 255, 0.88);
  }

  li::before {
    content: '';
    position: absolute;
    left: 0;
    top: 9px;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background: linear-gradient(140deg, var(--accent), var(--accent-soft));
    box-shadow: 0 0 12px rgba(53, 219, 255, 0.8);
  }
}

.login-form {
  width: 55%;
  padding: 58px 52px 34px;
  background: linear-gradient(170deg, rgba(8, 20, 54, 0.78), rgba(7, 16, 42, 0.6));
}

.title {
  margin: 0 0 8px;
  font-size: 32px;
  line-height: 1.2;
  letter-spacing: 1px;
  color: #ffffff;
}

.sub-title {
  margin: 0 0 32px;
  color: rgba(221, 237, 255, 0.7);
  font-size: 14px;
}

.login-form .el-form-item {
  margin-bottom: 22px;
}

.login-form .el-input__inner {
  height: 46px;
  padding-left: 42px;
  border-radius: 12px;
  border: 1px solid rgba(146, 189, 255, 0.26);
  background: rgba(6, 14, 37, 0.66);
  color: #eaf3ff;
}

.login-form .el-input__inner::placeholder {
  color: rgba(223, 236, 255, 0.4);
}

.login-form .el-input__inner:hover,
.login-form .el-input__inner:focus {
  border-color: rgba(114, 231, 255, 0.68);
}

.input-icon {
  height: 46px;
  width: 16px;
  margin-left: 2px;
  color: rgba(221, 235, 255, 0.84);
}

.captcha-wrap {
  display: flex;
  gap: 12px;
}

.captcha-wrap .el-input {
  flex: 1;
}

.login-code {
  width: 132px;
  height: 46px;
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(146, 189, 255, 0.26);
  background: rgba(6, 14, 37, 0.55);
}

.login-code-img {
  display: block;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  color: rgba(225, 238, 255, 0.8);
}

.login-options .el-checkbox {
  color: rgba(225, 238, 255, 0.8);
}

.login-options .el-checkbox__label {
  color: rgba(225, 238, 255, 0.88);
}

.register-link {
  font-size: 13px;
  color: #75e4ff;
}

.register-link:hover {
  color: #94ffd1;
}

.login-action {
  margin-bottom: 0;
}

.login-btn {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 12px;
  color: #08142f;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 2px;
  background: linear-gradient(120deg, var(--accent), var(--accent-soft));
  box-shadow: 0 12px 28px rgba(29, 195, 255, 0.34);
  transition: transform 0.2s ease, filter 0.2s ease, box-shadow 0.2s ease;
}

.login-btn:hover,
.login-btn:focus {
  transform: translateY(-1px);
  filter: brightness(1.03);
  box-shadow: 0 16px 32px rgba(29, 195, 255, 0.42);
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: rgba(228, 240, 255, 0.8);
  font-size: 12px;
  letter-spacing: 1.4px;
}

@keyframes float-up {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-22px);
  }
}

@keyframes float-side {
  0%,
  100% {
    transform: translateX(0);
  }
  50% {
    transform: translateX(24px);
  }
}

@keyframes sweep {
  0% {
    transform: translateY(-170px);
    opacity: 0;
  }
  15% {
    opacity: 0.85;
  }
  60% {
    opacity: 0.3;
  }
  100% {
    transform: translateY(560px);
    opacity: 0;
  }
}

@media (max-width: 980px) {
  .login {
    padding-top: 24px;
  }

  .login-shell {
    flex-direction: column;
    max-width: 640px;
    min-height: auto;
  }

  .login-brand,
  .login-form {
    width: 100%;
  }

  .login-brand {
    padding: 36px 30px;
    border-right: none;
    border-bottom: 1px solid rgba(136, 176, 255, 0.18);
  }

  .brand-title {
    font-size: 30px;
  }

  .login-form {
    padding: 36px 30px 26px;
  }
}

@media (max-width: 560px) {
  .login {
    padding: 16px 12px 68px;
  }

  .login-shell {
    border-radius: 20px;
  }

  .login-brand {
    display: none;
  }

  .login-form {
    width: 100%;
    padding: 30px 18px 20px;
  }

  .title {
    font-size: 28px;
  }

  .sub-title {
    margin-bottom: 24px;
  }

  .login-form .el-form-item {
    margin-bottom: 18px;
  }

  .captcha-wrap {
    gap: 8px;
  }

  .login-code {
    width: 110px;
  }

  .el-login-footer {
    letter-spacing: 0.5px;
    font-size: 11px;
  }
}
</style>
