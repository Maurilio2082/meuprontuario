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

    public List<TabelaTiss> listarPorPagina(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return tabelaTissDAO.listarPorPagina(offset, pageSize);
    }


    public List<TabelaTiss> listarPorTermo(String termo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return tabelaTissDAO.listarPorTermo(termo, offset, pageSize);
    }
    

}
