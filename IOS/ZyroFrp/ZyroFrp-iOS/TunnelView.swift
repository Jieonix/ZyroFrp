//
//  TunnelView.swift
//  ZyroFrp-iOS
//
//  Created by Kelvin on 2025/10/11.
//

import SwiftUI

// MARK: - 数据模型
struct Tunnel: Identifiable { // Tunnel 数据模型
    let id = UUID()
    var name: String
    var localPort: Int
    var remotePort: Int
    var isActive: Bool
}

// MARK: - 示例数据
let sampleTunnels: [Tunnel] = [
    Tunnel(name: "web-01", localPort: 8080, remotePort: 12000, isActive: true),
    Tunnel(name: "ssh-guest", localPort: 22, remotePort: 12022, isActive: false),
    Tunnel(name: "db-tunnel", localPort: 3306, remotePort: 13006, isActive: true),
    Tunnel(name: "web-02", localPort: 8081, remotePort: 12001, isActive: true),
    Tunnel(name: "ssh-admin", localPort: 2222, remotePort: 12023, isActive: false),
    Tunnel(name: "db-tunnel-2", localPort: 3307, remotePort: 13007, isActive: true)
]

// MARK: - 主视图
struct TunnelView: View { // 界面视图
    @State private var searchText: String = "" // 搜索框绑定状态
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {

                // 顶部通知按钮
                HStack {
                    Spacer()
                    Button(action: { /* TODO: 打开通知 */ }) {
                        Image(systemName: "bell")
                            .imageScale(.large)
                    }
                }

                // 搜索框
                HStack {
                    Image(systemName: "magnifyingglass")
                    TextField("搜索隧道或端口", text: $searchText)
                        .autocapitalization(.none)
                        .disableAutocorrection(true)
                }
                .padding(10)
                .background(Color(.systemGray6))
                .cornerRadius(8)

                // 快捷操作按钮
                HStack(spacing: 12) {
                    Button(action: { /* TODO: 新建隧道 */ }) {
                        VStack {
                            Image(systemName: "plus.circle")
                            Text("新建")
                                .font(.caption)
                        }
                    }

                    Button(action: { /* TODO: 刷新列表 */ }) {
                        VStack {
                            Image(systemName: "arrow.clockwise")
                            Text("刷新")
                                .font(.caption)
                        }
                    }

                    Spacer()
                }

                // 隧道列表
                VStack(alignment: .leading, spacing: 12) {
                    ForEach(sampleTunnels.filter {
                        searchText.isEmpty ? true : ($0.name.contains(searchText) || String($0.remotePort).contains(searchText))
                    }) { tunnel in
                        TunnelCard(tunnel: tunnel)
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Zyro FRP")
    }
}

// MARK: - 隧道卡片
struct TunnelCard: View {
    var tunnel: Tunnel
    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 6) {
                Text(tunnel.name)
                    .font(.headline)
                Text("本地: \(tunnel.localPort) → 远程: \(tunnel.remotePort)")
                    .font(.subheadline)
            }
            Spacer()
            Circle()
                .frame(width: 12, height: 12)
                .foregroundColor(tunnel.isActive ? .green : .gray)
        }
        .padding()
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(Color(.secondarySystemBackground))
                .shadow(radius: 1)
        )
    }
}

// MARK: - 预览
#Preview {
    NavigationStack { // 用 NavigationStack 包裹，方便显示导航标题
        TunnelView()
    }
}
