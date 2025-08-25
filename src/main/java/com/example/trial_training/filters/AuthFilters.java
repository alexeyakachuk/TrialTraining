package com.example.trial_training.filters;

import com.example.trial_training.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AuthFilters {
    public static Integer getSessionUserId(HttpServletRequest request) throws AuthenticationException {
        HttpSession session = request.getSession(false);


        if (session == null || session.getAttribute("username") == null) {
            throw new AuthenticationException("Требуется аунтефикация");
        }

        Object userIdObj = session.getAttribute("userId");

        if (!(userIdObj instanceof Integer)) {
            throw new AuthenticationException("Некорректные данные сессии");
        }

        return (Integer) userIdObj;
    }
}
