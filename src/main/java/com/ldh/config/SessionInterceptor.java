package com.ldh.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    // 이 메서드는 핸들러 메서드가 실행된 후에 호출됩니다.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {

            // 현재 HTTP 세션을 가져옵니다.
            HttpSession session = request.getSession();

            // 세션 속성을 가져옵니다.
            String name = (String) session.getAttribute("name");
            String email = (String) session.getAttribute("email");
            String accessToken = (String) session.getAttribute("accessToken");
            String refreshToken = (String) session.getAttribute("refreshToken");
            String idToken = (String) session.getAttribute("idToken");
            String roles = (String) session.getAttribute("roles");

            // 모델에 세션 속성을 추가합니다.
            modelAndView.addObject("name", name);
            modelAndView.addObject("email", email);
            modelAndView.addObject("accessToken", accessToken);
            modelAndView.addObject("refreshToken", refreshToken);
            modelAndView.addObject("idToken", idToken);
            modelAndView.addObject("roles", roles);
        }
    }
}
