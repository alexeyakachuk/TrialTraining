package com.example.trial_training;

import com.example.trial_training.exception.AuthenticationException;
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

//        Object userId = session.getAttribute("userId");
//        if (!((userId) instanceof Integer)) {
//            throw new AuthenticationException("Некоректныее данные сессии");
//        }

//        Integer id = (Integer) userId;

        if (path.startsWith("/clients")) {
            String idStr = path.substring("/clients/".length());
            id = Integer.valueOf(idStr);
//
        }

        Object userId = session.getAttribute("userId");
        if (userId == null || !userId.equals(id)) {
            throw new AuthenticationException("Некоректныее данные сессии");
        }

        chain.doFilter(request, response);

    }
}
