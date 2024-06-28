package com.project.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/");
            return false;
        } else {
            User login = (User) request.getSession().getAttribute("user");
            if (!login.getProfil().equals("admin")) {
                response.sendRedirect("/error403");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && request.getSession().getAttribute("user") != null) {
            modelAndView.addObject("user", request.getSession().getAttribute("user"));
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // Code après le traitement complet si nécessaire
    }
}
