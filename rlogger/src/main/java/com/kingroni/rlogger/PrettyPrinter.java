package com.kingroni.rlogger;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrettyPrinter implements Printer {
    private int lineLength;
    private boolean printTime;

    public PrettyPrinter(int lineLength, boolean printTime) {
        this.lineLength = lineLength;
        this.printTime = printTime;
    }

    @Override
    public void print(String logTag, LogLevel level, String message) {
        String formattedMessage = formatMessage(message);

        switch (level) {
            case VERBOSE:
                Log.v(logTag, formattedMessage);
                break;
            case DEBUG:
                Log.d(logTag, formattedMessage);
                break;
            case INFO:
                Log.i(logTag, formattedMessage);
                break;
            case WARNING:
                Log.w(logTag, formattedMessage);
                break;
            case ERROR:
                Log.e(logTag, formattedMessage);
                break;
            case WTF:
                Log.wtf(logTag, formattedMessage);
                break;
            default:
                // Handle unknown log level or implement custom behavior
                break;
        }
    }

    private String formatMessage(String message) {
        if (lineLength <= 0) {
            return message;
        }

        String[] lines = message.split("\\n");
        StringBuilder formattedMessage = new StringBuilder();
        for (String line : lines) {
            formattedMessage.append(formatLine(line)).append("\n");
        }
        return formattedMessage.toString();
    }

    private String formatLine(String line) {
        if (lineLength > 0 && line.length() > lineLength) {
            int endIndex = Math.min(lineLength, line.length());
            line = line.substring(0, endIndex);
        }
        if (printTime) {
            line = addTimestampAndThreadInfo(line);
        } else {
            line = "\n" + line;
        }
        return line;
    }

    private String addTimestampAndThreadInfo(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        String timestamp = sdf.format(new Date(System.currentTimeMillis()));
        String threadInfo = "[" + Thread.currentThread().getName() + "]";
        return timestamp + " " + threadInfo + " " + message;
    }
}
