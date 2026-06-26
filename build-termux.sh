#!/bin/bash

# Скрипт для сборки в Termux
# Использование: ./build-termux.sh

echo "╔═══════════════════════════════════════════╗"
echo "║  SPM Messenger Build Script для Termux    ║"
echo "╚═══════════════════════════════════════════╝"
echo ""

# Обновление Termux
echo "📦 Обновление пакетов Termux..."
pkg update -y
pkg upgrade -y
echo ""

# Установка зависимостей
echo "📥 Установка необходимых пакетов..."
pkg install -y git
pkg install -y openjdk-17
pkg install -y gradle
echo ""

# Проверка Java
echo "🔍 Проверка Java версии..."
java -version
echo ""

# Клонирование репозитория (если нужно)
if [ ! -d "SPM-Messenger" ]; then
    echo "📥 Клонирование репозитория SPM-Messenger..."
    git clone https://github.com/sargmike228/SPM-Messenger.git
    cd SPM-Messenger
else
    echo "✅ Репозиторий уже клонирован"
    cd SPM-Messenger
fi
echo ""

# Увеличение памяти для Gradle
echo "⚙️ Настройка параметров Gradle..."
export GRADLE_OPTS="-Xmx1024m -Xms512m"
echo ""

# Сборка
echo "🔨 Начало сборки..."
echo ""

echo "1️⃣ Очистка предыдущей сборки..."
./gradlew clean --parallel
echo ""

echo "2️⃣ Синхронизация зависимостей..."
./gradlew --refresh-dependencies
echo ""

echo "3️⃣ Сборка Debug APK..."
./gradlew assembleDebug --parallel --daemon
echo ""

echo "4️⃣ Сборка Release APK..."
./gradlew assembleRelease --parallel --daemon
echo ""

echo "╔═══════════════════════════════════════════╗"
echo "║     ✅ Сборка завершена успешно!         ║"
echo "╚═══════════════════════════════════════════╝"
echo ""
echo "📱 APK файлы находятся в:"
echo "  Debug:   app/build/outputs/apk/debug/app-debug.apk"
echo "  Release: app/build/outputs/apk/release/app-release.apk"
echo ""
echo "💾 Размер файлов:"
ls -lh app/build/outputs/apk/*/
echo ""
echo "📲 Для установки на устройство:"
echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
echo ""
