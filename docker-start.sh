#!/bin/bash
# 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰


# Docker 启动脚本
# 使用方法: ./docker-start.sh [dev|prod]

set -e

ENV=${1:-dev}

echo "=========================================="
echo "  加密货币交易平台 - Docker 启动脚本"
echo "=========================================="
echo ""

# 检查 Docker 是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ 错误: 未安装 Docker"
    echo "请先安装 Docker: https://docs.docker.com/get-docker/"
    exit 1
fi

# 检查 Docker Compose 是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "❌ 错误: 未安装 Docker Compose"
    echo "请先安装 Docker Compose: https://docs.docker.com/compose/install/"
    exit 1
fi

# 检查 .env 文件
if [ ! -f .env ]; then
    echo "⚠️  警告: .env 文件不存在"
    echo "正在从 .env.example 创建 .env 文件..."
    cp .env.example .env
    echo "✅ 已创建 .env 文件，请根据实际情况修改配置"
    echo ""
    read -p "是否继续启动？(y/n) " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# 创建必要的目录
echo "📁 创建必要的目录..."
mkdir -p logs
mkdir -p uploads
echo "✅ 目录创建完成"
echo ""

# 根据环境选择配置文件
if [ "$ENV" = "prod" ]; then
    echo "🏭 使用生产环境配置"
    COMPOSE_FILE="-f docker-compose.yml -f docker-compose.prod.yml"
else
    echo "🔧 使用开发环境配置"
    COMPOSE_FILE="-f docker-compose.yml"
fi

# 停止现有服务
echo "🛑 停止现有服务..."
docker-compose $COMPOSE_FILE down
echo "✅ 服务已停止"
echo ""

# 构建镜像
echo "🔨 构建 Docker 镜像..."
docker-compose $COMPOSE_FILE build --no-cache
echo "✅ 镜像构建完成"
echo ""

# 启动服务
echo "🚀 启动服务..."
docker-compose $COMPOSE_FILE up -d
echo "✅ 服务启动完成"
echo ""

# 等待服务就绪
echo "⏳ 等待服务就绪..."
sleep 10

# 检查服务状态
echo "📊 服务状态:"
docker-compose $COMPOSE_FILE ps
echo ""

# 检查健康状态
echo "🏥 检查服务健康状态..."
MAX_RETRIES=30
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if curl -f http://localhost:8080/api/actuator/health &> /dev/null; then
        echo "✅ 应用服务健康检查通过"
        break
    fi
    RETRY_COUNT=$((RETRY_COUNT + 1))
    echo "⏳ 等待应用启动... ($RETRY_COUNT/$MAX_RETRIES)"
    sleep 2
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    echo "⚠️  警告: 应用服务健康检查超时"
    echo "请查看日志: docker-compose logs api"
else
    echo ""
    echo "=========================================="
    echo "✅ 所有服务启动成功！"
    echo "=========================================="
    echo ""
    echo "📝 服务信息:"
    echo "  - API 服务: http://localhost:8080/api"
    echo "  - Swagger 文档: http://localhost:8080/api/swagger-ui.html"
    echo "  - MySQL: localhost:3306"
    echo "  - Redis: localhost:6379"
    echo ""
    echo "📋 常用命令:"
    echo "  - 查看日志: docker-compose logs -f"
    echo "  - 停止服务: docker-compose down"
    echo "  - 重启服务: docker-compose restart"
    echo ""
fi







