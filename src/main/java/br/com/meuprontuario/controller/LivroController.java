package br.com.meuprontuario.controller;

import br.com.meuprontuario.entity.Autor;
import br.com.meuprontuario.entity.Livro;
import br.com.meuprontuario.service.AutorService;
import br.com.meuprontuario.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;

@Controller
public class LivroController {

    @Autowired
    private AutorService autorService;

    @Autowired
    private LivroService livroService;

    @GetMapping("/")
    public ModelAndView obterListaLivros(@RequestParam(defaultValue = "0") int pagina) {
        Page<Livro> listaLivros = this.livroService.encontrarPaginado(pagina, 10); // 10 itens por página
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("listaLivros", listaLivros.getContent());
        mv.addObject("paginaAtual", pagina);
        mv.addObject("totalPaginas", listaLivros.getTotalPages());
        return mv;
    }

    @GetMapping("/livro/form/adicionar")
    public ModelAndView obterFormularioAdicionar() {
        ModelAndView mv = new ModelAndView("livroformulario");
        List<Autor> listaAutores = this.autorService.obterListaAutores();
        mv.addObject("listaAutores", listaAutores);
        return mv;
    }

    @PostMapping("/livro/form/salvar")
    public String salvarLivro(@Valid Livro livro, BindingResult resultado, RedirectAttributes redirect) {
        if (resultado.hasErrors()) {
            redirect.addFlashAttribute("mensagem", "Verifique os campos obrigatórios");
            return "redirect:/livro/form/adicionar";
        }
        this.livroService.salvar(livro);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public ModelAndView obterEdicao(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("livroformulario");
        List<Autor> listaAutores = this.autorService.obterListaAutores();
        mv.addObject("listaAutores", listaAutores);

        Livro livro = this.livroService.encontrarPorId(id);
        mv.addObject("livro", livro);
        return mv;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        this.livroService.delete(id);
        return "redirect:/";
    }

}
