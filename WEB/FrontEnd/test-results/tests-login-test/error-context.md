# Page snapshot

```yaml
- generic [active] [ref=e1]:
  - generic [ref=e3]:
    - banner [ref=e4]:
      - heading "ZyroFrp" [level=1] [ref=e5]
    - generic [ref=e7]:
      - heading "用户登录" [level=2] [ref=e8]
      - textbox "邮箱" [ref=e9]: testUser@test.com
      - textbox "密码" [ref=e10]: Shajie@123
      - button "登录" [ref=e11] [cursor=pointer]
      - generic [ref=e12]:
        - paragraph [ref=e13]:
          - link "忘记密码?" [ref=e14] [cursor=pointer]:
            - /url: /reset-password
        - paragraph [ref=e15]:
          - text: 还没有账号?
          - link "注册" [ref=e16] [cursor=pointer]:
            - /url: /register
  - img
  - generic [ref=e17]:
    - generic "Toggle devtools panel" [ref=e18] [cursor=pointer]:
      - img [ref=e19]
    - generic "Toggle Component Inspector" [ref=e24] [cursor=pointer]:
      - img [ref=e25]
```