package br.com.meuprontuario.model;

public class Historico {
    private int idHistorico;
    private String dataConsulta;
    private String observacao;
    private Paciente idPaciente = new Paciente(0, "", "", "", "", "", null);
    private Hospital idHospital = new Hospital(0, "", "", "", "", "", null, "");
    private Medico idMedico = new Medico(0, "", "", null, null, "");
    private Especialidade idEspecialidade = new Especialidade(0, "");

    public Historico(int idHistorico, String dataConsulta, String observacao,
            Paciente idPaciente, Hospital idHospital, Medico idMedico, Especialidade idEspecialidade) {

        this.idHistorico = idHistorico;
        this.dataConsulta = dataConsulta;
        this.observacao = observacao;
        this.idPaciente = idPaciente;
        this.idHospital = idHospital;
        this.idEspecialidade = idEspecialidade;
        this.idMedico = idMedico;
    }

    public Historico() {
    }

    public String getDataConsulta() {
        return dataConsulta;
    }

    public int getIdHistorico() {
        return idHistorico;
    }

    public void setIdHospital(Hospital idHospital) {
        this.idHospital = idHospital;
    }

    public void setIdPaciente(Paciente idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Hospital getIdHospital() {
        return idHospital;
    }

    public Paciente getIdPaciente() {
        return idPaciente;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setDataConsulta(String dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public void setIdHistorico(int idHistorico) {
        this.idHistorico = idHistorico;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setIdMedico(Medico idMedico) {
        this.idMedico = idMedico;
    }

    public void setiEspecialidade(Especialidade iEspecialidade) {
        this.idEspecialidade = iEspecialidade;
    }

    public Medico getIdMedico() {
        return idMedico;
    }

    public Especialidade getiEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(Especialidade idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    public Especialidade getIdEspecialidade() {
        return idEspecialidade;
    }
}