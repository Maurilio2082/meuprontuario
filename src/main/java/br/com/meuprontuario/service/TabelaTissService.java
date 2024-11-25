package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.TabelaTissDAO;
import br.com.meuprontuario.model.TabelaTiss;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TabelaTissService {

    private final TabelaTissDAO tabelaTissDAO = new TabelaTissDAO();

    public List<TabelaTiss> listarTodos() {
        return tabelaTissDAO.listarTodos();
    }

    public TabelaTiss buscarPorCodigo(long codigoTermo) {
        return tabelaTissDAO.buscarPorCodigo(codigoTermo);
    }
}
