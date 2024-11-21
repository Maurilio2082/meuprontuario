package br.com.meuprontuario.model;

public class Medico {
    private int idMedico;
    private String nomeMedico;
    private String conselho;
    private Hospital hospital;
    private Especialidade especialidade;
    private String cbo;

    public Medico(int idMedico, String nomeMedico, String conselho, Hospital hospital, Especialidade especialidade,
            String cbo) {
        this.idMedico = idMedico;
        this.nomeMedico = nomeMedico;
        this.conselho = conselho;
        this.hospital = hospital;
        this.especialidade = especialidade;
        this.cbo = cbo;
    }

    public Medico() {
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getConselho() {
        return conselho;
    }

    public void setConselho(String conselho) {
        this.conselho = conselho;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public String getCbo() {
        return cbo;
    }

    public void setCbo(String cbo) {
        this.cbo = cbo;
    }
}
