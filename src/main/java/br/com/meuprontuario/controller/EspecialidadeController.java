package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Especialidade;
import br.com.meuprontuario.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping
    public String listar(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        List<Especialidade> especialidades = especialidadeService.listarPorPagina(page, pageSize);
        int totalEspecialidades = especialidadeService.contarEspecialidades();
        int totalPages = (int) Math.ceil((double) totalEspecialidades / pageSize);

        model.addAttribute("especialidades", especialidades);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "especialidade-lista";
    }

    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Long id, Model model) {
        Especialidade especialidade = id != null ? especialidadeService.buscarPorId(id) : new Especialidade();
        model.addAttribute("especialidade", especialidade);
        return "especialidade-formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Especialidade especialidade) {
        especialidadeService.salvar(especialidade);
        return "redirect:/especialidades";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        especialidadeService.excluir(id);
        return "redirect:/especialidades";
    }
}
