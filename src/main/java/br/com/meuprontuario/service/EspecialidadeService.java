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

    public List<Especialidade> listarTodas() {
        return especialidadeDAO.listarTodas();
    }

    public Especialidade buscarPorId(int id) {
        return especialidadeDAO.buscarPorId(id);
    }

    public void salvar(Especialidade especialidade) {
        especialidadeDAO.salvar(especialidade);
    }

    public void excluir(int id) {
        especialidadeDAO.excluir(id);
    }

    public List<Especialidade> listarPorPagina(int page, int pageSize) {
        return especialidadeDAO.listarPorPagina(page, pageSize);
    }

    public int contarEspecialidades() {
        return especialidadeDAO.contarEspecialidades();
    }

    public Especialidade buscarEspecialidadePorMedico(int medicoId) {
        Medico medico = medicoDAO.buscarPorId(medicoId);
        return medico != null ? medico.getEspecialidade() : null;
    }

}
