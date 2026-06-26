# SPM Messenger - Build via GitHub API

## 📱 На Android/Termux

### Быстрая установка:
```bash
bash termux-setup.sh
```

Одна команда установит всё необходимое! ✅

### Или вручную:

#### 1. Обновление Termux
```bash
pkg update && pkg upgrade -y
```

#### 2. Установка инструментов
```bash
pkg install -y curl git nano openssh
```

#### 3. Создание GitHub токена
- Перейдите на https://github.com/settings/tokens
- Создайте новый токен (classic)
- Права: `repo` + `workflow`
- Скопируйте токен

#### 4. Экспорт токена в Termux
```bash
export GITHUB_TOKEN='your_github_token_here'
```

**Или добавьте в ~/.bashrc для постоянного хранения:**
```bash
echo "export GITHUB_TOKEN='your_token'" >> ~/.bashrc
source ~/.bashrc
```

#### 5. Клонирование репозитория
```bash
git clone https://github.com/sargmike228/SPM-Messenger.git
cd SPM-Messenger
```

#### 6. Запуск сборки через API
```bash
bash build-via-api.sh
```

#### 7. Отслеживание прогресса
- Откройте https://github.com/sargmike228/SPM-Messenger/actions
- Смотрите лог сборки в реальном времени
- Дождитесь завершения (~5-10 минут)

#### 8. Скачивание APK
- На странице workflow нажмите на вкладку "Artifacts"
- Скачайте:
  - `SPM-Messenger-debug` (для тестирования)
  - `SPM-Messenger-release` (подписанная версия)

---

## 🔐 Настройка подписи (на локальном компьютере)

### 1. Создание keystore
```bash
bash setup-keystore.sh
```

Эта команда:
- Создаст файл `release.keystore`
- Конвертирует в Base64 → `keystore_base64.txt`
- Добавит в .gitignore

### 2. Добавление Secrets в GitHub

Перейдите на https://github.com/sargmike228/SPM-Messenger/settings/secrets/actions

Добавьте 4 secrets:

| Name | Value |
|------|-------|
| KEYSTORE_BASE64 | Содержимое keystore_base64.txt |
| KEYSTORE_PASSWORD | SPMMessenger123!@# |
| KEY_ALIAS | spm_messenger_key |
| KEY_PASSWORD | SPMMessenger123!@# |

### 3. Коммит конфигурации
```bash
git add .gitignore .github/workflows/release-build.yml
git commit -m "Setup release build with signing"
git push
```

---

## 🚀 Полный workflow

### На локальном компьютере (один раз):
```bash
# Клон репо
git clone https://github.com/sargmike228/SPM-Messenger.git
cd SPM-Messenger

# Создание keystore
bash setup-keystore.sh

# Просмотр Base64
cat keystore_base64.txt

# Добавление secrets в GitHub вручную
# (см. раздел выше)

# Пуш конфигурации
git add .gitignore .github/workflows/release-build.yml
git commit -m "Setup release build"
git push
```

### На Android/Termux (каждый раз):
```bash
# Установка окружения (один раз)
bash termux-setup.sh

# Экспорт токена
export GITHUB_TOKEN='your_token'

# Клон репо
git clone https://github.com/sargmike228/SPM-Messenger.git
cd SPM-Messenger

# Запуск сборки через API
bash build-via-api.sh

# Отслеживание: https://github.com/sargmike228/SPM-Messenger/actions
```

---

## 📊 Результаты

После завершения сборки:

```
Artifacts:
├── SPM-Messenger-debug/
│   └── app-debug.apk (для тестирования)
└── SPM-Messenger-release/
    └── SPM-Messenger-release-signed.apk (для рилиза)
```

---

## 🔍 Проверка статуса

### Через GitHub Web:
1. https://github.com/sargmike228/SPM-Messenger/actions
2. Нажмите на последний workflow
3. Смотрите лог и прогресс

### Через API из Termux:
```bash
curl -H "Authorization: token $GITHUB_TOKEN" \
  https://api.github.com/repos/sargmike228/SPM-Messenger/actions/workflows/release-build.yml/runs \
  -s | grep -o '"conclusion":"[^"]*"'
```

---

## ⚠️ Важные моменты

✅ **Делайте:**
- Хранить GITHUB_TOKEN в переменной окружения
- Использовать токены с ограниченными правами
- Проверять логи перед скачиванием APK

❌ **Не делайте:**
- Коммитить release.keystore в Git
- Делиться токеном с другими
- Хранить токен в plaintext файлах

---

## 🆘 Решение проблем

### "GITHUB_TOKEN не установлен"
```bash
export GITHUB_TOKEN='your_token_here'
echo $GITHUB_TOKEN  # Проверка
```

### "Workflow не запустился"
- Проверьте права токена (нужны repo + workflow)
- Проверьте названия owner/repo в скрипте
- Проверьте что файл `.github/workflows/release-build.yml` существует

### "Build failed"
- Откройте лог на https://github.com/sargmike228/SPM-Messenger/actions
- Посмотрите детали ошибки
- Проверьте Android SDK версию

### "APK не подписан"
- Проверьте secrets добавлены правильно
- Проверьте KEYSTORE_BASE64 содержит полный Base64
- Проверьте пароли в secrets

---

## 📚 Полезные ссылки

- GitHub API Docs: https://docs.github.com/rest
- GitHub Actions: https://docs.github.com/actions
- Android Signing: https://developer.android.com/studio/publish/app-signing
- Termux: https://termux.com

---

**Готово! Теперь вы можете собирать SPM Messenger из Termux на Android! 🚀**
