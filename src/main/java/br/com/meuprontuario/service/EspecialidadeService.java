package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.EspecialidadeDAO;
import br.com.meuprontuario.dao.MedicoDAO;
import br.com.meuprontuario.model.Especialidade;
import br.com.meuprontuario.model.Medico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeDAO especialidadeDAO;

    @Autowired
    private MedicoDAO medicoDAO;

    /**
     * Lista todas as especialidades cadastradas.
     *
     * @return Lista de objetos Especialidade.
     */
    public List<Especialidade> listarTodas() {
        return especialidadeDAO.listarTodas();
    }

    /**
     * Busca uma especialidade pelo ID.
     *
     * @param id ID da especialidade.
     * @return Objeto Especialidade correspondente ou null se não encontrado.
     */
    public Especialidade buscarPorId(int id) {
        return especialidadeDAO.buscarPorId(id);
    }

    /**
     * Salva ou atualiza uma especialidade.
     *
     * @param especialidade Objeto Especialidade a ser salvo.
     */
    public void salvar(Especialidade especialidade) {
        especialidadeDAO.salvar(especialidade);
    }

    /**
     * Exclui uma especialidade pelo ID.
     *
     * @param id ID da especialidade.
     */
    public void excluir(int id) {
        especialidadeDAO.excluir(id);
    }

    /**
     * Lista especialidades de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Especialidade na página solicitada.
     */
    public List<Especialidade> listarPorPagina(int page, int pageSize) {
        return especialidadeDAO.listarPorPagina(page, pageSize);
    }

    /**
     * Conta o número total de especialidades cadastradas.
     *
     * @return Número total de especialidades.
     */
    public int contarEspecialidades() {
        return especialidadeDAO.contarEspecialidades();
    }

    /**
     * Busca a especialidade associada a um médico.
     *
     * @param medicoId ID do médico.
     * @return Objeto Especialidade associado ou null se não encontrado.
     */
    public Especialidade buscarEspecialidadePorMedico(int medicoId) {
        Medico medico = medicoDAO.buscarPorId(medicoId);
        return medico != null ? medico.getEspecialidade() : null;
    }
}
