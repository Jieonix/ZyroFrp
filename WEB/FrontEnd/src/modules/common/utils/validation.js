import messageManager from './message.js'

export const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
export const weakPasswords = /^(1{6,}|2{6,}|3{6,}|4{6,}|a{6,}|b{6,}|c{6,}|d{6,}|!{6,}|@{6,}|#{6,}|\${6,})$/;
export const codeRegex = /^\d{4}$/;

export function validateEmail(Email, showError = null) {
  if (!emailRegex.test(Email.value)) {
    const errorMsg = "请输入有效的邮箱地址";
    if (showError) {
      showError(errorMsg);
    } else {
      messageManager.warning(errorMsg);
    }
    return false;
  }
  return true;
}

export function validatePassword(password, showError = null) {
  if (weakPasswords.test(password.value)) {
    const errorMsg = "密码太简单，请换一个";
    if (showError) {
      showError(errorMsg);
    } else {
      messageManager.warning(errorMsg);
    }
    return false;
  }
  return true;
}

export function validateCode(Code, showError = null) {
  if (!Code.value || Code.value.trim() === '') {
    const errorMsg = "验证码不能为空";
    if (showError) {
      showError(errorMsg);
    } else {
      messageManager.warning(errorMsg);
    }
    return false;
  }

  if (!codeRegex.test(Code.value)) {
    const errorMsg = "验证码必须由4位数字组成";
    if (showError) {
      showError(errorMsg);
    } else {
      messageManager.warning(errorMsg);
    }
    return false;
  }

  return true;
}

export function validate(type, value, showError = null) {
  switch (type) {
    case 'email':
      return validateEmail(value, showError);
    case 'password':
      return validatePassword(value, showError);
    case 'code':
      return validateCode(value, showError);
    default:
      console.warn(`未知的验证类型: ${type}`);
      return true;
  }
}

export function validateMultiple(validations, showError = null) {
  for (const validation of validations) {
    if (!validate(validation.type, validation.value, showError)) {
      return false;
    }
  }
  return true;
}