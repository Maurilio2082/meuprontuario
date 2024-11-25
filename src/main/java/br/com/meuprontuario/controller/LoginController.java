package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Usuario;
import br.com.meuprontuario.service.LoginService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public String exibirFormularioLogin() {
        return "login"; // Página de login (Thymeleaf ou outra tecnologia)
    }

    @PostMapping
    public String autenticar(@RequestParam String username, @RequestParam String senha, HttpSession session,
            Model model) {
        Usuario usuario = loginService.autenticar(username, senha);
        if (usuario != null) {
            session.setAttribute("usuarioLogado", usuario); // Adiciona o usuário logado à sessão
            return usuario.getRedirectPage(); // Redireciona para a página correspondente
        } else {
            model.addAttribute("erro", "Usuário ou senha inválidos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            System.out.println("Invalidando a sessão: " + session.getId());
            session.invalidate(); // Invalida a sessão
        }
        return "redirect:/"; // Redireciona para o index
    }

}
