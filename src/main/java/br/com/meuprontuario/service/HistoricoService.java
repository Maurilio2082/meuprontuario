package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.HistoricoDAO;
import br.com.meuprontuario.model.Historico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoDAO historicoDAO;

    public Historico buscarPorId(int id) {
        return historicoDAO.buscarPorId(id);
    }

    public void salvar(Historico historico) {
        historicoDAO.salvar(historico);
    }

    public void excluir(int id) {
        historicoDAO.excluir(id);
    }

    public List<Historico> listarTodos() {
        return historicoDAO.listarTodos();
    }

    public List<Historico> listarPorPagina(int page, int pageSize) {
        return historicoDAO.listarPorPagina(page, pageSize);
    }

    public int contarHistoricos() {
        return historicoDAO.contarHistoricos();
    }
}
