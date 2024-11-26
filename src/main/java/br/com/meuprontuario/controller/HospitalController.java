package br.com.meuprontuario.controller;

import br.com.meuprontuario.dao.HospitalDAO;
import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Hospital;
import br.com.meuprontuario.service.HospitalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos
 * hospitais.
 */
@Controller
@RequestMapping("/hospitais")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService; // Serviço para lógica de negócios relacionada aos hospitais

    private final HospitalDAO hospitalDAO = new HospitalDAO(); // Instância do DAO para operações diretas no banco

    /**
     * Lista os hospitais com suporte a paginação.
     *
     * @param page  Número da página atual (default: 1).
     * @param model Modelo para enviar dados à visão.
     * @return Nome do template HTML que exibe a lista de hospitais.
     */
    @GetMapping
    public String listar(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 10; // Número de itens por página
        List<Hospital> hospitais = hospitalService.listarPorPagina(page, pageSize); // Busca hospitais paginados
        int totalHospitais = hospitalService.contarHospitais(); // Total de hospitais no banco
        int totalPages = (int) Math.ceil((double) totalHospitais / pageSize); // Calcula o total de páginas

        // Adiciona os dados ao modelo para renderização na visão
        model.addAttribute("hospitais", hospitais);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "hospital-lista"; // Nome do template HTML correspondente
    }

    /**
     * Exibe o formulário para criação ou edição de um hospital.
     *
     * @param id    ID do hospital a ser editado (opcional).
     * @param model Modelo para enviar dados à visão.
     * @return Nome do template HTML do formulário.
     */
    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Integer id, Model model) {
        // Busca o hospital pelo ID ou inicializa um novo hospital com valores padrão
        Hospital hospital = (id != null)
                ? hospitalService.buscarPorId(id)
                : new Hospital(0, "", "", "", "", "", new Endereco(), "");

        model.addAttribute("hospital", hospital); // Adiciona o hospital ao modelo
        return "hospital-formulario"; // Retorna o template HTML do formulário
    }

    /**
     * Exibe o formulário de hospital a partir do DAO.
     *
     * @param id    ID do hospital.
     * @param model Modelo para enviar dados à visão.
     * @return Nome da view do formulário do hospital.
     */
    @GetMapping("/hospitais/formulario")
    public String exibirFormulario(@RequestParam int id, Model model) {
        Hospital hospital = hospitalDAO.buscarPorId(id); // Busca o hospital pelo DAO
        model.addAttribute("hospital", hospital); // Adiciona o hospital ao modelo
        return "hospitais/formulario"; // Retorna o nome da view correspondente
    }

    /**
     * Salva ou atualiza um hospital no banco de dados.
     *
     * @param hospital Objeto do hospital a ser salvo.
     * @return Redirecionamento para a listagem de hospitais.
     */
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Hospital hospital) {
        hospitalService.salvar(hospital); // Salva o hospital usando o serviço
        return "redirect:/hospitais"; // Redireciona para a lista de hospitais
    }

    /**
     * Exclui um hospital pelo ID.
     *
     * @param id ID do hospital a ser excluído.
     * @return Redirecionamento para a listagem de hospitais.
     */
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        hospitalService.excluir(id); // Exclui o hospital pelo ID usando o serviço
        return "redirect:/hospitais"; // Redireciona para a lista de hospitais
    }
}
