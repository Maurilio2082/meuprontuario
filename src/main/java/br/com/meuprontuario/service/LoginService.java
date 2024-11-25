package br.com.meuprontuario.service;

import br.com.meuprontuario.model.Usuario;
import br.com.meuprontuario.model.UsuarioHospital;
import br.com.meuprontuario.model.UsuarioPaciente;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private Map<String, Usuario> usuarios = new HashMap<>();

    public LoginService() {
        // Instanciação direta para o protótipo
        usuarios.put("paciente", new UsuarioPaciente("paciente", "123"));
        usuarios.put("hospital", new UsuarioHospital("hospital", "456"));
    }

    public Usuario autenticar(String username, String senha) {
        Usuario usuario = usuarios.get(username);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        return null;
    }
}
