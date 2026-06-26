#!/bin/bash

# 🔐 SPM Messenger - Setup Release Keystore
# Создаёт подписанный APK для GitHub Actions
# Использование: bash setup-keystore.sh

echo "╔════════════════════════════════════════════════════════════════════════╗"
echo "║          SPM Messenger - Setup Release Keystore & Build              ║"
echo "╚════════════════════════════════════════════════════════════════════════╝"
echo ""

# Параметры
KEYSTORE_FILE="release.keystore"
KEYSTORE_BASE64="keystore_base64.txt"
KEY_ALIAS="spm_messenger_key"
KEY_PASSWORD="SPMMessenger123!@#"
STORE_PASSWORD="SPMMessenger123!@#"
VALIDITY_DAYS="36500"  # 100 лет

echo "🔐 Параметры keystore:"
echo "  File: $KEYSTORE_FILE"
echo "  Alias: $KEY_ALIAS"
echo "  Validity: $VALIDITY_DAYS days"
echo ""

# Проверка наличия Java
if ! command -v keytool &> /dev/null; then
    echo "❌ keytool не найден! Установите Java JDK 17"
    exit 1
fi

echo "✅ Java найдена"
echo ""

# Удаление старого keystore если существует
if [ -f "$KEYSTORE_FILE" ]; then
    echo "⚠️  Старый keystore найден, удаляю..."
    rm -f "$KEYSTORE_FILE"
    rm -f "$KEYSTORE_BASE64"
fi

echo "🔨 Создание нового keystore..."
keytool -genkey -v \
  -keystore "$KEYSTORE_FILE" \
  -alias "$KEY_ALIAS" \
  -keyalg RSA \
  -keysize 2048 \
  -validity "$VALIDITY_DAYS" \
  -storepass "$STORE_PASSWORD" \
  -keypass "$KEY_PASSWORD" \
  -dname "CN=SPM Messenger, OU=Development, O=SPM, L=Moscow, ST=Moscow, C=RU" || exit 1

echo "✅ Keystore создан!"
echo ""

# Конвертирование в base64
echo "📝 Конвертирование в Base64..."
if [ -f "$KEYSTORE_FILE" ]; then
    base64 "$KEYSTORE_FILE" | tr -d '\n' > "$KEYSTORE_BASE64"
    echo "✅ Base64 файл создан: $KEYSTORE_BASE64"
    echo ""
    
    # Показываем размер
    SIZE=$(wc -c < "$KEYSTORE_BASE64")
    echo "📊 Размер keystore: $(stat -f%z "$KEYSTORE_FILE" 2>/dev/null || stat -c%s "$KEYSTORE_FILE") bytes"
    echo "📊 Размер Base64: $SIZE bytes"
else
    echo "❌ Ошибка: keystore не был создан"
    exit 1
fi

echo ""
echo "⚙️  Экспортирую переменные окружения для GitHub Actions..."
echo ""

echo "📋 Добавляю в .gitignore..."
echo "release.keystore" >> .gitignore
echo "keystore_base64.txt" >> .gitignore

echo ""
echo "╔════════════════════════════════════════════════════════════════════════╗"
echo "║                    ✅ Setup Complete!                                 ║"
echo "╚════════════════════════════════════════════════════════════════════════╝"
echo ""
echo "📝 Следующие шаги:"
echo ""
echo "1️⃣  Скопируйте содержимое keystore_base64.txt в GitHub Secrets:"
echo "   - Перейдите на https://github.com/sargmike228/SPM-Messenger/settings/secrets"
echo "   - Нажмите 'New repository secret'"
echo "   - Name: KEYSTORE_BASE64"
echo "   - Value: (содержимое файла keystore_base64.txt)"
echo ""
echo "2️⃣  Добавьте другие Secrets:"
echo "   - KEYSTORE_PASSWORD: $STORE_PASSWORD"
echo "   - KEY_ALIAS: $KEY_ALIAS"
echo "   - KEY_PASSWORD: $KEY_PASSWORD"
echo ""
echo "3️⃣  Закоммитьте изменения:"
echo "   git add .gitignore"
echo "   git commit -m 'Setup release build configuration'"
echo "   git push"
echo ""
echo "4️⃣  Или сразу запустите сборку:"
echo "   git commit --allow-empty -m 'Trigger release build'"
echo "   git push"
echo ""
echo "⚠️  ВАЖНО: Никогда не коммитьте release.keystore или keystore_base64.txt!"
echo "           Они уже добавлены в .gitignore"
echo ""
