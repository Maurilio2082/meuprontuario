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

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadeService especialidadeService;

    @Autowired
    private HospitalService hospitalService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("medicos", medicoService.listarTodos());
        return "medico-lista";
    }

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

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Medico medico) {
        medicoService.salvar(medico);
        return "redirect:/medicos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        medicoService.excluir(id);
        return "redirect:/medicos";
    }
}
