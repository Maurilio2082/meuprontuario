package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.MedicoDAO;
import br.com.meuprontuario.model.Especialidade;
import br.com.meuprontuario.model.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoDAO medicoDAO;

    /**
     * Salva ou atualiza um médico no banco de dados.
     *
     * @param medico Objeto Medico a ser salvo.
     */
    public void salvar(Medico medico) {
        medicoDAO.salvar(medico);
    }

    /**
     * Lista todos os médicos cadastrados.
     *
     * @return Lista de objetos Medico.
     */
    public List<Medico> listarTodos() {
        return medicoDAO.listarTodos();
    }

    /**
     * Busca um médico pelo ID.
     *
     * @param id ID do médico.
     * @return Objeto Medico correspondente ou null se não encontrado.
     */
    public Medico buscarPorId(int id) {
        return medicoDAO.buscarPorId(id);
    }

    /**
     * Exclui um médico pelo ID.
     *
     * @param id ID do médico.
     */
    public void excluir(int id) {
        medicoDAO.excluir(id);
    }

    /**
     * Lista médicos de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Medico na página solicitada.
     */
    public List<Medico> listarPorPagina(int page, int pageSize) {
        return medicoDAO.listarPorPagina(page, pageSize);
    }

    /**
     * Conta o número total de médicos cadastrados.
     *
     * @return Número total de médicos.
     */
    public int contarMedicos() {
        return medicoDAO.contarMedicos();
    }

    /**
     * Lista médicos associados a um hospital específico.
     *
     * @param hospitalId ID do hospital.
     * @return Lista de objetos Medico.
     */
    public List<Medico> listarPorHospital(int hospitalId) {
        return medicoDAO.listarPorHospital(hospitalId);
    }

    /**
     * Busca a especialidade de um médico pelo ID.
     *
     * @param medicoId ID do médico.
     * @return Objeto Especialidade correspondente ou null se não encontrado.
     */
    public Especialidade buscarEspecialidadePorMedico(int medicoId) {
        return medicoDAO.buscarEspecialidadePorMedico(medicoId);
    }
}
