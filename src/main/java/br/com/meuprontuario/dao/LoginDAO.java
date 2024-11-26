package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Hospital;
import br.com.meuprontuario.model.Paciente;
import br.com.meuprontuario.model.Usuario;
import br.com.meuprontuario.model.UsuarioHospital;
import br.com.meuprontuario.model.UsuarioPaciente;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO (Data Access Object) responsável por operações relacionadas à
 * autenticação de usuários.
 */
@Repository
public class LoginDAO {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final HospitalDAO hospitalDAO = new HospitalDAO();

    /**
     * Busca um usuário pelo login e senha.
     *
     * @param login Login do usuário.
     * @param senha Senha do usuário.
     * @return Objeto Usuario correspondente ou null se não encontrado.
     */
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM USUARIO WHERE LOGIN = ? AND SENHA = ?";
        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("TIPO");
                    if ("PACIENTE".equals(tipo)) {
                        Paciente paciente = pacienteDAO.buscarPorId(rs.getInt("ID_PACIENTE"));
                        return new UsuarioPaciente(login, senha, paciente.getIdPaciente());
                    } else if ("HOSPITAL".equals(tipo)) {
                        Hospital hospital = hospitalDAO.buscarPorId(rs.getInt("ID_HOSPITAL"));
                        return new UsuarioHospital(login, senha, hospital.getIdHospital());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário.", e);
        }
        return null;
    }
}
