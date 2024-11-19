package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Autor;
import br.com.meuprontuario.model.Livro;
import br.com.meuprontuario.service.AutorService;
import br.com.meuprontuario.service.LivroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @Autowired
    private AutorService autorService;

    @GetMapping
    public String listar(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 10; // Tamanho da página, 10 por padrão
        List<Livro> livros = livroService.listarPorPagina(page, pageSize);
        int totalLivros = livroService.contarLivros();
        int totalPages = (int) Math.ceil((double) totalLivros / pageSize); // Total de páginas

        model.addAttribute("livros", livros);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "livros"; // View onde você renderiza os livros
    }

    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Long id, Model model) {
        Livro livro = id != null ? livroService.buscarPorId(id) : new Livro();
        List<Autor> autores = autorService.obterListaAutores();

        model.addAttribute("livro", livro);
        model.addAttribute("autores", autores); // Lista suspensa de autores
        return "livroformulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Livro livro) {
        livroService.salvar(livro);
        return "redirect:/livros";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        livroService.excluir(id);
        return "redirect:/livros";
    }
}
