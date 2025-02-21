package restauranteRepository;

import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import restaurante.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    private SessionFactory factory;

    public ClienteRepository() {
        factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Cliente.class)
                .buildSessionFactory();
    }

    public void guardarCliente(Cliente cliente) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(cliente);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Cliente obtenerClientePorId(int id) {
        Session session = factory.getCurrentSession();
        Cliente cliente = null;
        try {
            session.beginTransaction();
            cliente = session.get(Cliente.class, id);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return cliente;
    }

    public void actualizarClientePorId(int id, String nuevoNombre, String nuevoTelefono, String nuevaDireccion) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            Cliente cliente = session.get(Cliente.class, id);

            if (cliente != null) {
                cliente.setNombre(nuevoNombre);
                cliente.setTelefono(nuevoTelefono);
                cliente.setDireccion(nuevaDireccion);
                session.update(cliente);
                mostrarAlerta("Éxito", "Cliente modificado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "El ID del Cliente no es correcto.", Alert.AlertType.ERROR);
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void eliminarClientePorId(int id) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            Cliente cliente = session.get(Cliente.class, id);
            if (cliente != null) {
                session.delete(cliente);
                mostrarAlerta("Éxito", "Cliente eliminado correctamente.", Alert.AlertType.INFORMATION);
            }else{
                mostrarAlerta("Error", "El ID del Cliente no es correcto.", Alert.AlertType.ERROR);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public List<Cliente> obtenerTodosLosClientes() {
        Session session = factory.getCurrentSession();
        List<Cliente> clientes = new ArrayList<>();
        try{
            session.beginTransaction();
            clientes = session.createQuery("from restaurante.Cliente", Cliente.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }
        return clientes;
    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
