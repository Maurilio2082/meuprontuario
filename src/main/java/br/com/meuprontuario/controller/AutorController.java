package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Autor;
import br.com.meuprontuario.service.AutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    public String listar(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 10; // Paginação, 10 itens por página
        List<Autor> autores = autorService.listarPorPagina(page, pageSize);
        int totalAutores = autorService.contarAutores();
        int totalPages = (int) Math.ceil((double) totalAutores / pageSize);

        model.addAttribute("autores", autores);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "autores"; // View para renderizar a listagem
    }

    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Long id, Model model) {
        Autor autor = id != null ? autorService.buscarPorId(id) : new Autor();
        model.addAttribute("autor", autor);
        return "autorformulario"; // View para o formulário
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Autor autor) {
        autorService.salvar(autor);
        return "redirect:/autores";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        autorService.excluir(id);
        return "redirect:/autores";
    }
}
