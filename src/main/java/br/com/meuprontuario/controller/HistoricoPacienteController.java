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

/**
 * Controlador responsável por exibir o histórico médico de pacientes logados.
 */
@Controller
public class HistoricoPacienteController {

    @Autowired
    private HistoricoPacienteService historicoPacienteService; // Serviço para lidar com lógica de negócios do histórico
                                                               // do paciente

    /**
     * Exibe o histórico médico de um paciente logado.
     *
     * @param session Sessão HTTP atual para recuperar o usuário logado.
     * @param model   Modelo para enviar dados à visão.
     * @return Nome da página a ser exibida ou redirecionamento para a página de
     *         login caso o usuário não esteja autenticado.
     */
    @GetMapping("/pacientes/historico")
    public String exibirHistoricoPaciente(HttpSession session, Model model) {
        // Recupera o usuário logado da sessão
        Object usuarioLogado = session.getAttribute("usuarioLogado");

        // Verifica se o usuário logado é um paciente
        if (usuarioLogado instanceof UsuarioPaciente) {
            UsuarioPaciente paciente = (UsuarioPaciente) usuarioLogado;

            // Obtém o histórico médico do paciente logado
            List<Historico> historicos = historicoPacienteService.listarPorPaciente(paciente.getIdPaciente());

            // Adiciona o histórico ao modelo para exibição na visão
            model.addAttribute("historicos", historicos);

            // Retorna o nome da página que exibirá o histórico do paciente
            return "historico-paciente";
        }

        // Redireciona para a página de login se o usuário não for um paciente ou não
        // estiver autenticado
        return "redirect:/login";
    }
}
