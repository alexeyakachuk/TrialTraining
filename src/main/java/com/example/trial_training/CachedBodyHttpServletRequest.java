package com.example.trial_training;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        // Читаем тело запроса в массив байт и сохраняем
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = requestInputStream.readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {
        // Создаём новый InputStream из кэшированного массива байт
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);

        // Возвращаем ServletInputStream, который читает из кэшированного массива
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true; // Всегда готов к чтению
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Не используем асинхронное чтение, оставляем пустым
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        // Возвращаем BufferedReader поверх нашего InputStream
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
