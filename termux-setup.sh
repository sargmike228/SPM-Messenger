#!/bin/bash

# 📱 Complete Termux Setup for Building via GitHub API
# Для Android/Termux пользователей
# Использование: bash termux-setup.sh

echo "╔════════════════════════════════════════════════════════════════════════╗"
echo "║         SPM Messenger - Termux Setup (Android Build)                  ║"
echo "╚════════════════════════════════════════════════════════════════════════╝"
echo ""

echo "📱 Обновление Termux..."
pkg update -y
pkg upgrade -y

echo ""
echo "📦 Установка необходимых пакетов..."
pkg install -y curl
pkg install -y git
pkg install -y nano
pkg install -y openssh

echo ""
echo "✅ Установка завершена!"
echo ""

echo "📝 Следующие шаги:"
echo ""
echo "1️⃣  Создайте GitHub Personal Access Token:"
echo "   - Перейдите на https://github.com/settings/tokens"
echo "   - Нажмите 'Generate new token (classic)'"
echo "   - Дайте название: 'Termux Build Token'"
echo "   - Выберите права:"
echo "     ☑ repo (полный доступ к репозиторию)"
echo "     ☑ workflow (управление workflows)"
echo "   - Скопируйте токен"
echo ""

echo "2️⃣  Установите токен в Termux:"
echo "   export GITHUB_TOKEN='your_token_here'"
echo ""
echo "   Или добавьте в ~/.bashrc для постоянного хранения:"
echo "   echo \"export GITHUB_TOKEN='your_token'\" >> ~/.bashrc"
echo ""

echo "3️⃣  Клонируйте репозиторий:"
echo "   git clone https://github.com/sargmike228/SPM-Messenger.git"
echo "   cd SPM-Messenger"
echo ""

echo "4️⃣  Запустите сборку через API:"
echo "   bash build-via-api.sh"
echo ""

echo "5️⃣  Скачайте APK:"
echo "   - Откройте https://github.com/sargmike228/SPM-Messenger/actions"
echo "   - Нажмите на последний workflow"
echo "   - Прокрутите вниз до 'Artifacts'"
echo "   - Скачайте APK файлы"
echo ""

echo "╔════════════════════════════════════════════════════════════════════════╗"
echo "║                    ✅ Setup Complete!                                 ║"
echo "╚════════════════════════════════════════════════════════════════════════╝"
echo ""
