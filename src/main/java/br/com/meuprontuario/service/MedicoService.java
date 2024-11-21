package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.MedicoDAO;
import br.com.meuprontuario.model.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoDAO medicoDAO;

    public void salvar(Medico medico) {
        medicoDAO.salvar(medico);
    }

    public List<Medico> listarTodos() {
        return medicoDAO.listarTodos();
    }

    public Medico buscarPorId(int id) {
        return medicoDAO.buscarPorId(id);
    }

    public void excluir(int id) {
        medicoDAO.excluir(id);
    }

     public List<Medico> listarPorPagina(int page, int pageSize) {
        return medicoDAO.listarPorPagina(page, pageSize);
    }

    public int contarMedicos() {
        return medicoDAO.contarMedicos();
    }
}
