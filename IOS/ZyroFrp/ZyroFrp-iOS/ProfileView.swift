import SwiftUI

struct ProfileView: View {
    // MARK: - 登录/注册/重置密码状态
    @State private var email = ""
    @State private var password = ""
    @State private var code = ""
    @State private var isRegister = false
    @State private var isReset = false
    @State private var isCounting = false
    @State private var countdown = 60
    @State private var showMessage = false
    @State private var messageContent = ""
    
    // MARK: - 登录成功用户信息
    @State private var isLoggedIn = false
    @State private var userInfo = UserInfo(email: "", userKey: "", remainingTraffic: 0, uploadLimit: 0, downloadLimit: 0, vipStatus: "普通用户", createdAt: Date())
    
    @State private var timer: Timer?
    
    // MARK: - API错误码
    private let loginErrorCodes = ["LOGIN_4101","LOGIN_4102","LOGIN_4103"]
    private let registerErrorCodes = ["REGISTER_4001","REGISTER_4002","REGISTER_4003","REGISTER_4004","REGISTER_4005","REGISTER_4006","EMAIL_SEND_4301"]
    private let passwordResetErrorCodes = ["PASSWORD_RESET_4201","PASSWORD_RESET_4202","PASSWORD_RESET_4203","PASSWORD_RESET_4204","PASSWORD_RESET_4205","PASSWORD_RESET_4206","PASSWORD_RESET_4207","EMAIL_SEND_4301"]
    
    var body: some View {
        NavigationView {
            VStack {
                HeaderView()
                
                if !isLoggedIn {
                    formView
                        .padding()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(10)
                        .padding()
                } else {
                    profileView
                        .padding()
                }
                
                Spacer()
            }
            .navigationBarHidden(true)
            .alert(isPresented: $showMessage) {
                Alert(title: Text("提示"), message: Text(messageContent), dismissButton: .default(Text("确定")))
            }
        }
    }
    
    // MARK: - 表单视图
    var formView: some View {
        VStack(spacing: 16) {
            Text(isRegister ? "注册" : isReset ? "重置密码" : "登录")
                .font(.title)
            
            TextField("邮箱", text: $email)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .keyboardType(.emailAddress)
            
            if isRegister || isReset {
                HStack {
                    TextField("验证码", text: $code)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                    Button(action: sendCode) {
                        Text(isCounting ? "\(countdown)s" : "获取验证码")
                            .padding(8)
                            .background(Color.blue)
                            .foregroundColor(.white)
                            .cornerRadius(6)
                    }
                    .disabled(isCounting)
                }
            }
            
            SecureField(isReset ? "新密码" : "密码", text: $password)
                .textFieldStyle(RoundedBorderTextFieldStyle())
            
            Button(action: submitAction) {
                Text(isRegister ? "注册" : isReset ? "重置密码" : "登录")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.green)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            
            HStack {
                if !isReset {
                    Button(action: { toggleForm() }) {
                        Text(isRegister ? "已有账号？去登录" : "没有账号？去注册")
                            .font(.footnote)
                    }
                    
                    Button(action: { goReset() }) {
                        Text("忘记密码?")
                            .font(.footnote)
                    }
                }
            }
        }
    }
    
    // MARK: - 用户信息视图
    var profileView: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text("欢迎，\(userInfo.email)")
                    .font(.title)
                
                Text("VIP状态：\(userInfo.vipStatus)")
                Text("PriToken：\(userInfo.userKey)")
                Text("剩余流量：\(userInfo.remainingTraffic) GB")
                Text("上传速率：\(userInfo.uploadLimit) Mb/s")
                Text("下载速率：\(userInfo.downloadLimit) Mb/s")
                Text("已陪伴：\(daysSince(date: userInfo.createdAt)) 天")
                
                Button(action: logout) {
                    Text("退出登录")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.red)
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }
            }
            .padding()
        }
    }
    
    // MARK: - 方法
    func toggleForm() {
        isRegister.toggle()
        isReset = false
    }
    
    func goReset() {
        isReset = true
        isRegister = false
    }
    
    func showMessageBox(_ msg: String) {
        messageContent = msg
        showMessage = true
    }
    
    // MARK: - 发送验证码
    func sendCode() {
        guard !email.isEmpty else { showMessageBox("请输入邮箱"); return }
        guard let url = URL(string: "http://localhost:8085/emails/send-verification") else { return }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let type = isRegister ? "REGISTER" : "RESET_PASSWORD"
        let body = ["toEmail": email, "type": type]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        
        URLSession.shared.dataTask(with: request) { data, _, error in
            DispatchQueue.main.async {
                if let error = error {
                    showMessageBox("发送失败: \(error.localizedDescription)")
                    return
                }
                guard let data = data,
                      let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
                      let code = json["code"] as? String else { return }
                
                if (isRegister && registerErrorCodes.contains(code)) || (isReset && passwordResetErrorCodes.contains(code)) {
                    showMessageBox(json["message"] as? String ?? "发送失败")
                    return
                }
                
                showMessageBox("验证码发送成功")
                startCountdown()
            }
        }.resume()
    }
    
    func startCountdown() {
        isCounting = true
        countdown = 60
        timer?.invalidate()
        timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { t in
            countdown -= 1
            if countdown <= 0 {
                t.invalidate()
                isCounting = false
            }
        }
    }
    
    // MARK: - 提交表单
    func submitAction() {
        guard !email.isEmpty, !password.isEmpty else { showMessageBox("邮箱或密码为空"); return }
        var endpoint = ""
        var body: [String: Any] = [:]
        
        if isRegister {
            endpoint = "http://localhost:8085/auth/register"
            body = ["email":email, "password": password, "emailCode": code]
        } else if isReset {
            endpoint = "http://localhost:8085/auth/password"
            body = ["email": email, "newPassword": password, "emailCode": code]
        } else {
            endpoint = "http://localhost:8085/auth/login"
            body = ["email": email, "password": password]
        }
        
        guard let url = URL(string: endpoint) else { return }
        var request = URLRequest(url: url)
        request.httpMethod = isReset ? "PUT" : "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        
        URLSession.shared.dataTask(with: request) { data, _, error in
            DispatchQueue.main.async {
                if let error = error {
                    showMessageBox("请求失败: \(error.localizedDescription)")
                    return
                }
                guard let data = data,
                      let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
                      let code = json["code"] as? String? else { return }
                
                // 登录/注册/重置密码返回处理
                if let code = code {
                    if (isRegister && registerErrorCodes.contains(code)) || (isReset && passwordResetErrorCodes.contains(code)) || (!isRegister && !isReset && loginErrorCodes.contains(code)) {
                        showMessageBox(json["message"] as? String ?? "操作失败")
                        return
                    }
                }
                
                if isRegister {
                    showMessageBox("注册成功，请登录")
                    isRegister = false
                } else if isReset {
                    showMessageBox("密码修改成功，请登录")
                    isReset = false
                } else {
                    // 登录成功
                    if let dataDict = json["data"] as? [String: Any] {
                        userInfo.email = email
                        userInfo.userKey = dataDict["userKey"] as? String ?? "abc123"
                        userInfo.remainingTraffic = dataDict["remainingTraffic"] as? Int ?? 0
                        userInfo.uploadLimit = dataDict["uploadLimit"] as? Int ?? 0
                        userInfo.downloadLimit = dataDict["downloadLimit"] as? Int ?? 0
                        userInfo.vipStatus = dataDict["vipStatus"] as? String ?? "普通用户"
                        userInfo.createdAt = Date() // 如果接口有时间请解析
                        isLoggedIn = true
                    }
                }
            }
        }.resume()
    }
    
    func logout() {
        isLoggedIn = false
        email = ""
        password = ""
        code = ""
        isRegister = false
        isReset = false
    }
    
    func daysSince(date: Date) -> Int {
        return Calendar.current.dateComponents([.day], from: date, to: Date()).day ?? 0
    }
}

// MARK: - 用户信息模型
struct UserInfo {
    var email: String
    var userKey: String
    var remainingTraffic: Int
    var uploadLimit: Int
    var downloadLimit: Int
    var vipStatus: String
    var createdAt: Date
}

// MARK: - Header 占位
struct HeaderView: View {
    var body: some View {
        HStack {
            Text("FRP 管理平台")
                .font(.headline)
            Spacer()
        }
        .padding()
        .background(Color.gray.opacity(0.2))
    }
}
