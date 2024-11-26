package br.com.meuprontuario.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class UsuarioInterceptor implements HandlerInterceptor {

    /**
     * Método executado após a execução do controlador, mas antes de enviar
     * a resposta para o cliente. Permite modificar o ModelAndView.
     * 
     * @param request       a requisição HTTP recebida
     * @param response      a resposta HTTP que será enviada
     * @param handler       o manipulador que processou a requisição
     * @param modelAndView  o objeto que contém os dados e a visão retornada pelo controlador
     * @throws Exception    caso ocorra algum erro no processo
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        
        // Obtém a sessão atual, se existir (false indica que não cria uma nova sessão)
        HttpSession session = request.getSession(false);

        // Verifica se a sessão e o objeto ModelAndView não são nulos
        if (session != null && modelAndView != null) {
            // Recupera o atributo "usuarioLogado" da sessão
            Object usuarioLogado = session.getAttribute("usuarioLogado");

            // Adiciona o "usuarioLogado" ao ModelAndView, tornando-o disponível na visão
            modelAndView.addObject("usuarioLogado", usuarioLogado);
        }
    }
}
