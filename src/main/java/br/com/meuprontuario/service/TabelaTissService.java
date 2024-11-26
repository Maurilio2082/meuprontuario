package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.TabelaTissDAO;
import br.com.meuprontuario.model.TabelaTiss;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TabelaTissService {

    private final TabelaTissDAO tabelaTissDAO = new TabelaTissDAO();

    /**
     * Lista todos os registros da Tabela TISS.
     *
     * @return Lista de objetos TabelaTiss.
     */
    public List<TabelaTiss> listarTodos() {
        return tabelaTissDAO.listarTodos();
    }

    /**
     * Busca um registro da Tabela TISS pelo código do termo.
     *
     * @param codigoTermo Código do termo da Tabela TISS.
     * @return Objeto TabelaTiss correspondente ou null se não encontrado.
     */
    public TabelaTiss buscarPorCodigo(long codigoTermo) {
        return tabelaTissDAO.buscarPorCodigo(codigoTermo);
    }

    /**
     * Lista os registros da Tabela TISS de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos TabelaTiss na página solicitada.
     */
    public List<TabelaTiss> listarPorPagina(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return tabelaTissDAO.listarPorPagina(offset, pageSize);
    }

    /**
     * Lista os registros da Tabela TISS filtrados por um termo.
     *
     * @param termo    Termo para filtrar os registros.
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos TabelaTiss que atendem ao filtro.
     */
    public List<TabelaTiss> listarPorTermo(String termo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return tabelaTissDAO.listarPorTermo(termo, offset, pageSize);
    }
}
