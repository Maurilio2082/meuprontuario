package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Especialidade;
import br.com.meuprontuario.model.Hospital;
import br.com.meuprontuario.model.Medico;
import br.com.meuprontuario.service.EspecialidadeService;
import br.com.meuprontuario.service.HospitalService;
import br.com.meuprontuario.service.MedicoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos médicos.
 */
@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadeService especialidadeService;

    @Autowired
    private HospitalService hospitalService;

    /**
     * Lista os médicos com suporte a paginação.
     *
     * @param page     Número da página atual.
     * @param pageSize Tamanho da página (número de itens por página).
     * @param model    Modelo para enviar dados à visão.
     * @return Nome da página que lista os médicos.
     */
    @GetMapping
    public String listar(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            Model model) {

        List<Medico> medicos = medicoService.listarPorPagina(page, pageSize);
        int totalMedicos = medicoService.contarMedicos();
        int totalPages = (int) Math.ceil((double) totalMedicos / pageSize);

        model.addAttribute("medicos", medicos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "medico-lista";
    }

    /**
     * Exibe o formulário para criação ou edição de um médico.
     *
     * @param id    ID do médico a ser editado (opcional).
     * @param model Modelo para enviar dados à visão.
     * @return Nome da página do formulário do médico.
     */
    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Medico medico = id != null ? medicoService.buscarPorId(id) : new Medico();
        List<Especialidade> especialidades = especialidadeService.listarTodas();
        List<Hospital> hospitais = hospitalService.listarTodos();

        model.addAttribute("medico", medico);
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("hospitais", hospitais);

        return "medico-formulario";
    }

    /**
     * Salva ou atualiza um médico no banco de dados.
     *
     * @param medico Objeto médico a ser salvo.
     * @return Redirecionamento para a lista de médicos.
     */
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Medico medico) {
        medicoService.salvar(medico);
        return "redirect:/medicos";
    }

    /**
     * Exclui um médico pelo ID.
     *
     * @param id ID do médico a ser excluído.
     * @return Redirecionamento para a lista de médicos.
     */
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        medicoService.excluir(id);
        return "redirect:/medicos";
    }

    /**
     * Lista os médicos de um hospital específico.
     *
     * @param hospitalId ID do hospital.
     * @return Lista de médicos associados ao hospital.
     */
    @GetMapping("/listarPorHospital")
    public @ResponseBody List<Medico> listarPorHospital(@RequestParam("hospitalId") int hospitalId) {
        return medicoService.listarPorHospital(hospitalId);
    }
}
