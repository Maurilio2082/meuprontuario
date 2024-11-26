package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Usuario;
import br.com.meuprontuario.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelas operações de login e logout.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * Exibe a página de login.
     *
     * @return Nome do template da página de login.
     */
    @GetMapping
    public String exibirFormularioLogin() {
        return "login"; // Página de login (Thymeleaf ou outra tecnologia)
    }

    /**
     * Processa a autenticação do usuário.
     *
     * @param username Login do usuário.
     * @param senha    Senha do usuário.
     * @param session  Sessão HTTP para armazenar o usuário logado.
     * @param model    Modelo para passar informações à página.
     * @return Redirecionamento para a página inicial ou para a página de login em caso de erro.
     */
    @PostMapping
    public String autenticar(@RequestParam String username, @RequestParam String senha, HttpSession session,
                             Model model) {
        Usuario usuario = loginService.autenticar(username, senha);
        if (usuario != null) {
            session.setAttribute("usuarioLogado", usuario); // Armazena o usuário logado na sessão
            return usuario.getRedirecionamento(); // Redireciona para a página correspondente
        } else {
            model.addAttribute("erro", "Usuário ou senha inválidos");
            return "login";
        }
    }

    /**
     * Realiza o logout do usuário.
     *
     * @param session Sessão HTTP a ser invalidada.
     * @return Redirecionamento para a página inicial.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate(); // Invalida a sessão
        }
        return "redirect:/"; // Redireciona para o index
    }
}
