package br.com.meuprontuario.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.meuprontuario.model.UsuarioHospital;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AutenticacaoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;

        // Verifica se o usuário está logado
        if (usuarioLogado == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Permite o acesso a "/pacientes" somente para hospitais
        if (request.getRequestURI().startsWith("/pacientes") && !(usuarioLogado instanceof UsuarioHospital)) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
