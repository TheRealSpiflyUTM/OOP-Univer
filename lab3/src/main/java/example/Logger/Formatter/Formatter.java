package example.Logger.Formatter;

import example.Logger.LogLevel;

public interface Formatter {
    String getMsg(LogLevel msgLevel, String name, String message , Object... args);
}
