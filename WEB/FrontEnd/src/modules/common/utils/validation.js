// 统一验证工具 - 合并 validate.js 和 validation.js
export const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
export const weakPasswords = /^(1{6,}|2{6,}|3{6,}|4{6,}|a{6,}|b{6,}|c{6,}|d{6,}|!{6,}|@{6,}|#{6,}|\${6,})$/;
export const codeRegex = /^\d{4}$/;

/**
 * 验证邮箱格式
 * @param {Object} Email - Vue ref 对象，包含 .value 属性
 * @param {Function} showError - 错误显示函数，可选
 * @returns {boolean} 验证是否通过
 */
export function validateEmail(Email, showError = null) {
  if (!emailRegex.test(Email.value)) {
    const errorMsg = "请输入有效的邮箱地址";
    if (showError) {
      showError(errorMsg);
    } else {
      alert(errorMsg);
    }
    return false;
  }
  return true;
}

/**
 * 验证密码强度
 * @param {Object} password - Vue ref 对象，包含 .value 属性
 * @param {Function} showError - 错误显示函数，可选
 * @returns {boolean} 验证是否通过
 */
export function validatePassword(password, showError = null) {
  if (weakPasswords.test(password.value)) {
    const errorMsg = "密码太简单，请换一个";
    if (showError) {
      showError(errorMsg);
    } else {
      alert(errorMsg);
    }
    return false;
  }
  return true;
}

/**
 * 验证验证码格式
 * @param {Object} Code - Vue ref 对象，包含 .value 属性
 * @param {Function} showError - 错误显示函数，可选
 * @returns {boolean} 验证是否通过
 */
export function validateCode(Code, showError = null) {
  if (!Code.value || Code.value.trim() === '') {
    const errorMsg = "验证码不能为空";
    if (showError) {
      showError(errorMsg);
    } else {
      alert(errorMsg);
    }
    return false;
  }

  if (!codeRegex.test(Code.value)) {
    const errorMsg = "验证码必须由4位数字组成";
    if (showError) {
      showError(errorMsg);
    } else {
      alert(errorMsg);
    }
    return false;
  }

  return true;
}

/**
 * 统一验证函数 - 支持多种验证类型
 * @param {string} type - 验证类型：'email', 'password', 'code'
 * @param {Object} value - Vue ref 对象，包含 .value 属性
 * @param {Function} showError - 错误显示函数，可选
 * @returns {boolean} 验证是否通过
 */
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

/**
 * 批量验证函数
 * @param {Array} validations - 验证配置数组，每个元素包含 { type, value, errorField }
 * @param {Function} showError - 统一错误显示函数
 * @returns {boolean} 所有验证是否都通过
 */
export function validateMultiple(validations, showError = null) {
  for (const validation of validations) {
    if (!validate(validation.type, validation.value, showError)) {
      return false;
    }
  }
  return true;
}