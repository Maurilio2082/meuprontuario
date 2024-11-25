package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Historico;
import br.com.meuprontuario.model.UsuarioPaciente;
import br.com.meuprontuario.service.HistoricoPacienteService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HistoricoPacienteController {

    @Autowired
    private HistoricoPacienteService historicoPacienteService;

    @GetMapping("/pacientes/historico")
    public String exibirHistoricoPaciente(HttpSession session, Model model) {
        Object usuarioLogado = session.getAttribute("usuarioLogado");

        if (usuarioLogado instanceof UsuarioPaciente) {
            UsuarioPaciente paciente = (UsuarioPaciente) usuarioLogado;
            List<Historico> historicos = historicoPacienteService.listarPorPaciente(paciente.getIdPaciente());
            model.addAttribute("historicos", historicos);
            return "historico-paciente";
        }

        return "redirect:/login";
    }
}
