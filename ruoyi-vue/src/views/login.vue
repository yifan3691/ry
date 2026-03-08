<template>
  <div class="login">
    <div class="bg-surface" aria-hidden="true">
      <span class="bg-grid" />
      <span class="bg-glow" />
    </div>

    <div class="login-shell">
      <section class="brand-panel">
        <p class="brand-chip">RuoYi Enterprise Console</p>
        <h1 class="brand-title">统一管理平台登录</h1>
        <p class="brand-desc">
          聚合权限、流程、审计与监控能力，
          为团队提供稳定、可追踪、可扩展的业务协同入口。
        </p>

        <div class="brand-list">
          <div class="brand-item">
            <span class="item-title">权限治理</span>
            <span class="item-desc">基于角色与菜单的细粒度控制</span>
          </div>
          <div class="brand-item">
            <span class="item-title">流程协作</span>
            <span class="item-desc">统一任务流转，降低跨部门沟通成本</span>
          </div>
          <div class="brand-item">
            <span class="item-title">运维可观测</span>
            <span class="item-desc">关键链路实时监测与异常告警联动</span>
          </div>
        </div>
      </section>

      <section class="form-panel">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
          <div class="panel-head">
            <p class="panel-tag">Secure Access</p>
            <h3 class="title">欢迎登录</h3>
            <p class="sub-title">请输入账号和密码继续访问</p>
          </div>

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
            <span class="security-tip">TLS</span>
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
      </section>
    </div>

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
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@500;600;700;800&family=Noto+Sans+SC:wght@400;500;700&display=swap');

.login {
  --bg-main: #f3f5f8;
  --panel-bg: #ffffff;
  --panel-subtle: #f8fafc;
  --line: #dbe3ec;
  --line-strong: #c7d3e2;
  --text-main: #172333;
  --text-sub: #5f6f84;
  --accent: #234a84;
  --accent-hover: #1a3c70;

  position: relative;
  min-height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 36px 24px 74px;
  overflow: hidden;
  background:
    radial-gradient(circle at 10% 12%, rgba(35, 74, 132, 0.08), rgba(35, 74, 132, 0) 34%),
    radial-gradient(circle at 82% 18%, rgba(15, 122, 229, 0.06), rgba(15, 122, 229, 0) 32%),
    var(--bg-main);
  font-family: 'Noto Sans SC', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.bg-surface {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(196, 209, 225, 0.2) 1px, transparent 1px),
    linear-gradient(90deg, rgba(196, 209, 225, 0.2) 1px, transparent 1px);
  background-size: 36px 36px;
  opacity: 0.45;
}

.bg-glow {
  position: absolute;
  right: -120px;
  top: -90px;
  width: 340px;
  height: 340px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(35, 74, 132, 0.14), rgba(35, 74, 132, 0));
}

.login-shell {
  position: relative;
  z-index: 1;
  width: min(1080px, 100%);
  min-height: 600px;
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  border: 1px solid var(--line);
  border-radius: 20px;
  overflow: hidden;
  background: var(--panel-bg);
  box-shadow:
    0 18px 40px rgba(17, 38, 67, 0.08),
    0 2px 10px rgba(17, 38, 67, 0.04);
  animation: rise 0.35s ease;
}

.brand-panel {
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 52px 48px 40px;
  background:
    linear-gradient(160deg, #ffffff 20%, #f7fafe 100%);
  border-right: 1px solid var(--line);
}

.brand-chip {
  display: inline-flex;
  width: fit-content;
  margin: 0;
  padding: 6px 12px;
  border-radius: 999px;
  border: 1px solid var(--line-strong);
  background: #fff;
  color: #395271;
  font-size: 12px;
  letter-spacing: 0.8px;
}

.brand-title {
  margin: 0;
  color: var(--text-main);
  font-family: 'Manrope', 'Noto Sans SC', sans-serif;
  font-size: 44px;
  line-height: 1.16;
  letter-spacing: -0.4px;
}

.brand-desc {
  margin: 0;
  max-width: 500px;
  color: var(--text-sub);
  font-size: 15px;
  line-height: 1.9;
}

.brand-list {
  display: grid;
  gap: 12px;
  margin-top: 6px;
}

.brand-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 14px 14px 13px;
  border-radius: 10px;
  border: 1px solid var(--line);
  background: #fff;
}

.item-title {
  color: var(--text-main);
  font-size: 14px;
  font-weight: 600;
}

.item-desc {
  color: #6b7d93;
  font-size: 13px;
}

.form-panel {
  display: flex;
  align-items: center;
  padding: 32px;
  background: var(--panel-subtle);
}

.login-form {
  width: 100%;
  padding: 30px 24px 24px;
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.panel-head {
  margin-bottom: 24px;
}

.panel-tag {
  margin: 0;
  color: #5a6f8b;
  font-size: 12px;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.title {
  margin: 10px 0 8px;
  color: var(--text-main);
  font-family: 'Manrope', 'Noto Sans SC', sans-serif;
  font-size: 32px;
  line-height: 1.1;
}

.sub-title {
  margin: 0;
  color: #708198;
  font-size: 13px;
}

.login-form .el-form-item {
  margin-bottom: 18px;
}

.login-form .el-input__inner {
  height: 46px;
  padding-left: 42px;
  border-radius: 10px;
  border: 1px solid var(--line-strong);
  background: #fff;
  color: var(--text-main);
}

.login-form .el-input__inner::placeholder {
  color: #99a7b7;
}

.login-form .el-input__inner:hover,
.login-form .el-input__inner:focus {
  border-color: #95aac4;
  box-shadow: 0 0 0 2px rgba(35, 74, 132, 0.07);
}

.input-icon {
  width: 16px;
  height: 46px;
  margin-left: 1px;
  color: #8294ab;
}

.captcha-wrap {
  display: flex;
  gap: 10px;
}

.captcha-wrap .el-input {
  flex: 1;
}

.login-code {
  width: 122px;
  height: 46px;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid var(--line-strong);
  background: #fff;
}

.login-code-img {
  display: block;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.login-options {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px 12px;
  margin-bottom: 20px;
}

.login-options .el-checkbox {
  margin-right: auto;
}

.login-options .el-checkbox__label {
  color: #5f7086;
}

.login-options .el-checkbox__inner {
  border-color: #b9c7d8;
}

.login-options .el-checkbox__input.is-checked .el-checkbox__inner,
.login-options .el-checkbox__input.is-indeterminate .el-checkbox__inner {
  border-color: var(--accent);
  background: var(--accent);
}

.security-tip {
  padding: 3px 10px;
  border-radius: 999px;
  border: 1px solid #c7d4e4;
  background: #f4f8fc;
  color: #557395;
  font-size: 12px;
}

.register-link {
  font-size: 13px;
  color: #385b88;
}

.register-link:hover {
  color: #2a4b75;
}

.login-action {
  margin-bottom: 0;
}

.login-btn {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 10px;
  background: var(--accent);
  color: #fff;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 1.6px;
  box-shadow: 0 8px 18px rgba(35, 74, 132, 0.2);
  transition: background-color 0.2s ease, transform 0.2s ease;
}

.login-btn:hover,
.login-btn:focus {
  background: var(--accent-hover);
  transform: translateY(-1px);
}

.el-login-footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 40px;
  line-height: 40px;
  text-align: center;
  color: #7a8da5;
  font-size: 12px;
  letter-spacing: 0.6px;
}

@keyframes rise {
  from {
    transform: translateY(8px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media (max-width: 1060px) {
  .login-shell {
    width: min(960px, 100%);
  }

  .brand-panel {
    padding: 40px 32px 30px;
  }

  .brand-title {
    font-size: 36px;
  }

  .form-panel {
    padding: 24px;
  }
}

@media (max-width: 900px) {
  .login {
    padding-top: 18px;
  }

  .login-shell {
    grid-template-columns: 1fr;
    max-width: 720px;
    min-height: auto;
  }

  .brand-panel {
    border-right: none;
    border-bottom: 1px solid var(--line);
  }
}

@media (max-width: 680px) {
  .login {
    padding: 12px 10px 66px;
  }

  .login-shell {
    border-radius: 16px;
  }

  .brand-panel {
    display: none;
  }

  .form-panel {
    padding: 16px;
  }

  .login-form {
    padding: 24px 16px 18px;
    border-radius: 12px;
  }

  .title {
    font-size: 28px;
  }

  .login-code {
    width: 108px;
  }

  .el-login-footer {
    font-size: 11px;
    letter-spacing: 0.3px;
  }
}

@media (max-width: 420px) {
  .title {
    font-size: 26px;
  }

  .login-form .el-input__inner,
  .login-btn,
  .login-code {
    height: 44px;
  }

  .input-icon {
    height: 44px;
  }

  .login-code {
    width: 102px;
  }
}
</style>
