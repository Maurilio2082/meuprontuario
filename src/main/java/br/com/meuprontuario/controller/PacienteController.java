package br.com.meuprontuario.controller;

import br.com.meuprontuario.dao.PacienteDAO;
import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Paciente;
import br.com.meuprontuario.service.PacienteService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    @GetMapping
    public String listar(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        List<Paciente> pacientes = pacienteService.listarPorPagina(page, pageSize);
        int totalPacientes = pacienteService.contarPacientes();
        int totalPages = (int) Math.ceil((double) totalPacientes / pageSize);

        model.addAttribute("pacientes", pacientes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "paciente-lista"; // Retorna a página correta para listar pacientes
    }

    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Paciente paciente = id != null ? pacienteService.buscarPorId(id)
                : new Paciente(0, "", "", "", "", "", new Endereco());
        model.addAttribute("paciente", paciente);
        return "paciente-formulario";
    }

    @GetMapping("/pacientes/formulario")
    public String exibirFormulario(@RequestParam int id, Model model) {
        Paciente paciente = pacienteDAO.buscarPorId(id);
        model.addAttribute("paciente", paciente);
        return "pacientes/formulario"; // Retorna a view do formulário do paciente
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Paciente paciente, HttpSession session) {
        pacienteService.salvar(paciente);

        Object usuarioLogado = session.getAttribute("usuarioLogado");

        // Verifica se o usuário logado é um paciente
        if (usuarioLogado instanceof br.com.meuprontuario.model.UsuarioPaciente) {
            return "redirect:/pacientes/historico"; // Redireciona o paciente logado para o histórico
        }

        // Para outros tipos de usuários (ex.: hospital)
        return "redirect:/pacientes";
    }

    @GetMapping("/formulario/cancelar")
    public String cancelar(HttpSession session) {
        Object usuarioLogado = session.getAttribute("usuarioLogado");

        // Verifica se o usuário logado é um paciente
        if (usuarioLogado instanceof br.com.meuprontuario.model.UsuarioPaciente) {
            return "redirect:/pacientes/historico"; // Redireciona o paciente logado para o histórico
        }

        // Para outros tipos de usuários (ex.: hospital)
        return "redirect:/pacientes";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        pacienteService.excluir(id);
        return "redirect:/pacientes";
    }
}
