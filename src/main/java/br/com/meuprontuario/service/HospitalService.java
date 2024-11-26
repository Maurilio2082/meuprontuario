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

    /**
     * Salva ou atualiza um hospital, garantindo que o endereço associado seja salvo
     * antes.
     *
     * @param hospital Objeto Hospital a ser salvo ou atualizado.
     */
    public void salvar(Hospital hospital) {
        Endereco endereco = hospital.getEndereco();
        enderecoDAO.salvar(endereco); // Salva o endereço no banco de dados
        hospital.setEndereco(endereco); // Atualiza o ID do endereço no objeto hospital
        hospitalDAO.salvar(hospital); // Salva o hospital no banco de dados
    }

    /**
     * Lista os hospitais de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Hospital na página solicitada.
     */
    public List<Hospital> listarPorPagina(int page, int pageSize) {
        return hospitalDAO.listarPorPagina(page, pageSize);
    }

    /**
     * Conta o número total de hospitais cadastrados.
     *
     * @return Número total de hospitais.
     */
    public int contarHospitais() {
        return hospitalDAO.contarHospitais();
    }

    /**
     * Lista todos os hospitais cadastrados.
     *
     * @return Lista de objetos Hospital.
     */
    public List<Hospital> listarTodos() {
        return hospitalDAO.listarTodos();
    }

    /**
     * Busca um hospital pelo ID.
     *
     * @param id ID do hospital.
     * @return Objeto Hospital correspondente ou null se não encontrado.
     */
    public Hospital buscarPorId(int id) {
        return hospitalDAO.buscarPorId(id);
    }

    /**
     * Exclui um hospital e o endereço associado.
     *
     * @param id ID do hospital a ser excluído.
     * @throws IllegalArgumentException se o hospital não for encontrado.
     */
    public void excluir(int id) {
        Hospital hospital = hospitalDAO.buscarPorId(id);
        if (hospital != null) {
            hospitalDAO.excluir(id); // Exclui o hospital
            enderecoDAO.excluir(hospital.getEndereco().getIdEndereco()); // Exclui o endereço associado
        } else {
            throw new IllegalArgumentException("Hospital com o ID " + id + " não encontrado.");
        }
    }
}
