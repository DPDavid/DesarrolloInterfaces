package restauranteRepository;

import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import restaurante.Pedido;
import restaurante.PedidoDetalle;
import restaurante.Cliente;
import restaurante.Productos;

import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    private SessionFactory factory;

    public PedidoRepository() {
        factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Pedido.class)
                .addAnnotatedClass(PedidoDetalle.class)  // Añadido
                .addAnnotatedClass(Cliente.class)        // Añadido
                .addAnnotatedClass(Productos.class)      // Añadido
                .buildSessionFactory();
    }

    public void guardarPedido(Pedido pedido) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(pedido);
            session.getTransaction().commit();
            mostrarAlerta("Éxito", "Pedido guardado correctamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            mostrarAlerta("Error", "No se pudo guardar el pedido.", Alert.AlertType.ERROR);
        } finally {
            session.close();
        }
    }

    public void actualizarPedido(int id, String nuevoEstado) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            Pedido pedido = session.get(Pedido.class, id);

            if (pedido != null) {
                pedido.setEstado(nuevoEstado);
                session.update(pedido);
                mostrarAlerta("Éxito", "Pedido actualizado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se encontró un pedido con ese ID.", Alert.AlertType.ERROR);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            mostrarAlerta("Error", "No se pudo actualizar el pedido.", Alert.AlertType.ERROR);
        } finally {
            session.close();
        }
    }

    public void eliminarPedidoPorId(int id) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            Pedido pedido = session.get(Pedido.class, id);
            if (pedido != null) {
                session.delete(pedido);
                mostrarAlerta("Éxito", "Pedido eliminado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se encontró un pedido con ese ID.", Alert.AlertType.ERROR);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            mostrarAlerta("Error", "No se pudo eliminar el pedido.", Alert.AlertType.ERROR);
        } finally {
            session.close();
        }
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        Session session = factory.getCurrentSession();
        List<Pedido> pedidos = new ArrayList<>();
        try {
            session.beginTransaction();
            pedidos = session.createQuery("from restaurante.Pedido", Pedido.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return pedidos;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
