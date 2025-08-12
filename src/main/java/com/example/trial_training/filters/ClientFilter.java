package com.example.trial_training.filters;

import com.example.trial_training.CachedBodyHttpServletRequest;
import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.workout.Workout;
import com.example.trial_training.service.client.ClientService;
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
public class ClientFilter implements Filter {

    private static final List<String> EXCLUDED_PATHS = List.of("/auth/login", "/auth/logout");
    private final ObjectMapper objectMapper;
    private final ClientService clientService;

    public ClientFilter(ObjectMapper objectMapper, ClientService clientService)
            throws IOException, ServletException {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.clientService = clientService;
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

//            if (!path.startsWith("/clients")) {
//                chain.doFilter(request, response);
//                return;
//            }

            //получаю Id пользователя из сессии
            Integer sessionUserId = AuthFilters.getSessionUserId(req);

            // 2. Фильтр для получения клиента по id.
            if ("GET".equals(method) && path.matches("^/clients/\\d+$")) {
                //разбиваем строку по слешу
                String[] parts = path.split("/");
                Integer id = null;

                // проверяем длину строки(URL)
                if (parts.length > 2) {
                    String idStr = parts[2];
                    try {
                        id = Integer.parseInt(idStr);
                    } catch (NumberFormatException e) {
                        throw new AuthenticationException("Некоруктные данные сессии");
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

            // 3. Фильтр для удоления клиента
            if ("DELETE".equals(method) && path.startsWith("/clients")) {
                chain.doFilter(request, response);
                return;
            }

            // 4. Фильтр для обновленя клиента
            if ("PUT".equals(method) && path.startsWith("/clients")) {
                // оборачиваем request в обертку
                CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);

                Client client;
                // Парсим json в сущьность client
                try {
                    client = objectMapper.readValue(cachedReq.getInputStream(), Client.class);
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Некорректный JSON в теле запроса");
                    return;
                }

                // проверяем наличие поля id
                if (client.getId() == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("В теле запроса отсутствует поле тренера id");
                    return;
                }

                // проверяем совподает ли поле id с id сессии
                if (!sessionUserId.equals(client.getId())) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("Доступ запрещён: можно обновлять только свои данные");
                    return;
                }

                chain.doFilter(cachedReq, response);
                return;

            }

            // 5. Фильтр для получения всех тренировок клиента
            if ("GET".equals(method) && path.matches("^/clients/\\d+/workouts$")) {

                // разбиваем строку по слешу
                String[] parts = path.split("/");
                Integer id = null;

                // проверяем длину строки
                if (parts.length > 2) {
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

            // 6. Фильтр для создания коиента
            if ("POST".equals(method) && path.startsWith("/clients")) {
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
