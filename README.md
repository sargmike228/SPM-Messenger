# SPM Messenger 🚀

Полнофункциональный децентрализованный мессенджер для Android с поддержкой P2P соединений, шифрованием Signal Protocol и защитой конфиденциальности.

## Возможности

✅ **Текстовые сообщения** - Полностью зашифрованные сообщения через Signal Protocol
✅ **Голосовые сообщения** - Запись и отправка зашифрованного аудио
✅ **Голосовые звонки** - P2P звонки с шифрованием в реальном времени
✅ **Видеозвонки** - HD видеозвонки между пользователями
✅ **Отправка файлов** - Безопасная передача файлов без сервера
✅ **Контакты** - Управление контактами и профилями
✅ **P2P архитектура** - Без центрального сервера, прямое соединение между пользователями
✅ **Signal Protocol** - Современный протокол эндэнд шифрования
✅ **Красивый дизайн** - Material Design 3 с Jetpack Compose
✅ **Высокая безопасность** - Защита всех данных и коммуникаций

## Архитектура

```
app/
├── core/
│   ├── network/         # P2P соединения (WiFi Direct, Bluetooth, mDNS)
│   ├── database/        # Room + DataStore для хранения
│   ├── security/        # Signal Protocol + Crypto
│   └── ui/              # Compose UI компоненты
├── feature/
│   ├── messaging/       # Функционал сообщений
│   ├── calls/           # Голосовые и видео звонки
│   ├── contacts/        # Управление контактами
│   └── settings/        # Настройки приложения
└── shared/
    └── common/          # Общие утилиты и модели
```

## Технологический стек

- **Язык**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Архитектура**: MVVM + Dependency Injection (Hilt)
- **Сетевые соединения**: WiFi Direct, Bluetooth, mDNS
- **Шифрование**: Signal Protocol, Bouncy Castle
- **VoIP**: WebRTC
- **База данных**: Room + DataStore
- **Сборка**: Gradle

## Требования

- Android 7.0+ (API 24)
- Android Studio Arctic Fox или выше
- Java 17 JDK

## Сборка

```bash
# Клонировать репозиторий
git clone https://github.com/sargmike228/SPM-Messenger.git
cd SPM-Messenger

# Собрать debug версию
./gradlew assembleDebug

# Собрать release версию
./gradlew assembleRelease
```

## Лицензия

MIT License
