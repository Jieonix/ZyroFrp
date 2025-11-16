import { test, expect } from '@playwright/test';

async function waitForToken(page, tk) {
  for (let i = 0; i < 20; i++) {
    const t = await page.evaluate((key) => localStorage.getItem(key), tk);
    if (t) return t;
    await page.waitForTimeout(100);
  }
  return null;
}

Object.defineProperty(Object.prototype, 'clickAndWait', {
  value: async function (delay = 200) {
    await this.click();
    await new Promise(resolve => setTimeout(resolve, delay));
  },
  writable: true,
  configurable: true
});

test.use({
  actionTimeout: 120000,
  navigationTimeout: 120000,
});

test('test', async ({ page }) => {
  // 设置测试超时为5分钟
  test.setTimeout(300000);

  console.log('✅ 登录测试开始！');

  // 用户登录界面
  // 登录成功
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('testUser@test.com');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();

  let token = await waitForToken(page, 'Token');
  expect(token).not.toBeNull();
  await page.evaluate(() => localStorage.clear());


  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('testAdmin@test.com');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();

  token = await waitForToken(page, 'Token');
  expect(token).not.toBeNull();
  await page.evaluate(() => localStorage.clear());

  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('testSuperAdmin@test.com');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();

  token = await waitForToken(page, 'Token');
  expect(token).not.toBeNull();
  await page.evaluate(() => localStorage.clear());

  // 邮箱格式错误
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('test@');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 用户不存在
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('test@test.com');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 密码错误
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('testUser@test.com');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('111111');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 未输入邮箱
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 未输入密码
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('testUser@test.com');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 未输入内容
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 管理员界面
  // user用户登录测试
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('textbox', { name: '管理员邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '管理员邮箱' }).fill('testUser@test.com');
  await page.getByRole('textbox', { name: '管理员密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '管理员密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '管理员登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // Admin用户登录测试
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('textbox', { name: '管理员邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '管理员邮箱' }).fill('testAdmin@test.com');
  await page.getByRole('textbox', { name: '管理员密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '管理员密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '管理员登录' }).clickAndWait();

  let adminToken = await waitForToken(page, 'AdminToken');
  expect(adminToken).not.toBeNull();
  await page.evaluate(() => localStorage.clear());

  // SuperAdmin用户登录测试
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('textbox', { name: '管理员邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '管理员邮箱' }).fill('testSuperAdmin@test.com');
  await page.getByRole('textbox', { name: '管理员密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '管理员密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '管理员登录' }).clickAndWait();

  adminToken = await waitForToken(page, 'AdminToken');
  expect(adminToken).not.toBeNull();
  await page.evaluate(() => localStorage.clear());

  // 邮箱格式错误
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('test@');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 用户不存在
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('test@test.com');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 密码错误
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('testAdmin@test.com');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('111111');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 未输入邮箱
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('textbox', { name: '密码' }).clickAndWait();
  await page.getByRole('textbox', { name: '密码' }).fill('Shajie@123');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 未输入密码
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('textbox', { name: '邮箱' }).clickAndWait();
  await page.getByRole('textbox', { name: '邮箱' }).fill('testUser@test.com');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());


  // 未输入内容
  await page.goto('http://localhost:5173/');
  await page.getByRole('button', { name: '我知道了' }).clickAndWait();
  await page.goto('http://localhost:5173/admin-login');
  await page.getByRole('button', { name: '登录' }).clickAndWait();
  await expect(page).toHaveScreenshot({
    maxDiffPixelRatio: 0.01
  });
  await page.evaluate(() => localStorage.clear());

  console.log('✅ 登录测试完成！');


}, { timeout: 300000 });