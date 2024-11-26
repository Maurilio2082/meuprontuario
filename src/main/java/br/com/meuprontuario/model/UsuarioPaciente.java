package br.com.meuprontuario.model;

public class UsuarioPaciente extends Usuario {
    private int idPaciente;

    public UsuarioPaciente(String login, String senha, int idPaciente) {
        super(login, senha);
        this.idPaciente = idPaciente;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    @Override
    public String getRedirecionamento() {
        return "redirect:/pacientes/historico";
    }

}
