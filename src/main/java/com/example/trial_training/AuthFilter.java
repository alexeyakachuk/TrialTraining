package com.example.trial_training;

import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.model.workout.Workout;
import com.example.trial_training.service.trainer.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//@Component
public class AuthFilter implements Filter {

    private static final List<String> EXCLUDED_PATHS = List.of("/auth/login", "/auth/logout");
    private final ObjectMapper objectMapper;
    private final TrainerService trainerService;

    public AuthFilter(ObjectMapper objectMapper, TrainerService trainerService) {
        this.trainerService = trainerService;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        try {
            String path = req.getRequestURI();
            String method = req.getMethod();

            // 1. Если путь входит в список исключений (например, вход/выход)
            if (EXCLUDED_PATHS.contains(path)) {
                chain.doFilter(request, response);
                return;
            }

            // 2. Разрешаем POST на регистрацию без аутентификации
            if ("POST".equals(method) && ("/clients".equals(path) || "/trainers".equals(path))) {
                chain.doFilter(request, response);
                return;
            }

            // 3. Разрешаем GET /trainers и GET /trainers/{pathUserId} без аутентификации
            if ("GET".equals(method) && path.startsWith("/trainers")) {
                chain.doFilter(request, response);
                return;
            }

            // 4. Получаем сессию без создания новой, если её нет - значит пользователь не авторизован
            HttpSession session = req.getSession(false);

            // 5. Если сессия отсутствует или в ней нет атрибута username — возвращаем 401 (требуется аутентификация)
            if (session == null || session.getAttribute("username") == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("text/plain; charset=UTF-8");
                resp.getWriter().write("Требуется аутентификация");
                return;
            }

            // 6. Получаем sessionUserId из сессии и проверяем что это Integer (иначе бросаем исключение)
            Object userIdObj = session.getAttribute("userId");
            if (!(userIdObj instanceof Integer)) {
                throw new AuthenticationException("Некоректныее данные сессии");
            }
            Integer sessionUserId = (Integer) userIdObj;

            // 7. Если URL начинается с /clients/, пытаемся вытащить pathUserId клиента из URL
            // переделать через регулярное выражение
            if (path.startsWith("/clients/")) {
                String idStr = path.substring("/clients/".length());
                int pathUserId;
                try {
                    pathUserId = Integer.parseInt(idStr);
                } catch (NumberFormatException e) {
                    throw new AuthenticationException("Некоректныее данные сессии");
                }
                if (!sessionUserId.equals(pathUserId)) {
                    throw new AuthenticationException("Некоректныее данные сессии");
                }
            }

            // 8. Если это POST-запрос на создание тренировки("/workout"), проверяем тело и соответствие clientId
            if ("POST".equals(method) && "/workout".equals(path)) {
                CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);
                Workout workout;
                try {
                    workout = objectMapper.readValue(cachedReq.getInputStream(), Workout.class);
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Некорректный JSON в теле запроса");
                    return;
                }
                if (!(workout.getClientId().equals(sessionUserId))) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Не возможно записатся на тренировку. " +
                            "Id клиента не совподает с pathUserId сессии.");
                    return;
                }
                chain.doFilter(cachedReq, response);
                return;
            }

            // 9. Если это PUT-запрос на обновление клиента ("/clients"), проверяем тело запроса
            if ("PUT".equals(method) && "/clients".equals(path)) {
                CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);
                Client client;
                try {
                    client = objectMapper.readValue(cachedReq.getInputStream(), Client.class);
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Некорректный JSON в теле запроса");
                    return;
                }
                if (client.getId() == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("В теле запроса отсутствует поле pathUserId");
                    return;
                }
                if (!sessionUserId.equals(client.getId())) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Доступ запрещён: можно обновлять только свои данные");
                    return;
                }
                chain.doFilter(cachedReq, response);
                return;
            }

            // 10. Если это PUT-запрос на обновление тренера ("/trainers"), проверяем тело запроса
            if ("PUT".equals(method) && "/trainers".equals(path)) {
                CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);
                Trainer trainer;
                try {
                    trainer = objectMapper.readValue(cachedReq.getInputStream(), Trainer.class);
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Некорректный JSON в теле запроса");
                    return;
                }
                if (trainer.getId() == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("В теле запроса отсутствует поле pathUserId");
                    return;
                }
                if (!sessionUserId.equals(trainer.getId())) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Доступ запрещён: можно обновлять только свои данные");
                    return;
                }
                chain.doFilter(cachedReq, response);
                return;
            }

            // 11. Для всех остальных запросов, если аутентификация пройдена, пропускаем дальше
            chain.doFilter(request, response);

        } catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json; charset=UTF-8");
            Map<String, String> error = Map.of(
                    "error", "Произошла ошибка. Пожалуйста, зарегистрируйтесь или пройдите аутентификацию",
                    "message", e.getMessage()
            );
            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }

    }
}