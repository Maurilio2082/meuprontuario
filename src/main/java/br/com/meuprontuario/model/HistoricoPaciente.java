package br.com.meuprontuario.model;

public class HistoricoPaciente extends Historico {
    public HistoricoPaciente(int idHistorico, String dataConsulta, String observacao, Paciente paciente,
            Hospital hospital, Medico medico, Especialidade especialidade,
            Cid cid, TabelaTiss tabelaTiss) {
        super(idHistorico, dataConsulta, observacao, paciente, hospital, medico, especialidade, cid, tabelaTiss);
    }
}
