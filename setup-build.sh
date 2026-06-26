#!/bin/bash

# Скрипт установки окружения для сборки
# Поддерживает: Linux, macOS, Termux

echo "╔════════════════════════════════════════════════╗"
echo "║  SPM Messenger - Setup Build Environment       ║"
echo "╚════════════════════════════════════════════════╝"
echo ""

# Определение ОС
OS_TYPE="$(uname -s)"
echo "🖥️  Обнаруженная ОС: $OS_TYPE"
echo ""

case "$OS_TYPE" in
    Linux*)
        echo "📦 Установка для Linux..."
        sudo apt-get update
        sudo apt-get install -y openjdk-17-jdk
        sudo apt-get install -y gradle
        sudo apt-get install -y git
        ;;
    Darwin*)
        echo "📦 Установка для macOS..."
        # Проверка Homebrew
        if ! command -v brew &> /dev/null; then
            echo "❌ Homebrew не установлен. Установите с https://brew.sh"
            exit 1
        fi
        brew install openjdk@17
        brew install gradle
        brew install git
        ;;
    *)
        echo "❓ Неизвестная ОС: $OS_TYPE"
        echo "⚠️  Пожалуйста установите вручную:"
        echo "   - JDK 17 (https://www.oracle.com/java/technologies/downloads/)"
        echo "   - Gradle 8.0+ (https://gradle.org/releases/)"
        echo "   - Git (https://git-scm.com/)"
        exit 1
        ;;
esac

echo ""
echo "✅ Проверка установленного ПО:"
echo ""

if command -v java &> /dev/null; then
    echo "✅ Java:"
    java -version
else
    echo "❌ Java не установлена!"
    exit 1
fi

echo ""

if command -v gradle &> /dev/null; then
    echo "✅ Gradle:"
    gradle --version
else
    echo "❌ Gradle не установлен!"
    exit 1
fi

echo ""

if command -v git &> /dev/null; then
    echo "✅ Git:"
    git --version
else
    echo "❌ Git не установлен!"
    exit 1
fi

echo ""
echo "╔════════════════════════════════════════════════╗"
echo "║     ✅ Окружение установлено успешно!         ║"
echo "╚════════════════════════════════════════════════╝"
echo ""
echo "🚀 Теперь вы готовы к сборке!"
echo ""
echo "Команды для сборки:"
echo "  Debug:   ./gradlew assembleDebug"
echo "  Release: ./gradlew assembleRelease"
echo ""
