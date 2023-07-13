# RLogger [![](https://jitpack.io/v/roniaronson/RLogger.svg)](https://jitpack.io/#roniaronson/RLogger)

📝 RLogger is a powerful logger library for Android applications. It provides convenient logging capabilities with bordered logs, activity names, log timestamps, log types (verbose, info, warning, debug, error, etc.), and thread information. RLogger also offers automatic log saving for each session in shared preferences and the ability to export logs to your own database.

## 🚀 Features

- 🖼️ Bordered logs with activity name, timestamp, log type, and thread information
- 🔢 Logging levels: verbose, debug, info, warning, error, and what a terrible failure (wtf)
- 💾 Automatic log saving for each session
- 📤 Export logs to your own database

## ⬇️ Installation

RLogger can be installed via [JitPack](https://jitpack.io/). Follow these steps to add RLogger to your Android project:

- Add the JitPack repository to your root `build.gradle` file:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- Add the RLogger dependency to your app's `build.gradle` file:

```groovy
dependencies {
      implementation 'com.github.roniaronson:RLogger:Tag'
}
```

- Sync your project to include the RLogger library.

## Prerequisites
In order to enable the coloring of the debug logs, you need to open you Android Studio IDE -> Setting/Preferences -> Search LogCat:
- Set the following HexCode colors:
```text
Assert: 9C27B0

Debug: 2196F3

Error: F44336

Info: 4CAF50

Warning: FFC107
```
![Screenshot 2023-07-12 at 11 17 47](https://github.com/roniaronson/RLogger/assets/62941996/85fc3170-b52c-41d0-a11a-10f781665207)

## Demo Output:
![Screenshot 2023-07-12 at 11 17 10](https://github.com/roniaronson/RLogger/assets/62941996/4332350a-9b19-4fac-b7b7-2e1caeda18ae)

## 🎯 Usage

To start using RLogger, follow these steps:

- Initialize the logger with a printer and context:

```java
Printer printer = new PrettyPrinter(120, true);
Logger logger = Logger.getInstance(printer, this);
```

- Use the logger to log messages in your code:

```java
logger.v(getLocalClassName(), "Verbose log");
logger.d(getLocalClassName(), "Debug log");
logger.i(getLocalClassName(), "Info log");
logger.w(getLocalClassName(), "Warning log");
logger.e(getLocalClassName(), "Error log");
logger.wtf(getLocalClassName(), "What a terrible failure log");
```

- Retrieve and export logs when the session ends (in `onPaused` or `onDestroy`):

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    // Retrieve the log messages from shared preferences
    Map<String, String> logMessages = logger.getLogMessages();

    // Print the log messages to the console
    for (Map.Entry<String, String> entry : logMessages.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
    }

    // Send the logs of the current session to your own database
    logger.clearLogMessages();
}

@Override
protected void onPause() {
    super.onPause();
    
    // Retrieve the log messages from shared preferences
    Map<String, String> logMessages = logger.getLogMessages();

    // Print the log messages to the console
    for (Map.Entry<String, String> entry : logMessages.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
    }

    // Send the logs of the current session to your own database
    logger.clearLogMessages();
}
```

## 🛠️ Customization

RLogger provides various customization options:

- **Printer**: RLogger supports different printers for formatting logs. The example above uses `PrettyPrinter` with a line length of 120 characters and border enabled. You can use other printer implementations or create your own by implementing the `Printer` interface.
- **Log Levels**: RLogger supports different log levels (`v`, `d`, `i`, `w`, `e`, `wtf`). Use the appropriate log level method according to the desired log severity.
- **Log Format**: The format of the log can be customized by modifying the printer implementation.
- **Export to Database**: To export logs to your own database, modify the code in `onDestroy` or `onPause` to send the log messages to your desired database.

## 🤝 Contributing

Contributions to RLogger are welcome! If you encounter any issues or have suggestions for improvements, please create an issue or submit a pull request on the GitHub repository.

## 📄 License

RLogger is released under the [MIT License](https://opensource.org/licenses/MIT).
