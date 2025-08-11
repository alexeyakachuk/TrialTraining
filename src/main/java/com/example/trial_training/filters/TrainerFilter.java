package com.example.trial_training.filters;


import com.example.trial_training.CachedBodyHttpServletRequest;
import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.model.trainer.Trainer;
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
public class TrainerFilter implements Filter {

    private static final List<String> EXCLUDED_PATHS = List.of("/auth/login", "/auth/logout");
    private final ObjectMapper objectMapper;
    private final TrainerService trainerService;

    public TrainerFilter(ObjectMapper objectMapper, TrainerService trainerService) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.trainerService = trainerService;
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


            if ("GET".equals(method) && path.startsWith("/trainers")) {
                chain.doFilter(request, response);
                return;
            }

            //получаю Id пользователя из сессии
            Integer sessionUserId = AuthFilters.getSessionUserId(req);

            // 2. Фильтр для получения всех тренеровок определенного тренера
            //добавить коментарии и проверить имена переменных
            if ("GET".equals(method) && path.matches("^/trainers/\\d+/workouts$")) {

                //разбиваем строку по слешу
                String[] parts = path.split("/");
                Integer id = null;
                // проверяем длину строки(URL)
                if (parts.length > 2) {
                    //получаю id тренера из URL
                    String idStr = parts[2];
                    try {
                        id = Integer.parseInt(idStr);
                    } catch (NumberFormatException e) {
                        throw new AuthenticationException("Некоректные данные сессии");
                    }
                }

                if (!sessionUserId.equals(id)) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Доступ запрещён: пройдите аунтефикацию");
                    return;
                }
                chain.doFilter(request, response);
                return;

            }

            // 3. Фильтр для получения тренира по id или всех тренеров без аунтефикации.
            if ("GET".equals(method) && path.startsWith("/trainers")) {
                chain.doFilter(request, response);
                return;
            }

            // 4. Фильтр для создания тренера
            if ("POST".equals(method) && path.startsWith("/trainers")) {
                chain.doFilter(request, response);
                return;
            }

            // 5. Фильтр для обновления тренера
            if ("PUT".equals(method) && path.startsWith("/trainers")) {
                //Оборачиваем request в класс обертку
                CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);

                Trainer trainer;
                //парсим json в сущьность Trainer
                try {
                    trainer = objectMapper.readValue(cachedReq.getInputStream(), Trainer.class);
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Некорректный JSON в теле запроса");
                    return;
                }

                //Проверяем наличие поля id
                if (trainer.getId() == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("В теле запроса отсутствует поле тренера id");
                    return;
                }

                //проверяем совподает ли поле id тренера с id сессии
                if (!sessionUserId.equals(trainer.getId())) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Доступ запрещён: можно обновлять только свои данные");
                    return;
                }

                chain.doFilter(cachedReq, response);
            }

            // 6. Фильтр для удаления тренера
            if ("DELETE".equals(method) && path.startsWith("/trainers")) {
                chain.doFilter(request, response);
                return;
            }

            //-----------------------------------------------------------------------------------

            //Фильтры для контролеров workout

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
                if (!sessionUserId.equals(workout.getTrainerId())) {
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
