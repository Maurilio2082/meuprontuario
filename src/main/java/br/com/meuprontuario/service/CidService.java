package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.CidDAO;
import br.com.meuprontuario.model.Cid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidService {

    private final CidDAO cidDAO = new CidDAO();

    public List<Cid> listarTodos() {
        return cidDAO.listarTodos();
    }

    public Cid buscarPorCodigo(String codCid) {
        return cidDAO.buscarPorCodigo(codCid);
    }

    public List<Cid> listarPorPagina(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return cidDAO.listarPorPagina(offset, pageSize);
    }

}
