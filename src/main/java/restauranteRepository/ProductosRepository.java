package restauranteRepository;

import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import restaurante.Productos;

import java.util.ArrayList;
import java.util.List;

public class ProductosRepository {
    private SessionFactory factory;

    public ProductosRepository() {
        factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Productos.class)
                .buildSessionFactory();
    }

    public void guardarProducto(Productos productos) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(productos);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Productos obtenerProductoPorId(int id) {
        Session session = factory.getCurrentSession();
        Productos productos = null;
        try {
            session.beginTransaction();
            productos = session.get(Productos.class, id);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return productos;
    }

    public void actualizarProductoPorId(int id, String nuevoNombre, String nuevaCategoria , float nuevoPrecio, String nuevoDisponible) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            Productos productos = session.get(Productos.class, id);

            if (productos != null) {
                productos.setNombre(nuevoNombre);
                productos.setCategoria(nuevaCategoria);
                productos.setPrecio(nuevoPrecio);
                productos.setDisponible(nuevoDisponible);
                session.update(productos);
                mostrarAlerta("Éxito", "Producto modificado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "Producto no encontrado con ese ID", Alert.AlertType.ERROR);
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void eliminarProductoPorId(int id) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            Productos productos = session.get(Productos.class, id);
            if (productos != null) {
                session.delete(productos);
                mostrarAlerta("Éxito", "Producto eliminado correctamente.", Alert.AlertType.INFORMATION);
            }else{
                mostrarAlerta("Error", "El ID del Producto no es correcto.", Alert.AlertType.ERROR);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public List<Productos> obtenerTodosLosProductos() {
        Session session = factory.getCurrentSession();
        List<Productos> productos = new ArrayList<>();
        try{
            session.beginTransaction();
            productos = session.createQuery("from restaurante.Productos", Productos.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }
        return productos;
    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
