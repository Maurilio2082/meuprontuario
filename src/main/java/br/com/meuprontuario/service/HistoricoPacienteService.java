package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.HistoricoPacienteDAO;
import br.com.meuprontuario.model.Historico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoPacienteService {

    @Autowired
    private HistoricoPacienteDAO historicoPacienteDAO;

    /**
     * Lista os históricos médicos associados a um paciente.
     *
     * @param idPaciente ID do paciente.
     * @return Lista de objetos Historico.
     */
    public List<Historico> listarPorPaciente(int idPaciente) {
        return historicoPacienteDAO.listarPorPaciente(idPaciente);
    }
}

