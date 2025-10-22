// Copyright 2023 The frp Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package config

import (
	"sync"

	v1 "github.com/fatedier/frp/pkg/config/v1"
)

var (
	globalServerConfig *v1.ServerConfig
	globalConfigMutex  sync.RWMutex
)

// SetGlobalServerConfig 设置全局服务器配置
// 此函数应该在配置加载完成后调用
func SetGlobalServerConfig(cfg *v1.ServerConfig) {
	globalConfigMutex.Lock()
	defer globalConfigMutex.Unlock()
	globalServerConfig = cfg
}

// GetGlobalServerConfig 获取全局服务器配置
// 如果配置未设置，返回 nil
func GetGlobalServerConfig() *v1.ServerConfig {
	globalConfigMutex.RLock()
	defer globalConfigMutex.RUnlock()
	return globalServerConfig
}