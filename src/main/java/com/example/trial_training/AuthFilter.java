package com.example.trial_training;

import com.example.trial_training.exception.AuthenticationException;
import com.example.trial_training.model.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private Integer id;
    private final ObjectMapper objectMapper;

    public AuthFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;


        String path = req.getRequestURI();
        String method = req.getMethod();



        if("POST".equals(method) && ("/clients".equals(path) || "/trainers".equals(path))) {
            chain.doFilter(request, response);
            return;
        }

        if (EXCLUDED_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("Требуется аутентификация");
            return;
        }
//получаем id из сессии
        Object userIdObj = session.getAttribute("userId");
        if (!((userIdObj) instanceof Integer)) {
            throw new AuthenticationException("Некоректныее данные сессии");
        }
//в переменную Integer устанавливаем userIdObj
        Integer userId = (Integer) userIdObj;
 //сосдаю переменную id
        Integer id = null;
        if (path.startsWith("/clients/")) {
            String idStr = path.substring("/clients/".length());
            id = Integer.parseInt(idStr);
        }

        if (id != null && !userId.equals(id)) {
            throw new AuthenticationException("Некоректныее данные сессии");
        }

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
                resp.getWriter().write("В теле запроса отсутствует поле id");
                return;
            }

            if (!userId.equals(client.getId())) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.setContentType("text/plain; charset=UTF-8");
                resp.getWriter().write("Доступ запрещён: можно обновлять только свои данные");
                return;
            }

            // Передаём дальше обёрнутый запрос
            chain.doFilter(cachedReq, response);
            return;
        }


        chain.doFilter(request, response);
    }
}
