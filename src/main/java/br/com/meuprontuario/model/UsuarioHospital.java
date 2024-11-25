package br.com.meuprontuario.model;

public class UsuarioHospital extends Usuario {

    public UsuarioHospital(String username, String senha) {
        super(username, senha);
    }

    @Override
    public String getRedirectPage() {
        return "redirect:/hospitais";
    }
}
