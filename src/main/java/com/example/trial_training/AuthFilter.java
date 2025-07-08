package com.example.trial_training;

import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.model.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AuthFilter implements Filter {

    private static final List<String> EXCLUDED_PATHS = List.of("/auth/login", "/auth/logout",
            "/trainers", "/trainers/{id}");
    private final ObjectMapper objectMapper;

    public AuthFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String method = req.getMethod();

        //запрос на создание клиента или тренера, пропускаем фильтр (регистрация не требует аутентификации)
        if("POST".equals(method) && ("/clients".equals(path) || "/trainers".equals(path))) {
            chain.doFilter(request, response);
            return;
        }

        // Если путь входит в список исключений (например, вход/выход или получение списка тренеров), пропускаем фильтр
        if (EXCLUDED_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Получаем сессию без создания новой, если её нет - значит пользователь не авторизован
        HttpSession session = req.getSession(false);

        // Если сессия отсутствует или в ней нет атрибута username — возвращаем 401 (требуется аутентификация)
        if (session == null || session.getAttribute("username") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("Требуется аутентификация");
            return;
        }

        // Получаем userId из сессии и проверяем что это Integer (иначе бросаем исключение)
        Object userIdObj = session.getAttribute("userId");
        if (!((userIdObj) instanceof Integer)) {
            throw new AuthenticationException("Некоректныее данные сессии");
        }

        Integer userId = (Integer) userIdObj;
        Integer id = null;

        // Если URL начинается с /clients/, пытаемся вытащить id клиента из URL
        if (path.startsWith("/clients/")) {
            String idStr = path.substring("/clients/".length());
            id = Integer.parseInt(idStr);
        }

        // Если в URL есть id клиента и он не совпадает с userId из сессии — выбрасываем исключение (попытка доступа к чужим данным)
        if (id != null && !userId.equals(id)) {
            throw new AuthenticationException("Некоректныее данные сессии");
        }

        // Если это PUT-запрос на обновление клиента ("/clients"), проверяем тело запроса
        if ("PUT".equals(method) && "/clients".equals(path)) {
            // Оборачиваем запрос, чтобы можно было прочитать тело несколько раз
            CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);
            Client client;
            try {
                // Парсим JSON из тела запроса в объект Client
                client = objectMapper.readValue(cachedReq.getInputStream(), Client.class);
            } catch (Exception e) {
                // Если JSON некорректный — возвращаем 400
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/plain; charset=UTF-8");
                resp.getWriter().write("Некорректный JSON в теле запроса");
                return;
            }

            // Проверяем, что в JSON есть поле id
            if (client.getId() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/plain; charset=UTF-8");
                resp.getWriter().write("В теле запроса отсутствует поле id");
                return;
            }

            // Проверяем, что id клиента из тела совпадает с userId из сессии — иначе запрещаем обновлять чужие данные
            if (!userId.equals(client.getId())) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.setContentType("text/plain; charset=UTF-8");
                resp.getWriter().write("Доступ запрещён: можно обновлять только свои данные");
                return;
            }

            // Если проверки пройдены, передаём дальше обёрнутый запрос с возможностью повторного чтения тела
            chain.doFilter(cachedReq, response);
            return;
        }

        // Для всех остальных запросов, если аутентификация пройдена, пропускаем дальше
        chain.doFilter(request, response);
    }
}