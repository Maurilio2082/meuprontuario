package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.TabelaTiss;
import br.com.meuprontuario.service.TabelaTissService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por gerenciar operações relacionadas à Tabela
 * TISS.
 */
@RestController
@RequestMapping("/tabelaTiss")
public class TabelaTissController {

    @Autowired
    private TabelaTissService tabelaTissService; // Serviço para lógica de negócios da Tabela TISS

    /**
     * Lista itens da Tabela TISS com suporte a busca por termo e paginação.
     *
     * @param termo Termo de busca para filtrar os itens (opcional).
     * @param page  Número da página para paginação (default: 1).
     * @param size  Tamanho da página para paginação (default: 10).
     * @return Lista de itens da Tabela TISS filtrados e paginados.
     */
    @GetMapping("/listar")
    public List<TabelaTiss> listarTiss(
            @RequestParam(value = "q", required = false, defaultValue = "") String termo,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        // Chama o serviço para listar os itens da Tabela TISS com base nos parâmetros
        // fornecidos
        return tabelaTissService.listarPorTermo(termo, page, size);
    }
}
