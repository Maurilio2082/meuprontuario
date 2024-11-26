package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Cid;
import br.com.meuprontuario.service.CidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cids")
public class CidController {

    @Autowired
    private CidService cidService;

    @GetMapping("/listar")
    public List<Cid> listarCids(
            @RequestParam(value = "q", required = false, defaultValue = "") String termo,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return cidService.listarPorTermo(termo, page, size);
    }

}
