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

    /**
     * Salva ou atualiza um paciente, garantindo que o endereço associado seja salvo antes.
     *
     * @param paciente Objeto Paciente a ser salvo ou atualizado.
     */
    public void salvar(Paciente paciente) {
        Endereco endereco = paciente.getEndereco();
        enderecoDAO.salvar(endereco); // Salva o endereço no banco de dados
        paciente.setEndereco(endereco); // Atualiza o ID do endereço no objeto paciente
        pacienteDAO.salvar(paciente); // Salva o paciente no banco de dados
    }

    /**
     * Lista os pacientes de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Paciente na página solicitada.
     */
    public List<Paciente> listarPorPagina(int page, int pageSize) {
        return pacienteDAO.listarPorPagina(page, pageSize);
    }

    /**
     * Conta o número total de pacientes cadastrados.
     *
     * @return Número total de pacientes.
     */
    public int contarPacientes() {
        return pacienteDAO.contarPacientes();
    }

    /**
     * Lista todos os pacientes cadastrados.
     *
     * @return Lista de objetos Paciente.
     */
    public List<Paciente> listarTodos() {
        return pacienteDAO.listarTodos();
    }

    /**
     * Busca um paciente pelo ID.
     *
     * @param id ID do paciente.
     * @return Objeto Paciente correspondente ou null se não encontrado.
     */
    public Paciente buscarPorId(int id) {
        return pacienteDAO.buscarPorId(id);
    }

    /**
     * Exclui um paciente e o endereço associado.
     *
     * @param id ID do paciente a ser excluído.
     * @throws IllegalArgumentException se o paciente não for encontrado.
     */
    public void excluir(int id) {
        Paciente paciente = pacienteDAO.buscarPorId(id);
        if (paciente != null) {
            pacienteDAO.excluir(id); // Exclui o paciente
            enderecoDAO.excluir(paciente.getEndereco().getIdEndereco()); // Exclui o endereço associado
        } else {
            throw new IllegalArgumentException("Paciente com o ID " + id + " não encontrado.");
        }
    }
}
