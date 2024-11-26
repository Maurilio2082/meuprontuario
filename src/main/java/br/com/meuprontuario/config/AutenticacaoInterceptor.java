package br.com.meuprontuario.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.meuprontuario.model.UsuarioHospital;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Interceptador responsável por validar a autenticação e autorização dos
 * usuários antes de
 * permitir o acesso às rotas protegidas.
 */
@Component
public class AutenticacaoInterceptor implements HandlerInterceptor {

    /**
     * Método executado antes de o controlador processar a requisição.
     * 
     * @param request  A requisição HTTP recebida.
     * @param response A resposta HTTP que será enviada.
     * @param handler  O manipulador que processará a requisição.
     * @return `true` se a requisição for autorizada, `false` caso contrário.
     * @throws Exception Caso ocorra algum erro no processo.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // Obtém a sessão atual, se existir (false para não criar uma nova sessão)
        HttpSession session = request.getSession(false);

        // Recupera o usuário logado da sessão, se existir
        Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;

        // Redireciona para a página de login se o usuário não estiver logado
        if (usuarioLogado == null) {
            response.sendRedirect("/login");
            return false; // Bloqueia o acesso
        }

        // Verifica se a URL acessada começa com "/pacientes" e se o usuário não é do
        // tipo "UsuarioHospital"
        if (request.getRequestURI().startsWith("/pacientes") && !(usuarioLogado instanceof UsuarioHospital)) {
            response.sendRedirect("/login");
            return false; // Bloqueia o acesso
        }

        // Continua com a requesicao
        return true;
    }
}
