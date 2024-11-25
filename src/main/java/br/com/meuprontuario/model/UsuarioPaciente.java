package br.com.meuprontuario.model;

public class UsuarioPaciente extends Usuario {

    public UsuarioPaciente(String username, String senha) {
        super(username, senha);
    }

    @Override
    public String getRedirectPage() {
        return "redirect:/pacientes";
    }
}
