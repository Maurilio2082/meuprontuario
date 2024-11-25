package br.com.meuprontuario.model;

public class UsuarioHospital extends Usuario {
    private int idHospital;

    public UsuarioHospital(String login, String senha, int idHospital) {
        super(login, senha);
        this.idHospital = idHospital;
    }

    public int getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(int idHospital) {
        this.idHospital = idHospital;
    }

    @Override
    public String getRedirectPage() {
        return "redirect:/hospitais";
    }
}
