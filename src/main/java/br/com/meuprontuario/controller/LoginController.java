package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Usuario;
import br.com.meuprontuario.service.LoginService;
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
    public String autenticar(@RequestParam String username, @RequestParam String senha, Model model) {
        Usuario usuario = loginService.autenticar(username, senha);
        if (usuario != null) {
            return usuario.getRedirectPage(); // Polimorfismo em ação
        } else {
            model.addAttribute("erro", "Usuário ou senha inválidos");
            return "login";
        }
    }
}
