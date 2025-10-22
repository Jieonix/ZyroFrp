//
//  Tabview.swift
//  ZyroFrp-iOS
//
//  Created by Kelvin on 2025/10/11.
//

import SwiftUI

struct TabViewRoot: View {
    var body: some View {
        TabView {
            NavigationStack {
                HomeView()
                    .navigationTitle("主页")
                    .toolbar {
                        ToolbarItem(placement: .navigationBarTrailing) {
                            Button("设置") {
                                print("点了设置")
                            }
                        }
                    }
            }
            .tabItem {
                Label("主页", systemImage: "house")
            }

            NavigationStack {
                TunnelView()
                    .navigationTitle("隧道")
            }
            .tabItem {
                Label("隧道", systemImage: "arrowshape.turn.up.right")
            }

            NavigationStack {
                UsageView()
                    .navigationTitle("用量")
            }
            .tabItem {
                Label("用量", systemImage: "chart.bar")
            }

            NavigationStack {
                ProfileView()
                    .navigationTitle("我")
            }
            .tabItem {
                Label("我", systemImage: "person.crop.circle")
            }
        }
    }
}

#Preview {
    TabViewRoot()
}
