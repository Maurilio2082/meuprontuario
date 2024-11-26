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

/**
 * Controlador responsável por gerenciar as operações relacionadas aos pacientes.
 */
@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService; // Serviço para lógica de negócios relacionada aos pacientes

    private final PacienteDAO pacienteDAO = new PacienteDAO(); // Instância do DAO para operações diretas no banco

    /**
     * Lista os pacientes com suporte a paginação.
     *
     * @param page  Número da página atual (default: 1).
     * @param model Modelo para enviar dados à visão.
     * @return Nome da página que lista os pacientes.
     */
    @GetMapping
    public String listar(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 10; // Tamanho da página para paginação
        List<Paciente> pacientes = pacienteService.listarPorPagina(page, pageSize); // Busca os pacientes paginados
        int totalPacientes = pacienteService.contarPacientes(); // Total de pacientes cadastrados
        int totalPages = (int) Math.ceil((double) totalPacientes / pageSize); // Calcula o total de páginas

        model.addAttribute("pacientes", pacientes); // Adiciona a lista de pacientes ao modelo
        model.addAttribute("currentPage", page); // Adiciona a página atual ao modelo
        model.addAttribute("totalPages", totalPages); // Adiciona o total de páginas ao modelo

        return "paciente-lista"; // Retorna a página HTML que exibe a lista de pacientes
    }

    /**
     * Exibe o formulário para criação ou edição de um paciente.
     *
     * @param id    ID do paciente a ser editado (opcional).
     * @param model Modelo para enviar dados à visão.
     * @return Nome da página do formulário do paciente.
     */
    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Integer id, Model model) {
        // Busca o paciente pelo ID ou inicializa um novo paciente com valores padrão
        Paciente paciente = id != null ? pacienteService.buscarPorId(id)
                : new Paciente(0, "", "", "", "", "", new Endereco());
        model.addAttribute("paciente", paciente); // Adiciona o paciente ao modelo
        return "paciente-formulario"; // Retorna a página HTML do formulário
    }

    /**
     * Exibe o formulário de edição usando o DAO.
     *
     * @param id    ID do paciente a ser editado.
     * @param model Modelo para enviar dados à visão.
     * @return Nome da página do formulário do paciente.
     */
    @GetMapping("/pacientes/formulario")
    public String exibirFormulario(@RequestParam int id, Model model) {
        Paciente paciente = pacienteDAO.buscarPorId(id); // Busca o paciente pelo DAO
        model.addAttribute("paciente", paciente); // Adiciona o paciente ao modelo
        return "pacientes/formulario"; // Retorna a página HTML do formulário
    }

    /**
     * Salva ou atualiza um paciente no banco de dados.
     *
     * @param paciente Objeto do paciente a ser salvo.
     * @param session  Sessão HTTP para verificar o tipo de usuário logado.
     * @return Redirecionamento para a página correspondente ao tipo de usuário logado.
     */
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Paciente paciente, HttpSession session) {
        pacienteService.salvar(paciente); // Salva o paciente usando o serviço

        Object usuarioLogado = session.getAttribute("usuarioLogado"); // Recupera o usuário logado

        // Redireciona o paciente logado para sua página de histórico
        if (usuarioLogado instanceof br.com.meuprontuario.model.UsuarioPaciente) {
            return "redirect:/pacientes/historico";
        }

        // Redireciona outros usuários (ex.: hospital) para a listagem de pacientes
        return "redirect:/pacientes";
    }

    /**
     * Cancela a operação no formulário e redireciona o usuário.
     *
     * @param session Sessão HTTP para verificar o tipo de usuário logado.
     * @return Redirecionamento para a página correspondente ao tipo de usuário logado.
     */
    @GetMapping("/formulario/cancelar")
    public String cancelar(HttpSession session) {
        Object usuarioLogado = session.getAttribute("usuarioLogado"); // Recupera o usuário logado

        // Redireciona o paciente logado para sua página de histórico
        if (usuarioLogado instanceof br.com.meuprontuario.model.UsuarioPaciente) {
            return "redirect:/pacientes/historico";
        }

        // Redireciona outros usuários para a listagem de pacientes
        return "redirect:/pacientes";
    }

    /**
     * Exclui um paciente pelo ID.
     *
     * @param id ID do paciente a ser excluído.
     * @return Redirecionamento para a lista de pacientes.
     */
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        pacienteService.excluir(id); // Exclui o paciente pelo ID usando o serviço
        return "redirect:/pacientes"; // Redireciona para a lista de pacientes
    }
}
