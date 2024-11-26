package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.CidDAO;
import br.com.meuprontuario.model.Cid;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CidService {

    private final CidDAO cidDAO = new CidDAO(); // DAO responsável por acessar a tabela CID no banco de dados.

    /**
     * Lista todos os registros da tabela CID.
     *
     * @return Lista de objetos Cid.
     */
    public List<Cid> listarTodos() {
        return cidDAO.listarTodos();
    }

    /**
     * Busca um CID pelo código.
     *
     * @param codCid Código do CID.
     * @return Objeto Cid correspondente ou null se não encontrado.
     */
    public Cid buscarPorCodigo(String codCid) {
        return cidDAO.buscarPorCodigo(codCid);
    }

    /**
     * Lista os registros da tabela CID de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Cid.
     */
    public List<Cid> listarPorPagina(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return cidDAO.listarPorPagina(offset, pageSize);
    }

    /**
     * Lista os registros da tabela CID filtrados por um termo.
     *
     * @param termo    Termo para filtrar os registros.
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Cid que atendem ao filtro.
     */
    public List<Cid> listarPorTermo(String termo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return cidDAO.listarPorTermo(termo, offset, pageSize);
    }
}
