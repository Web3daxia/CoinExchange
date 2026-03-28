#!/bin/bash
# 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰


# Docker 停止脚本
# 使用方法: ./docker-stop.sh [dev|prod]

set -e

ENV=${1:-dev}

echo "=========================================="
echo "  加密货币交易平台 - Docker 停止脚本"
echo "=========================================="
echo ""

# 根据环境选择配置文件
if [ "$ENV" = "prod" ]; then
    echo "🏭 使用生产环境配置"
    COMPOSE_FILE="-f docker-compose.yml -f docker-compose.prod.yml"
else
    echo "🔧 使用开发环境配置"
    COMPOSE_FILE="-f docker-compose.yml"
fi

# 停止服务
echo "🛑 停止服务..."
docker-compose $COMPOSE_FILE down

echo ""
echo "=========================================="
echo "✅ 所有服务已停止"
echo "=========================================="
echo ""
echo "💡 提示:"
echo "  - 如需删除数据卷，请使用: docker-compose down -v"
echo "  - 如需查看日志，请使用: docker-compose logs"
echo ""







