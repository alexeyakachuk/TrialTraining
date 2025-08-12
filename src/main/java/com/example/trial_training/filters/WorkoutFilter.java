package com.example.trial_training.filters;

import com.example.trial_training.CachedBodyHttpServletRequest;
import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.workout.Workout;
import com.example.trial_training.service.trainer.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class WorkoutFilter implements Filter {
    private static final List<String> EXCLUDED_PATHS = List.of("/auth/login", "/auth/logout");
    private final ObjectMapper objectMapper;

    public WorkoutFilter(ObjectMapper objectMapper, TrainerService trainerService) {
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

        try {
            // 1. Если путь входит в список исключений (например, вход/выход)
            if (EXCLUDED_PATHS.contains(path)) {
                chain.doFilter(request, response);
                return;
            }

            //получаю Id пользователя из сессии
            Integer sessionUserId = AuthFilters.getSessionUserId(req);

            // 1. Фильт для создания тренеровки
            if ("POST".equals(method) && path.startsWith("/workout")) {
                chain.doFilter(request, response);
                return;
            }

            // 2. Получение тренеровки по id
            if ("GET".equals(method) && path.startsWith("/workout")) {
                CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);
                Workout workout;
                // Парсим json в сущьность workout
                try {
                    workout = objectMapper.readValue(cachedReq.getInputStream(), Workout.class);
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Некорректный JSON в теле запроса");
                    return;
                }
                // проверяем что id сессии совподает с id переданным в workout через json
                if (!sessionUserId.equals(workout.getClientId())) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Доступ запрещён: пройдите аунтефикацию");
                    return;
                }


                chain.doFilter(cachedReq, response);
                return;
            }
            // 3. Фильтр для удаления тренировки
            if ("DELETE".equals(method) && path.startsWith("/workout")) {
                chain.doFilter(request, response);
                return;
            }


            //Для всех остальных запросов, если аутентификация пройдена, пропускаем дальше
            chain.doFilter(request, response);

        } catch (
                AuthenticationException e) {
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
