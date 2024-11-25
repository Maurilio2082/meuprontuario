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

    public Historico buscarPorId(int id) {
        return historicoDAO.buscarPorId(id);
    }

    public void salvar(Historico historico) {
        historicoDAO.salvar(historico);
    }

    public void excluir(int id) {
        historicoDAO.excluir(id);
    }

    public List<Historico> listarTodos() {
        return historicoDAO.listarTodos();
    }

    public List<Historico> listarPorPagina(int page, int pageSize) {
        return historicoDAO.listarPorPagina(page, pageSize);
    }

    public int contarHistoricos() {
        return historicoDAO.contarHistoricos();
    }

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
