package com.kingroni.rlogger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Logger {
    private static Logger instance;
    private Printer printer;
    private Context context;
    private static final String LOG_MESSAGES_KEY = "log_messages";

    private Logger(Printer printer, Context context) {
        this.printer = printer;
        this.context = context;
    }

    /**
     * @param printer - custom printer properties
     * @return custom object instance
     */
    public static synchronized Logger getInstance(Printer printer, Context context) {
        if (instance == null) {
            instance = new Logger(printer, context);
        }
        return instance;
    }

    /**
     * @return default object instance
     */
    public static synchronized Logger getInstance(Context context) {
        if (instance == null) {
            instance = new Logger(new PrettyPrinter(100, true), context); // default printer
        }
        return instance;
    }

    public void v(String tag, String message) {
        log(tag, LogLevel.VERBOSE, message);
    }

    public void d(String tag, String message) {
        log(tag, LogLevel.DEBUG, message);
    }

    public void i(String tag, String message) {
        log(tag, LogLevel.INFO, message);
    }

    public void w(String tag, String message) {
        log(tag, LogLevel.WARNING, message);
    }

    public void e(String tag, String message) {
        log(tag, LogLevel.ERROR, message);
    }

    public void wtf(String tag, String message) {
        log(tag, LogLevel.WTF, message);
    }

    private void log(String logTag, LogLevel level, String message) {
        String formattedMessage = formatMessage(level, message);
        printer.print(logTag, level, formattedMessage);

        // Retrieve existing log messages from shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String logMessages = sharedPreferences.getString(LOG_MESSAGES_KEY, "");

        // Concatenate the new log message to the existing log messages
        String logMessage = String.format("[%s] %s\n", level.toString(), formattedMessage);
        logMessages += logMessage;

        // Save the concatenated log messages to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOG_MESSAGES_KEY, logMessages);
        editor.apply();
    }


    private String formatMessage(LogLevel level, String message) {
        String timestampAndThreadInfo = addTimestampAndThreadInfo(message);
        String logBorder = getLogBorder(timestampAndThreadInfo);
        String logLevelTag = "[" + level.name() + "]";
        String logWithTag = logLevelTag + " " + timestampAndThreadInfo;

        return logBorder + logWithTag + logBorder;
    }

    private String addTimestampAndThreadInfo(String message) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        String timestamp = sdf.format(new Date(System.currentTimeMillis()));
        String threadInfo = "[" + Thread.currentThread().getName() + "]";
        return timestamp + " " + threadInfo + " " + message;
    }

    private String getLogBorder(String message) {
        int length = message.length();
        StringBuilder sb = new StringBuilder(length + 20);
        for (int i = 0; i < length + 16; i++) {
            sb.append("-");
        }
        sb.insert(0, "\n| ");
        sb.insert(8, " | ");
        sb.append(" |\n");
        return sb.toString();
    }

    public Map<String, String> getLogMessages() {
        Map<String, String> logMessages = new HashMap<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String logTag = entry.getKey();
            String logMessage = (String) entry.getValue();
            logMessages.put(logTag, logMessage);
        }
        return logMessages;
    }

    public void clearLogMessages() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }
}
