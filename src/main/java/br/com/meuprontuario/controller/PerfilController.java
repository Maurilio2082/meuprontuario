package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PerfilController {

    @GetMapping("/perfil")
    public String exibirPerfil(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login"; // Redireciona para login se não estiver logado
        }
        model.addAttribute("usuario", usuarioLogado);
        return "perfil";
    }

    @PostMapping("/perfil/salvar")
    public String salvarPerfil(Usuario usuario, HttpSession session) {
        session.setAttribute("usuarioLogado", usuario); // Atualiza os dados na sessão
        // Você pode salvar os dados no banco de dados aqui, se necessário.
        return "redirect:/perfil"; // Redireciona para a página de perfil após salvar
    }
}
