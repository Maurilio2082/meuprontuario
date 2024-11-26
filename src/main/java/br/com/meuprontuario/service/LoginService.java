package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.LoginDAO;
import br.com.meuprontuario.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de autenticação de usuários.
 */
@Service
public class LoginService {

    @Autowired
    private LoginDAO loginDAO;

    /**
     * Autentica um usuário com base no login e senha fornecidos.
     *
     * @param login Login do usuário.
     * @param senha Senha do usuário.
     * @return Objeto Usuario autenticado ou null se a autenticação falhar.
     */
    public Usuario autenticar(String login, String senha) {
        return loginDAO.autenticar(login, senha);
    }
}
