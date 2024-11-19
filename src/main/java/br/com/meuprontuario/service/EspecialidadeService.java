package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.EspecialidadeDAO;
import br.com.meuprontuario.model.Especialidade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {
    private EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();

    public void salvar(Especialidade especialidade) {
        if (especialidade.getNome() == null || especialidade.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome da especialidade não pode ser vazio.");
        }
        especialidadeDAO.salvar(especialidade);
    }

    public List<Especialidade> listarPorPagina(int page, int pageSize) {
        return especialidadeDAO.listarPorPagina(page, pageSize);
    }

    public int contarEspecialidades() {
        return especialidadeDAO.contarEspecialidades();
    }

    public Especialidade buscarPorId(Long id) {
        return especialidadeDAO.buscarPorId(id);
    }

    public void excluir(Long id) {
        especialidadeDAO.excluir(id);
    }
}
