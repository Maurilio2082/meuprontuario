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
    public String listar(Model model) {
        List<Livro> livros = livroService.listarTodos();
        model.addAttribute("livros", livros);
        return "livros"; // Deve haver um arquivo "livros.html" em templates
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
