package br.com.meuprontuario.controller;

import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Hospital;
import br.com.meuprontuario.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hospitais")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping
    public String listar(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 10; // Tamanho fixo de página
        List<Hospital> hospitais = hospitalService.listarPorPagina(page, pageSize);
        int totalHospitais = hospitalService.contarHospitais();
        int totalPages = (int) Math.ceil((double) totalHospitais / pageSize);

        model.addAttribute("hospitais", hospitais);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "hospital-lista"; // Nome do template HTML
    }

    @GetMapping("/formulario")
    public String exibirFormulario(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Hospital hospital = (id != null) ? hospitalService.buscarPorId(id)
                : new Hospital(0, "", "", "", "", "", new Endereco(), "");
        model.addAttribute("hospital", hospital);
        return "hospital-formulario"; // Nome da página HTML correspondente
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Hospital hospital) {
        hospitalService.salvar(hospital);
        return "redirect:/hospitais";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable int id) {
        hospitalService.excluir(id);
        return "redirect:/hospitais";
    }

}
