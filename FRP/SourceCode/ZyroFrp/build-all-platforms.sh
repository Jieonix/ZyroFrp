#!/bin/bash

echo "=== ZyroFrp 全平台构建开始 ==="
echo "$(date)"
echo ""

PROJECT_ROOT="$(pwd)"
BUILD_ROOT="$PROJECT_ROOT/builds"
CONF_DIR="$PROJECT_ROOT/conf"
LDFLAGS="-s -w"

# 删除旧的 builds
[ -d "$BUILD_ROOT" ] && rm -rf "$BUILD_ROOT"

# 创建一级目录：frps 和 frpc
mkdir -p "$BUILD_ROOT/frps" "$BUILD_ROOT/frpc"

PLATFORMS="macos windows linux"
ARCHS="amd64 arm64"

build_arch() {
    local bin_type="$1"
    local platform="$2"
    local goos="$3"
    local goarch="$4"
    local arch_name="$5"

    echo "  🛠️ 构建 $bin_type $platform $arch_name..."

    TARGET_DIR="$BUILD_ROOT/$bin_type/$platform/$arch_name"
    mkdir -p "$TARGET_DIR"

    local bin_file="$bin_type"
    [ "$platform" = "windows" ] && bin_file="$bin_file.exe"

    env CGO_ENABLED=0 GOOS=$goos GOARCH=$goarch \
        go build -trimpath -ldflags "$LDFLAGS" -tags "$bin_type" -o "$TARGET_DIR/$bin_file" "./cmd/$bin_type"

    [ -f "$CONF_DIR/$bin_type.toml" ] && cp "$CONF_DIR/$bin_type.toml" "$TARGET_DIR/$bin_type.toml"

    TAR_FILE="$BUILD_ROOT/$bin_type/$platform/$bin_type-$platform-$arch_name.tar.gz"
    (cd "$TARGET_DIR/.." && tar -czf "$TAR_FILE" "$arch_name")
    rm -rf "$TARGET_DIR"

    echo "    ✅ $bin_type $platform $arch_name 构建并压缩完成 -> $TAR_FILE"
}

build_platform() {
    local bin_type="$1"

    for platform in $PLATFORMS; do
        echo ""
        echo "🚀 开始构建 $bin_type 平台 $platform..."

        for arch in $ARCHS; do
            case "$platform:$arch" in
                macos:amd64) build_arch "$bin_type" "$platform" "darwin" "amd64" "amd64" ;;
                macos:arm64)  build_arch "$bin_type" "$platform" "darwin" "arm64" "arm64" ;;
                windows:amd64) build_arch "$bin_type" "$platform" "windows" "amd64" "amd64" ;;
                windows:arm64)  build_arch "$bin_type" "$platform" "windows" "arm64" "arm64" ;;
                linux:amd64)   build_arch "$bin_type" "$platform" "linux" "amd64" "amd64" ;;
                linux:arm64)    build_arch "$bin_type" "$platform" "linux" "arm64" "arm64" ;;
            esac
        done

        echo "✅ $bin_type 平台 $platform 构建完成"
    done
}

build_platform "frps"
build_platform "frpc"

echo ""
echo "🎉 全平台构建并压缩完成！目录结构："
if command -v tree >/dev/null 2>&1; then
    tree "$BUILD_ROOT" -L 3
else
    find "$BUILD_ROOT" -print | sed 's|[^/]*/|    |g'
fi
