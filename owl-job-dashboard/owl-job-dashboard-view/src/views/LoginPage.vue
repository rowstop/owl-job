<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { LoginParam } from '@/api/security/model'
import { login } from '@/api/security'
import { throttle } from '@/util/common'
import Logo from '@/assets/img/logo.svg'
import { useSecurityStore } from '@/stores/security'

const security = useSecurityStore()
const loginForm = ref<FormInstance>()
const rememberMe = ref<boolean>(false)
const loginParam = ref<LoginParam>({
  username: '',
  password: ''
})

const loginRules = reactive<FormRules<LoginParam>>({
  username: [{ required: true, trigger: 'blur', message: '输入用户名' }],
  password: [{ required: true, trigger: 'blur', message: '输入密码' }]
})

const doLogin = throttle(() => {
  if (!loginForm.value) {
    return
  }
  loginForm.value?.validate((isValid) => {
    if (!isValid) {
      return
    }
    login(loginParam.value).then((result) => {
      console.log(result)
      const data = result.data
      security.token = {
        name: data.tokenName,
        value: data.tokenValue
      }
    })
  })
}, 1000)
</script>
<template>
  <div class="box">
    <div class="inner">
      <div class="login-form">
        <el-image :src="Logo" class="logo" />
        <el-form ref="loginForm" :model="loginParam" :rules="loginRules" label-width="auto">
          <el-form-item prop="username">
            <el-input v-model="loginParam.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginParam.password" placeholder="请输入密码" />
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="rememberMe" label="记住登录状态" />
          </el-form-item>
        </el-form>
        <el-button class="login-button" type="primary" @click="doLogin">登录</el-button>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: 100vh;

  .inner {
    width: 500px;
    padding: 0 16px 24px 16px;
    border-radius: 12px;
    border: 1px solid $owl-color-primary;

    .login-form {
      width: 100%;
      text-align: center;

      .logo {
        width: 30%;
        height: 30%;
      }

      .login-button {
        width: 100%;
      }
    }
  }
}
</style>
