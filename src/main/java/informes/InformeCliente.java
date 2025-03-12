package informes;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import restaurante.Cliente;

import java.util.HashMap;
import java.util.List;

public class InformeCliente {
    public void generarInforme(List<Cliente> clientes) {
        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/jasper/Cliente.jasper"));
            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(clientes);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, new HashMap<>(), datasource);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

}
