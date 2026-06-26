#!/bin/bash

# SPM Messenger Build Script для GitHub Actions и локальной сборки
# Использование: ./build.sh [debug|release]

set -e

BUILD_TYPE=${1:-debug}

echo "====================================="
echo "SPM Messenger Build Script"
echo "====================================="
echo "Build Type: $BUILD_TYPE"
echo ""

# Проверка JDK
if ! command -v java &> /dev/null; then
    echo "❌ Java не установлена! Пожалуйста установите JDK 17"
    exit 1
fi

echo "✅ Java версия:"
java -version
echo ""

# Проверка Gradle
if [ ! -f "gradlew" ]; then
    echo "❌ gradlew не найден!"
    exit 1
fi

echo "✅ Давая права на выполнение для gradlew..."
chmod +x gradlew
echo ""

# Очистка предыдущей сборки
echo "🧹 Очистка предыдущей сборки..."
./gradlew clean
echo ""

# Синхронизация Gradle
echo "🔄 Синхронизация Gradle зависимостей..."
./gradlew --refresh-dependencies
echo ""

# Проверка кода
echo "🔍 Проверка кода (Lint)..."
./gradlew lint || echo "⚠️ Lint завершился с предупреждениями"
echo ""

# Сборка
if [ "$BUILD_TYPE" = "release" ]; then
    echo "📦 Сборка Release APK..."
    ./gradlew assembleRelease --stacktrace
    echo ""
    echo "✅ Release APK собран!"
    echo "📁 Путь: app/build/outputs/apk/release/app-release.apk"
    echo ""
    echo "📦 Сборка Release Bundle..."
    ./gradlew bundleRelease --stacktrace
    echo ""
    echo "✅ Release Bundle собран!"
    echo "📁 Путь: app/build/outputs/bundle/release/app-release.aab"
elif [ "$BUILD_TYPE" = "debug" ]; then
    echo "🐛 Сборка Debug APK..."
    ./gradlew assembleDebug --stacktrace
    echo ""
    echo "✅ Debug APK собран!"
    echo "📁 Путь: app/build/outputs/apk/debug/app-debug.apk"
else
    echo "❌ Неизвестный тип сборки: $BUILD_TYPE"
    echo "Использование: ./build.sh [debug|release]"
    exit 1
fi

echo ""
echo "====================================="
echo "✅ Сборка завершена успешно!"
echo "====================================="
