#!/bin/bash

# 🚀 API-based Build Trigger Script
# Для запуска из Termux на Android
# Использование: bash build-via-api.sh

echo "╔════════════════════════════════════════════════════════════════════════╗"
echo "║      SPM Messenger - Build via GitHub API (Termux/Android)            ║"
echo "╚════════════════════════════════════════════════════════════════════════╝"
echo ""

# Переменные
REPO_OWNER="sargmike228"
REPO_NAME="SPM-Messenger"
BRANCH="main"
WORKFLOW_FILE="release-build.yml"

echo "📋 Конфигурация:"
echo "  Owner: $REPO_OWNER"
echo "  Repo: $REPO_NAME"
echo "  Branch: $BRANCH"
echo "  Workflow: $WORKFLOW_FILE"
echo ""

# Проверка GitHub token
if [ -z "$GITHUB_TOKEN" ]; then
    echo "❌ GITHUB_TOKEN не установлен!"
    echo ""
    echo "📝 Установите токен:"
    echo "   export GITHUB_TOKEN='your_github_token_here'"
    echo ""
    echo "🔗 Создайте токен на: https://github.com/settings/tokens"
    echo "   Требуемые права:"
    echo "   - repo (полный доступ к репозиторию)"
    echo "   - workflow (управление workflows)"
    exit 1
fi

echo "✅ GITHUB_TOKEN найден"
echo ""

# Проверка curl
if ! command -v curl &> /dev/null; then
    echo "❌ curl не найден!"
    echo "   Установите: pkg install -y curl"
    exit 1
fi

echo "✅ curl найден"
echo ""

# Запуск workflow через API
echo "🚀 Запускаю workflow через GitHub API..."
echo ""

RESPONSE=$(curl -X POST \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  "https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/actions/workflows/$WORKFLOW_FILE/dispatches" \
  -d "{\"ref\":\"$BRANCH\"}" \
  2>&1)

echo "📮 Ответ API:"
echo "$RESPONSE"
echo ""

# Проверка результата
if echo "$RESPONSE" | grep -q "No Content\|204"; then
    echo "✅ Workflow успешно запущен!"
    echo ""
    echo "🔗 Отслеживайте прогресс на:"
    echo "   https://github.com/$REPO_OWNER/$REPO_NAME/actions"
    echo ""
    echo "⏱️  Обычно сборка занимает 5-10 минут"
    echo ""
    echo "💡 Советы:"
    echo "   1. Откройте Actions в браузере"
    echo "   2. Нажмите на последний workflow"
    echo "   3. После завершения скачайте APK"
else
    echo "❌ Ошибка при запуске workflow!"
    echo ""
    echo "Возможные причины:"
    echo "  - Неверный GITHUB_TOKEN"
    echo "  - Неверные учётные данные"
    echo "  - Workflow файл не найден"
    echo ""
    echo "Попробуйте:"
    echo "  1. Проверьте токен: echo \$GITHUB_TOKEN"
    echo "  2. Проверьте права токена"
    echo "  3. Проверьте названия owner/repo"
    exit 1
fi
