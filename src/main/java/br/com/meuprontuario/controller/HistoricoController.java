package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Especialidade;
import br.com.meuprontuario.model.Historico;
import br.com.meuprontuario.model.Hospital;
import br.com.meuprontuario.model.Medico;
import br.com.meuprontuario.model.Paciente;
import br.com.meuprontuario.service.EspecialidadeService;
import br.com.meuprontuario.service.HistoricoService;
import br.com.meuprontuario.service.HospitalService;
import br.com.meuprontuario.service.MedicoService;
import br.com.meuprontuario.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Historico historico = (id != null) ? historicoService.buscarPorId(id) : new Historico();

        // Certifique-se de inicializar corretamente os objetos relacionados
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

        model.addAttribute("historico", historico);
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("hospitais", hospitalService.listarTodos());
        model.addAttribute("medicos", medicoService.listarTodos());
        model.addAttribute("especialidades", especialidadeService.listarTodas());

        return "historico-formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Historico historico) {
        historicoService.salvar(historico);
        return "redirect:/historicos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        historicoService.excluir(id);
        return "redirect:/historicos";
    }
}
