package br.com.meuprontuario.model;

public class Medico {
    private int idMedico;
    private String nomeMedico;
    private String conselho;
    private Hospital hospital;
    private Especialidade especialidade;

    public Medico(int idMedico, String nomeMedico, String conselho, Hospital hospital, Especialidade especialidade) {
        this.idMedico = idMedico;
        this.nomeMedico = nomeMedico;
        this.conselho = conselho;
        this.hospital = hospital;
        this.especialidade = especialidade;
    }

    public Medico() {

    }

    // Getters e Setters
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

    public int getEspecialidadeId() {
        return especialidade != null ? especialidade.getId() : 0;
    }

    public void setEspecialidadeId(int especialidadeId) {
        if (especialidadeId != 0) {
            this.especialidade = new Especialidade();
            this.especialidade.setId(especialidadeId);
        } else {
            this.especialidade = null;
        }
    }

    public int getHospitalId() {
        return hospital != null ? hospital.getIdHospital() : 0;
    }

    public void setHospitalId(int hospitalId) {
        if (hospitalId != 0) {
            this.hospital = new Hospital();
            this.hospital.setIdHospital(hospitalId);
        } else {
            this.hospital = null;
        }
    }

}
