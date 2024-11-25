package br.com.meuprontuario.service;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.dao.HospitalDAO;
import br.com.meuprontuario.dao.PacienteDAO;
import br.com.meuprontuario.model.Hospital;
import br.com.meuprontuario.model.Paciente;
import br.com.meuprontuario.model.Usuario;
import br.com.meuprontuario.model.UsuarioHospital;
import br.com.meuprontuario.model.UsuarioPaciente;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private Map<String, Usuario> usuarios = new HashMap<>();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final HospitalDAO hospitalDAO = new HospitalDAO();

    public LoginService() {
        usuarios.put("paciente", new UsuarioPaciente("paciente", "123", 0));
        usuarios.put("hospital", new UsuarioHospital("hospital", "456", 0));
    }

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
