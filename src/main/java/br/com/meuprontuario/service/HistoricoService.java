package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.CidDAO;
import br.com.meuprontuario.dao.EspecialidadeDAO;
import br.com.meuprontuario.dao.HistoricoDAO;
import br.com.meuprontuario.dao.HospitalDAO;
import br.com.meuprontuario.dao.MedicoDAO;
import br.com.meuprontuario.dao.PacienteDAO;
import br.com.meuprontuario.dao.TabelaTissDAO;
import br.com.meuprontuario.model.Historico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoDAO historicoDAO;

    @Autowired
    private PacienteDAO pacienteDAO;

    @Autowired
    private MedicoDAO medicoDAO;

    @Autowired
    private HospitalDAO hospitalDAO;

    @Autowired
    private EspecialidadeDAO especialidadeDAO;

    @Autowired
    private CidDAO cidDAO;

    @Autowired
    private TabelaTissDAO tabelaTissDAO;

    /**
     * Busca um histórico pelo ID.
     *
     * @param id ID do histórico.
     * @return Objeto Historico correspondente ou null se não encontrado.
     */
    public Historico buscarPorId(int id) {
        return historicoDAO.buscarPorId(id);
    }

    /**
     * Salva ou atualiza um histórico.
     *
     * @param historico Objeto Historico a ser salvo.
     */
    public void salvar(Historico historico) {
        historicoDAO.salvar(historico);
    }

    /**
     * Exclui um histórico pelo ID.
     *
     * @param id ID do histórico.
     */
    public void excluir(int id) {
        historicoDAO.excluir(id);
    }

    /**
     * Lista todos os históricos cadastrados.
     *
     * @return Lista de objetos Historico.
     */
    public List<Historico> listarTodos() {
        return historicoDAO.listarTodos();
    }

    /**
     * Lista históricos de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Historico na página solicitada.
     */
    public List<Historico> listarPorPagina(int page, int pageSize) {
        return historicoDAO.listarPorPagina(page, pageSize);
    }

    /**
     * Conta o número total de históricos cadastrados.
     *
     * @return Número total de históricos.
     */
    public int contarHistoricos() {
        return historicoDAO.contarHistoricos();
    }

    /**
     * Importa históricos de um arquivo XML.
     *
     * @param arquivo Arquivo XML contendo os históricos.
     * @return Lista de objetos Historico importados.
     */
    public List<Historico> importarHistoricosDeXML(MultipartFile arquivo) {
        List<Historico> historicos = new ArrayList<>();
        try (InputStream inputStream = arquivo.getInputStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);

            NodeList nodeList = doc.getElementsByTagName("historico");

            for (int i = 0; i < nodeList.getLength(); i++) {
                try {
                    Element elemento = (Element) nodeList.item(i);
                    Historico historico = new Historico();

                    historico.setDataConsulta(elemento.getElementsByTagName("dataConsulta").item(0).getTextContent());
                    historico.setObservacao(elemento.getElementsByTagName("observacao").item(0).getTextContent());

                    int pacienteId = Integer
                            .parseInt(elemento.getElementsByTagName("idPaciente").item(0).getTextContent());
                    historico.setIdPaciente(pacienteDAO.buscarPorId(pacienteId));

                    int hospitalId = Integer
                            .parseInt(elemento.getElementsByTagName("idHospital").item(0).getTextContent());
                    historico.setIdHospital(hospitalDAO.buscarPorId(hospitalId));

                    int medicoId = Integer.parseInt(elemento.getElementsByTagName("idMedico").item(0).getTextContent());
                    historico.setIdMedico(medicoDAO.buscarPorId(medicoId));

                    int especialidadeId = Integer
                            .parseInt(elemento.getElementsByTagName("idEspecialidade").item(0).getTextContent());
                    historico.setIdEspecialidade(especialidadeDAO.buscarPorId(especialidadeId));

                    String codCid = elemento.getElementsByTagName("codCid").item(0).getTextContent();
                    historico.setCid(cidDAO.buscarPorCodigo(codCid));

                    long codTiss = Long
                            .parseLong(elemento.getElementsByTagName("codigoTermo").item(0).getTextContent());
                    historico.setTabelaTiss(tabelaTissDAO.buscarPorCodigo(codTiss));

                    historicos.add(historico);
                } catch (Exception e) {
                    System.err.println("Erro ao processar histórico no índice " + i + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o XML: " + e.getMessage());
        }
        return historicos;
    }
}
