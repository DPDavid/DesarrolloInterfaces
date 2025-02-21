package org.example.final_di;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import restaurante.Productos;

import restauranteRepository.ProductosRepository;

import java.util.List;
import java.util.regex.Pattern;

public class gestionProductosController {
    @FXML
    private Button btnSalir;

    Pattern patternLetras = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$");
    Pattern patternNumeros = Pattern.compile("^[\\d,.]+$");

    public void abrirFormularioAgregar() {
        Stage ventanaAgregar = new Stage();
        ventanaAgregar.initModality(Modality.APPLICATION_MODAL);
        ventanaAgregar.setTitle("Agregar Producto");

        Label labelNombre = new Label("Nombre:");
        TextField campoNombre = new TextField();

        Label labelCategoria = new Label("Categoria:");
        TextField campoCategoria = new TextField();

        Label labelPrecio = new Label("Precio:");
        TextField campoPrecio = new TextField();

        Label labelDisponible = new Label("Disponible:");
        ComboBox<String> comboDisponible = new ComboBox<>();
        comboDisponible.getItems().addAll("Sí", "No");
        comboDisponible.setValue("Sí");

        Button botonGuardar = new Button("Guardar");
        botonGuardar.setOnAction(e -> {
            String nombre = campoNombre.getText();
            String categoria = campoCategoria.getText();
            String precio = campoPrecio.getText();
            String disponible = comboDisponible.getValue();

            if (nombre.isEmpty() || categoria.isEmpty() || precio.isEmpty() || disponible.isEmpty()) {
                mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                return;
            }
            if (!patternLetras.matcher(nombre).matches() || !patternLetras.matcher(categoria).matches()) {
                mostrarAlerta("Error", "Por favor, no ingrese números en el campo Nombre o Categoria", Alert.AlertType.ERROR);
                return;
            }
            if (!patternNumeros.matcher(precio).matches()) {
                mostrarAlerta("Error", "Por favor, ingrese solo números en el campo Precio", Alert.AlertType.ERROR);
                return;
            }
            double precioProducto = Double.parseDouble(precio);
            Productos productos = new Productos(nombre, categoria, precioProducto, disponible);
            ProductosRepository productosRepository = new ProductosRepository();
            productosRepository.guardarProducto(productos);
            mostrarAlerta("Exito", "Producto agregado correctamente.", Alert.AlertType.INFORMATION);
            ventanaAgregar.close();
        });

        Button botonCancelar = new Button("Cancelar");
        botonCancelar.setOnAction(e -> ventanaAgregar.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelNombre, campoNombre, labelCategoria, campoCategoria,
                labelPrecio, campoPrecio, labelDisponible, comboDisponible, botonGuardar, botonCancelar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 400, 500);
        ventanaAgregar.setScene(scene);
        ventanaAgregar.showAndWait();
    }

    public void abrirFormularioListar() {
        Stage ventanaListar = new Stage();
        ventanaListar.initModality(Modality.APPLICATION_MODAL);
        ventanaListar.setTitle("Lista de Productos");

        TableView<Productos> tablaProductos = new TableView<>();

        TableColumn<Productos, Integer> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Productos, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Productos, String> columnaCategoria = new TableColumn<>("Categoría");
        columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Productos, Double> columnaPrecio = new TableColumn<>("Precio");
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));


        tablaProductos.getColumns().addAll(columnaId, columnaNombre, columnaCategoria, columnaPrecio);

        ProductosRepository productosRepository = new ProductosRepository();
        List<Productos> listaProductos = productosRepository.obtenerTodosLosProductos();
        tablaProductos.getItems().addAll(listaProductos);

        Button botonCerrar = new Button("Cerrar");
        botonCerrar.setOnAction(e -> ventanaListar.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(tablaProductos, botonCerrar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 600, 400);
        ventanaListar.setScene(scene);
        ventanaListar.showAndWait();
    }

    public void abrirFormularioModificar() {
        Stage ventanaModificar = new Stage();
        ventanaModificar.initModality(Modality.APPLICATION_MODAL);
        ventanaModificar.setTitle("Actualizar Productos");

        Label labelId = new Label("ID del Producto:");
        TextField campoId = new TextField();

        Label labelNombre = new Label("Nuevo Nombre:");
        TextField campoNombre = new TextField();

        Label labelCategoria = new Label("Categoria:");
        TextField campoCategoria = new TextField();

        Label labelPrecio = new Label("Precio:");
        TextField campoPrecio = new TextField();

        Label labelDisponible = new Label("Disponible:");
        ComboBox<String> comboDisponible = new ComboBox<>();
        comboDisponible.getItems().addAll("Sí", "No");
        comboDisponible.setValue("Sí");

        Button botonActualizar = new Button("Actualizar");
        botonActualizar.setOnAction(e -> {
            String nombre = campoNombre.getText();
            String categoria = campoCategoria.getText();
            String precio = campoPrecio.getText();
            String disponible = comboDisponible.getValue();

            String idTexto = campoId.getText();
            if (idTexto.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese un ID.", Alert.AlertType.ERROR);
                return;
            }

            try {
                if (nombre.isEmpty() || categoria.isEmpty() || precio.isEmpty() || disponible.isEmpty()) {
                    mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                    return;
                }
                if (!patternLetras.matcher(nombre).matches() || !patternLetras.matcher(categoria).matches()) {
                    mostrarAlerta("Error", "Por favor, no ingrese números en el campo Nombre o Categoria", Alert.AlertType.ERROR);
                    return;
                }
                if (!patternNumeros.matcher(precio).matches()) {
                    mostrarAlerta("Error", "Por favor, ingrese solo números en el campo Precio", Alert.AlertType.ERROR);
                    return;
                }
                int idProducto = Integer.parseInt(idTexto);
                int precioProducto = Integer.parseInt(precio);
                ProductosRepository productosRepository = new ProductosRepository();
                productosRepository.actualizarProductoPorId(idProducto, nombre, categoria, precioProducto, disponible);

            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El ID debe ser un número entero.", Alert.AlertType.ERROR);
            }

            ventanaModificar.close();
        });
        Button botonCancelar = new Button("Cancelar");
        botonCancelar.setOnAction(e -> ventanaModificar.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelId, campoId, labelNombre, campoNombre, labelCategoria, campoCategoria, labelPrecio, campoPrecio,labelDisponible,comboDisponible ,botonCancelar,botonActualizar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;-fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 400, 500);
        ventanaModificar.setScene(scene);
        ventanaModificar.showAndWait();
    }

    public void abrirFormularioEliminar() {
        Stage ventanaEliminar = new Stage();
        ventanaEliminar.initModality(Modality.APPLICATION_MODAL);
        ventanaEliminar.setTitle("Eliminar Producto");

        Label labelId = new Label("ID del Producto:");
        TextField campoId = new TextField();

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.setOnAction(e -> {
            String idTexto = campoId.getText();
            if (idTexto.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese un ID.", Alert.AlertType.ERROR);
                return;
            }

            try {
                int idProducto = Integer.parseInt(idTexto);
                ProductosRepository productosRepository = new ProductosRepository();
                productosRepository.eliminarProductoPorId(idProducto);


            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El ID debe ser un número entero.", Alert.AlertType.ERROR);
            }

            ventanaEliminar.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelId, campoId, botonEliminar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;-fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 300, 200);
        ventanaEliminar.setScene(scene);
        ventanaEliminar.showAndWait();
    }

    public void volverAlMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
            Stage stage = (Stage) btnSalir.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
