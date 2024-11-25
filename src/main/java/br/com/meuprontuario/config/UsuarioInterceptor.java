package br.com.meuprontuario.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class UsuarioInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession(false); // Evitar criar uma nova sessão
        if (session != null && modelAndView != null) {
            Object usuarioLogado = session.getAttribute("usuarioLogado");
            modelAndView.addObject("usuarioLogado", usuarioLogado);
        }
    }
}
