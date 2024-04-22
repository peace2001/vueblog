<template>
  <div style="height: 100vh; display: flex; align-items: center; justify-content: center; background-color: #669fef">
    <div style="display: flex; background-color: white; width: 50%; border-radius: 5px; overflow: hidden">
      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <el-form :model="user" style="width: 80%" :rules="rules" ref="registerRef">
          <div style="font-size: 20px; font-weight: bold; text-align: center; margin-bottom: 20px">欢迎注册</div>
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号" v-model="user.username"></el-input>
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入昵称" v-model="user.nickname"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请输入密码" v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPass">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请确认密码" v-model="user.confirmPass"></el-input>
          </el-form-item>
          <el-form-item prop="userEmail">
            <el-input prefix-icon="el-icon-mail" size="medium"  placeholder="请输入邮箱号" v-model="user.email"></el-input>
          </el-form-item>



          <el-form-item>
            <el-button type="success" style="width: 100%" @click="register">注 册</el-button>
          </el-form-item>
          <div style="display: flex">
            <div style="flex: 1">已经有账号了？请 <span style="color: #6e77f2; cursor: pointer" @click="$router.push('/UserLogin')">登录</span></div>
          </div>
        </el-form>
      </div>
    </div>

  </div>
</template>

<script>

export default {
  name: "Register",
  data() {
    // 验证码校验
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'))
      } else if (confirmPass !== this.user.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      user: {
        username: '',
        password: '',
        confirmPass: '',
        nickname:'',
        email:'',
      },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        nickname: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        confirmPass: [
          { validator: validatePassword, trigger: 'blur' }
        ],

      },
      userAge:18,
    }
  },
  created() {

  },
  methods: {
    register() {
      this.$refs['registerRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$axios.post('/register', this.user).then(res => {
            if (res.data.code === 200) {
              this.$router.push('/userLogin')
              this.$message.success('注册成功')
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },
    handleChange(value) {
      console.log(value);
    }
  }
}
</script>

<style scoped>

</style>