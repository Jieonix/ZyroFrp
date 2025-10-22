// utils/validate.js
export function validateEmail(Email) {
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  if (!emailRegex.test(Email.value)) {
    alert("请输入有效的邮箱地址")
    return false
  }
  return true
}

export function validatePassword(password) {
  const weakPasswords = /^(1{6,}|2{6,}|3{6,}|4{6,}|a{6,}|b{6,}|c{6,}|d{6,}|!{6,}|@{6,}|#{6,}|\${6,})$/;

  if (weakPasswords.test(password.value)) {
    alert("密码太简单，请换一个")
    return false
  }

  return true
}

export function validateCode(Code) {

  if (!Code.value || Code.value.trim() === '') {
    alert("验证码不能为空");
    return false;
  }

  const digitRegex = /^\d{4}$/;
  if (!digitRegex.test(Code.value)) {
    alert("验证码必须由4位数字组成");
    return false;
  }

  return true;
}

