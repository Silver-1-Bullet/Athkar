# أذكاري — Athkar

> تطبيق ويدجت للأذكار اليومية | A minimalist Islamic remembrance counter with a home screen widget

## Status

| PR | Title | Status |
|----|-------|--------|
| [#1](https://github.com/Silver-1-Bullet/Athkar/pull/1) | Add Athkar home screen widget Android app | ✅ Merged |
| [#3](https://github.com/Silver-1-Bullet/Athkar/pull/3) | Add application for dhikr and istighfar interface | ✅ Ready |

---

## Features

- **Home Screen Widget** — Tap anywhere on the widget to increment the counter; a small `↺` button resets the daily count
- **Main App Screen** — Large circular tappable counter with spring-bounce animation (scale `0.92f` on press)
- **8 Dhikr Options** — Scrollable `FilterChip` row to switch between:
  `أستغفر الله`, `سبحان الله`, `الحمد لله`, `الله أكبر`, `لا إله إلا الله`, `لا حول ولا قوة إلا بالله`, `سبحان الله وبحمده`, `سبحان الله العظيم`
- **Daily & Lifetime Counters** — Today's count auto-rolls on date change; lifetime count persists forever
- **Midnight Auto-Reset** — `AlarmManager.setExactAndAllowWhileIdle` scheduled at next midnight; self-rescheduling; toggleable in settings
- **Boot Resilience** — `BootReceiver` re-schedules the midnight alarm after device reboot
- **Arabic RTL** — Full right-to-left text support; Tajawal font via Google Fonts provider
- **Dark Mode** — Material 3 light/dark color schemes; Teal Dark `#0D4D4D` / Gold Accent `#D4A843` palette

## Tech Stack

| | |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Widget | Glance AppWidget 1.0.0 (`SizeMode.Exact`) |
| Persistence | Preferences DataStore |
| Async | Kotlin Coroutines |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |

## Architecture

```
app/src/main/java/com/istighfar/counter/
├── IstighfarApp.kt                 # Application class — schedules midnight alarm on start
├── MainActivity.kt                 # Compose entry point
├── data/
│   └── CounterRepository.kt        # DataStore persistence (today_count, lifetime_count, last_date, …)
├── widget/
│   ├── IstighfarWidget.kt          # GlanceAppWidget — teal/gold UI
│   ├── IstighfarWidgetReceiver.kt  # GlanceAppWidgetReceiver
│   └── WidgetActions.kt            # IncrementAction & ResetTodayAction callbacks
├── ui/
│   ├── screens/HomeScreen.kt       # Main Compose screen
│   └── theme/                      # Color, Theme, Type (Material 3)
└── receiver/
    ├── MidnightResetReceiver.kt    # Alarm-triggered daily reset
    └── BootReceiver.kt             # Re-registers alarm after reboot
```

## Build

```bash
# Clone the repo
git clone https://github.com/Silver-1-Bullet/Athkar.git
cd Athkar

# Debug build
./gradlew assembleDebug

# Install on connected device / emulator
./gradlew installDebug

# Run unit tests
./gradlew test
```

> **Requirements:** Android Studio Hedgehog (2023.1) or newer, JDK 17, Android SDK 34.
