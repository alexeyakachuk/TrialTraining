package com.example.trial_training;

import com.example.trial_training.dto.trainer.TrainerDto;
import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.exception.NotFoundException;
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

@Component
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


            // Разрешаем POST на регистрацию без аутентификации
            if ("POST".equals(method) && ("/clients".equals(path) || "/trainers".equals(path))) {
                chain.doFilter(request, response);
                return;
            }

            // Разрешаем GET /trainers и GET /trainers/{id} без аутентификации
            if ("GET".equals(method) && ("/trainers".equals(path) || path.startsWith("/trainers/"))) {
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

            // Если это POST-запрос на создание тренировки("/workout"), проверяем что id клиента пренадлежит авторезированному пользователю
            // и тренер существует

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
// Если id сессии не совподает с id клиента
                if (!(workout.getClientId().equals(userId))) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Не возможно записатся на тренировку. " +
                            "Id клиента не совподает с id сессии.");
                    return;
                }
//  Если тренера не существует
                try {
                    TrainerDto trainer = trainerService.findTrainer(workout.getTrainerId());
                } catch (NotFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Не возможно записатся на тренировку. " +
                            e.getMessage());
                    return;
                }

                chain.doFilter(cachedReq, response);
                return;
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

            // Если это PUT-запрос на обновление клиента ("/clients"), проверяем тело запроса
            if ("PUT".equals(method) && "/trainers".equals(path)) {
                // Оборачиваем запрос, чтобы можно было прочитать тело несколько раз
                CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);
                Trainer trainer;
                try {
                    // Парсим JSON из тела запроса в объект Client
                    trainer = objectMapper.readValue(cachedReq.getInputStream(), Trainer.class);
                } catch (Exception e) {
                    // Если JSON некорректный — возвращаем 400
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Некорректный JSON в теле запроса");
                    return;
                }

                // Проверяем, что в JSON есть поле id
                if (trainer.getId() == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("В теле запроса отсутствует поле id");
                    return;
                }

                // Проверяем, что id клиента из тела совпадает с userId из сессии — иначе запрещаем обновлять чужие данные
                if (!userId.equals(trainer.getId())) {
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