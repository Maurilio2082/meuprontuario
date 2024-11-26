package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Cid;
import br.com.meuprontuario.service.CidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por gerenciar operações relacionadas aos CIDs
 * (Classificação Internacional de Doenças).
 */
@RestController
@RequestMapping("/cids")
public class CidController {

    @Autowired
    private CidService cidService; // Serviço para lidar com lógica de negócios relacionada a CIDs

    /**
     * Lista CIDs com suporte a busca por termo e paginação.
     *
     * @param termo Termo de busca para filtrar os CIDs (opcional).
     * @param page  Número da página para paginação (default: 1).
     * @param size  Tamanho da página para paginação (default: 10).
     * @return Lista de CIDs filtrados e paginados.
     */
    @GetMapping("/listar")
    public List<Cid> listarCids(
            @RequestParam(value = "q", required = false, defaultValue = "") String termo,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return cidService.listarPorTermo(termo, page, size); // Chama o serviço para listar os CIDs
    }
}
