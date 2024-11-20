package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.EnderecoDAO;
import br.com.meuprontuario.dao.HospitalDAO;
import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    @Autowired
    private HospitalDAO hospitalDAO;

    @Autowired
    private EnderecoDAO enderecoDAO;

    public List<Hospital> listarTodos() {
        return hospitalDAO.listarTodos();
    }

    public Hospital buscarPorId(int id) {
        return hospitalDAO.buscarPorId(id);
    }

    public void salvar(Hospital hospital) {
        // Salvar o endereço primeiro e associar ao hospital
        Endereco endereco = hospital.getEndereco();
        enderecoDAO.salvar(endereco);

        // Salvar o hospital com o ID do endereço
        hospital.setEndereco(endereco);
        hospitalDAO.salvar(hospital);
    }

    public void excluir(int id) {
        Hospital hospital = hospitalDAO.buscarPorId(id);
        if (hospital != null) {
            // Excluir o hospital
            hospitalDAO.excluir(id);

            // Excluir o endereço associado
            enderecoDAO.excluir(hospital.getEndereco().getIdEndereco());
        } else {
            throw new IllegalArgumentException("Hospital com o ID " + id + " não encontrado.");
        }
    }

    public List<Hospital> listarPorPagina(int page, int pageSize) {
        return hospitalDAO.listarPorPagina(page, pageSize);
    }

    public int contarHospitais() {
        return hospitalDAO.contarHospitais();
    }

}
