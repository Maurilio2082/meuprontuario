package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Especialidade;
import br.com.meuprontuario.service.EspecialidadeService;
import br.com.meuprontuario.service.MedicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar operações relacionadas às especialidades médicas.
 */
@Controller
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService; // Serviço para lógica de negócios relacionada às especialidades

    @Autowired
    private MedicoService medicoService; // Serviço para lógica relacionada aos médicos

    /**
     * Lista especialidades com suporte a paginação.
     *
     * @param page  Número da página para paginação (default: 1).
     * @param model Objeto para adicionar atributos que serão enviados à visão.
     * @return Nome da página de listagem de especialidades.
     */
    @GetMapping
    public String listar(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 10; // Tamanho da página para paginação
        List<Especialidade> especialidades = especialidadeService.listarPorPagina(page, pageSize); // Lista especialidades paginadas
        int totalEspecialidades = especialidadeService.contarEspecialidades(); // Conta o total de especialidades
        int totalPages = (int) Math.ceil((double) totalEspecialidades / pageSize); // Calcula o número total de páginas

        model.addAttribute("especialidades", especialidades); // Adiciona as especialidades à visão
        model.addAttribute("currentPage", page); // Adiciona a página atual à visão
        model.addAttribute("totalPages", totalPages); // Adiciona o total de páginas à visão

        return "especialidade-lista"; // Nome da página HTML para renderizar
    }

    /**
     * Exibe o formulário para criação ou edição de uma especialidade.
     *
     * @param id    ID da especialidade a ser editada (opcional).
     * @param model Objeto para adicionar atributos que serão enviados à visão.
     * @return Nome da página do formulário.
     */
    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Especialidade especialidade = (id != null) ? especialidadeService.buscarPorId(id) : new Especialidade(0, ""); // Cria ou busca a especialidade
        model.addAttribute("especialidade", especialidade); // Adiciona a especialidade ao modelo
        return "especialidade-formulario"; // Nome da página HTML para renderizar
    }

    /**
     * Salva uma nova especialidade ou atualiza uma existente.
     *
     * @param especialidade Objeto da especialidade a ser salva.
     * @return Redirecionamento para a listagem de especialidades.
     */
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Especialidade especialidade) {
        especialidadeService.salvar(especialidade); // Salva a especialidade no banco
        return "redirect:/especialidades"; // Redireciona para a listagem
    }

    /**
     * Exclui uma especialidade pelo ID.
     *
     * @param id ID da especialidade a ser excluída.
     * @return Redirecionamento para a listagem de especialidades.
     */
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        especialidadeService.excluir(id); // Exclui a especialidade
        return "redirect:/especialidades"; // Redireciona para a listagem
    }

    /**
     * Busca a especialidade de um médico específico.
     *
     * @param medicoId ID do médico.
     * @return Objeto da especialidade encontrada.
     * @throws RuntimeException Se nenhuma especialidade for encontrada.
     */
    @GetMapping("/especialidadePorMedico")
    @ResponseBody
    public Especialidade buscarEspecialidadePorMedico(@RequestParam("medicoId") int medicoId) {
        Especialidade especialidade = medicoService.buscarEspecialidadePorMedico(medicoId); // Busca a especialidade pelo ID do médico
        if (especialidade == null) {
            throw new RuntimeException("Nenhuma especialidade encontrada para o médico ID: " + medicoId); // Exceção caso não encontre
        }
        return especialidade;
    }
}
