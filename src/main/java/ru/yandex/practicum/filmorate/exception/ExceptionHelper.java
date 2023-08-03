package ru.yandex.practicum.filmorate.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHelper {
    public String getStackTrace(final Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
