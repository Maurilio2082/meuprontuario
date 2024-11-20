package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.EnderecoDAO;
import br.com.meuprontuario.dao.PacienteDAO;
import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteDAO pacienteDAO;

    @Autowired
    private EnderecoDAO enderecoDAO;

    public void salvar(Paciente paciente) {
        Endereco endereco = paciente.getEndereco();
        enderecoDAO.salvar(endereco);

        paciente.setEndereco(endereco);
        pacienteDAO.salvar(paciente);
    }

    public List<Paciente> listarPorPagina(int page, int pageSize) {
        return pacienteDAO.listarPorPagina(page, pageSize);
    }

    public int contarPacientes() {
        return pacienteDAO.contarPacientes();
    }

    public List<Paciente> listarTodos() {
        return pacienteDAO.listarTodos();
    }

    public Paciente buscarPorId(int id) {
        return pacienteDAO.buscarPorId(id);
    }

    public void excluir(int id) {
        Paciente paciente = pacienteDAO.buscarPorId(id);
        if (paciente != null) {
            pacienteDAO.excluir(id);

            enderecoDAO.excluir(paciente.getEndereco().getIdEndereco());
        } else {
            throw new IllegalArgumentException("Paciente com o ID " + id + " não encontrado.");
        }
    }
}
