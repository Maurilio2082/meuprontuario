package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.*;
import br.com.meuprontuario.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador responsável pelas operações relacionadas ao histórico médico no
 * sistema.
 */
@Controller
@RequestMapping("/historicos")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadeService especialidadeService;

    @Autowired
    private TabelaTissService tabelaTissService;

    @Autowired
    private CidService cidService;

    /**
     * Lista os históricos médicos com suporte a paginação.
     *
     * @param page  Número da página atual (default: 1).
     * @param size  Quantidade de itens por página (default: 10).
     * @param model Modelo para enviar dados à visão.
     * @return Nome da página de listagem de históricos.
     */
    @GetMapping
    public String listar(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<Historico> historicos = historicoService.listarPorPagina(page, size);
        int totalHistoricos = historicoService.contarHistoricos();
        int totalPages = (int) Math.ceil((double) totalHistoricos / size);

        model.addAttribute("historicos", historicos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "historico-lista";
    }

    /**
     * Exibe o formulário para criação ou edição de um histórico médico.
     *
     * @param id    ID do histórico a ser editado (opcional).
     * @param model Modelo para enviar dados à visão.
     * @return Nome da página do formulário.
     */
    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Historico historico = (id != null) ? historicoService.buscarPorId(id) : new Historico();

        // Inicialização padrão para evitar valores nulos
        if (historico.getIdPaciente() == null) {
            historico.setIdPaciente(new Paciente(0, "", "", "", "", "", null));
        }
        if (historico.getIdHospital() == null) {
            historico.setIdHospital(new Hospital(0, "", "", "", "", "", null, ""));
        }
        if (historico.getIdMedico() == null) {
            historico.setIdMedico(new Medico(0, "", "", null, null, ""));
        }
        if (historico.getIdEspecialidade() == null) {
            historico.setIdEspecialidade(new Especialidade(0, ""));
        }
        if (historico.getCid() == null) {
            historico.setCid(new Cid("", "", ""));
        }
        if (historico.getTabelaTiss() == null) {
            historico.setTabelaTiss(new TabelaTiss(0, ""));
        }

        // Adiciona dados necessários à visão
        model.addAttribute("historico", historico);
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("hospitais", hospitalService.listarTodos());
        model.addAttribute("medicos", medicoService.listarTodos());
        model.addAttribute("especialidades", especialidadeService.listarTodas());
        model.addAttribute("cids", cidService.listarTodos());
        model.addAttribute("tiss", tabelaTissService.listarTodos());

        return "historico-formulario";
    }

    /**
     * Salva ou atualiza um histórico médico no banco de dados.
     *
     * @param historico Objeto do histórico a ser salvo.
     * @return Redirecionamento para a página de listagem de históricos.
     */
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Historico historico) {
        historicoService.salvar(historico);
        return "redirect:/historicos";
    }

    /**
     * Exclui um histórico médico pelo ID.
     *
     * @param id ID do histórico a ser excluído.
     * @return Redirecionamento para a página de listagem de históricos.
     */
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        historicoService.excluir(id);
        return "redirect:/historicos";
    }

    /**
     * Importa históricos médicos a partir de um arquivo XML.
     *
     * @param arquivo            Arquivo XML contendo os históricos.
     * @param redirectAttributes Atributos para mensagens de redirecionamento.
     * @return Redirecionamento para a página do formulário.
     */
    @PostMapping("/importar")
    public String importarArquivoXML(@RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirectAttributes) {
        if (arquivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro",
                    "Nenhum arquivo foi enviado. Por favor, selecione um arquivo XML.");
            return "redirect:/historicos/formulario";
        }

        try {
            // Processa o arquivo e importa os históricos
            List<Historico> historicosImportados = historicoService.importarHistoricosDeXML(arquivo);

            if (historicosImportados.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "O arquivo XML não contém registros válidos.");
            } else {
                historicosImportados.forEach(historicoService::salvar);
                redirectAttributes.addFlashAttribute("mensagem",
                        "Importação realizada com sucesso! Registros importados: " + historicosImportados.size());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar o arquivo: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/historicos/formulario";
    }
}
