package br.com.meuprontuario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.meuprontuario.model.UsuarioHospital;
import br.com.meuprontuario.model.UsuarioPaciente;
import jakarta.servlet.http.HttpSession;


    @Controller
    public class IndexController {
        @GetMapping("/")
        public String exibirIndex(HttpSession session) {
            Object usuarioLogado = session.getAttribute("usuarioLogado");
            if (usuarioLogado != null) {
                if (usuarioLogado instanceof UsuarioPaciente) {
                    return "redirect:/pacientes";
                } else if (usuarioLogado instanceof UsuarioHospital) {
                    return "redirect:/hospitais";
                }
            }
            return "index"; // Retorna a página inicial se ninguém estiver logado
        }
    }


