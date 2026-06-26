# Команды для сборки SPM Messenger

## 🖥️ На локальном компьютере

### Требуемое ПО:
- JDK 17+
- Android SDK (API 34)
- Gradle 8.0+

### Сборка Debug версии:
```bash
./gradlew assembleDebug
```

### Сборка Release версии:
```bash
./gradlew assembleRelease
```

### Очистка и пересборка:
```bash
./gradlew clean assembleDebug
```

### Запуск тестов:
```bash
./gradlew test
```

### Запуск на эмуляторе/устройстве:
```bash
./gradlew installDebug
adb shell am start -n com.sargmike228.spm_messenger/.MainActivity
```

---

## 🔧 В Termux на серверах GitHub (или локально)

### Быстрая сборка (все в одной команде):
```bash
bash build-termux.sh
```

### По шагам в Termux:

#### 1. Обновление пакетов:
```bash
pkg update && pkg upgrade -y
```

#### 2. Установка Java 17:
```bash
pkg install -y openjdk-17
```

#### 3. Установка Git:
```bash
pkg install -y git
```

#### 4. Установка Gradle:
```bash
pkg install -y gradle
```

#### 5. Клонирование репозитория:
```bash
git clone https://github.com/sargmike228/SPM-Messenger.git
cd SPM-Messenger
```

#### 6. Проверка Java:
```bash
java -version
```

#### 7. Выдача прав на gradlew:
```bash
chmod +x gradlew
```

#### 8. Сборка Debug APK:
```bash
./gradlew assembleDebug
```

#### 9. Сборка Release APK:
```bash
./gradlew assembleRelease
```

---

## 🚀 На GitHub Actions (CI/CD)

Всё автоматически! Просто push в репозиторий:

### Workflow файлы:
- `.github/workflows/build.yml` - Сборка при каждом push
- `.github/workflows/test.yml` - Тестирование
- `.github/workflows/release.yml` - Релиз при создании тега

### Просмотр статуса сборки:
1. Перейти на GitHub: https://github.com/sargmike228/SPM-Messenger
2. Нажать на вкладку "Actions"
3. Видеть все сборки и статусы

### Скачивание артефактов:
1. Зайти на GitHub Actions
2. Открыть нужную сборку
3. Скачать APK из секции "Artifacts"

---

## 📦 Результаты сборки

### Debug APK:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release APK:
```
app/build/outputs/apk/release/app-release.apk
```

### Release Bundle (для Google Play):
```
app/build/outputs/bundle/release/app-release.aab
```

---

## 🐛 Отладка при ошибках

### Просмотр детальных логов:
```bash
./gradlew assembleDebug --stacktrace --debug
```

### Очистка кеша Gradle:
```bash
./gradlew --stop
rm -rf ~/.gradle/caches
./gradlew clean
```

### Обновление зависимостей:
```bash
./gradlew --refresh-dependencies
```

---

## 💾 Установка на устройство

### Через ADB (Android Debug Bridge):
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Запуск после установки:
```bash
adb shell am start -n com.sargmike228.spm_messenger/.MainActivity
```

### Просмотр логов:
```bash
adb logcat | grep "spm_messenger"
```

---

## ⚙️ Оптимизация сборки

### Параллельная сборка:
```bash
./gradlew assembleDebug --parallel
```

### С использованием Daemon:
```bash
./gradlew assembleDebug --daemon
```

### Увеличение памяти для Gradle:
```bash
export GRADLE_OPTS="-Xmx2048m -Xms512m"
./gradlew assembleDebug
```

---

## 🎯 Рекомендуемый процесс разработки

1. **Локальная разработка:**
   ```bash
   ./gradlew assembleDebug
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

2. **Перед commit:**
   ```bash
   ./gradlew test lint
   ```

3. **Release сборка:**
   ```bash
   ./gradlew assembleRelease bundleRelease
   ```

4. **CI/CD на GitHub:**
   - Автоматическая сборка при push
   - Скачивание APK из Actions
   - Развёртывание на тестовые устройства

---

## 📝 Примечания

- Первая сборка может занять 10-15 минут
- Убедитесь, что у вас достаточно памяти (минимум 4GB)
- Для Termux рекомендуется использование внешнего хранилища
- Release версия требует подписи ключом (настроить в release.yml)

---

**Готово! Теперь вы можете собирать SPM Messenger где угодно! 🚀**
