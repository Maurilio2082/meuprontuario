package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.TabelaTiss;
import br.com.meuprontuario.service.TabelaTissService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tabelaTiss")
public class TabelaTissController {

    @Autowired
    private TabelaTissService tabelaTissService;

    @GetMapping("/listar")
    public List<TabelaTiss> listarTiss(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return tabelaTissService.listarPorPagina(page, size);
    }
}
