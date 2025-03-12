package informes;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import restaurante.Pedido;

import java.util.HashMap;
import java.util.List;

public class InformePedidos {
        public void generarInforme(List<Pedido> pedidos) {
            try {
                JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/jasper/PedidosPreparacion.jasper"));
                JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(pedidos);
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, new HashMap<>(), datasource);
                JasperViewer.viewReport(jasperPrint, false);

            } catch (JRException e) {
                e.printStackTrace();
            }
        }
}
