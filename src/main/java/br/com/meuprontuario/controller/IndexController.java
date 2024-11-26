package br.com.meuprontuario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.meuprontuario.model.UsuarioHospital;
import br.com.meuprontuario.model.UsuarioPaciente;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador responsável por gerenciar a página inicial do sistema.
 */
@Controller
public class IndexController {

    /**
     * Redireciona o usuário logado para sua página principal correspondente ou
     * exibe a página inicial se não houver nenhum usuário autenticado.
     *
     * @param session Sessão HTTP para verificar o usuário logado.
     * @return Redirecionamento para a página do tipo de usuário logado ou a página
     *         inicial.
     */
    @GetMapping("/")
    public String exibirIndex(HttpSession session) {
        // Recupera o usuário logado da sessão
        Object usuarioLogado = session.getAttribute("usuarioLogado");

        if (usuarioLogado != null) {
            // Redireciona o usuário para a página correspondente
            if (usuarioLogado instanceof UsuarioPaciente) {
                return "redirect:/pacientes";
            } else if (usuarioLogado instanceof UsuarioHospital) {
                return "redirect:/hospitais";
            }
        }

        // Retorna a página inicial se nenhum usuário estiver logado
        return "index";
    }
}
