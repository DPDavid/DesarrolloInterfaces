package org.example.final_di;

import informes.InformeCliente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import restaurante.Cliente;
import restauranteRepository.ClienteRepository;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class gestionClienteController {
    @FXML
    private Button btnSalir;

    private InformeCliente informeCliente = new InformeCliente();
    private ClienteRepository clienteRepository = new ClienteRepository();

    Pattern patternLetras = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$");
    Pattern patternNumeros = Pattern.compile("^\\d+$");

    public void abrirFormularioAgregar() {
        Stage ventanaAgregar = new Stage();
        ventanaAgregar.initModality(Modality.APPLICATION_MODAL);
        ventanaAgregar.setTitle("Agregar Estudiante");

        Label labelNombre = new Label("Nombre:");
        TextField campoNombre = new TextField();

        Label labelTelefono = new Label("Teléfono:");
        TextField campoTelefono = new TextField();

        Label labelEmail = new Label("Dirección:");
        TextField campoEmail = new TextField();

        Button botonGuardar = new Button("Guardar");
        botonGuardar.setOnAction(e -> {
            String nombre = campoNombre.getText();
            String telefono = campoTelefono.getText();
            String email = campoEmail.getText();

            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                return;
            }
            if (!patternLetras.matcher(nombre).matches()) {
                mostrarAlerta("Error", "Por favor, no ingrese números en el campo Nombre", Alert.AlertType.ERROR);
                return;
            }
            if (!patternNumeros.matcher(telefono).matches()) {
                mostrarAlerta("Error", "Por favor, ingrese solo números en el campo Teléfono", Alert.AlertType.ERROR);
                return;
            }
            Cliente cliente = new Cliente(nombre, telefono, email);
            ClienteRepository clienteRepository = new ClienteRepository();
            clienteRepository.guardarCliente(cliente);


            mostrarAlerta("Éxito", "Cliente agregado correctamente.", Alert.AlertType.INFORMATION);
            ventanaAgregar.close();
        });

        Button botonCancelar = new Button("Cancelar");
        botonCancelar.setOnAction(e -> ventanaAgregar.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelNombre, campoNombre, labelTelefono, campoTelefono, labelEmail, campoEmail, botonGuardar, botonCancelar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 300, 300);
        ventanaAgregar.setScene(scene);
        ventanaAgregar.showAndWait();
    }



    public void abrirFormularioListar() {
        Stage ventanaListar = new Stage();
        ventanaListar.initModality(Modality.APPLICATION_MODAL);
        ventanaListar.setTitle("Lista de Clientes");

        TableView<Cliente> tablaClientes = new TableView<>();

        TableColumn<Cliente, Integer> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> columnaTelefono = new TableColumn<>("Teléfono");
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<Cliente, String> columnaEmail = new TableColumn<>("Direccion");
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        tablaClientes.getColumns().addAll(columnaId, columnaNombre, columnaTelefono, columnaEmail);

        ClienteRepository clienteRepository = new ClienteRepository();
        List<Cliente> listaClientes = clienteRepository.obtenerTodosLosClientes();
        tablaClientes.getItems().addAll(listaClientes);

        Button botonCerrar = new Button("Cerrar");
        botonCerrar.setOnAction(e -> ventanaListar.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(tablaClientes, botonCerrar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 500, 400);
        ventanaListar.setScene(scene);
        ventanaListar.showAndWait();
    }

    public void abrirFormularioModificar() {
        Stage ventanaModificar = new Stage();
        ventanaModificar.initModality(Modality.APPLICATION_MODAL);
        ventanaModificar.setTitle("Actualizar Cliente");

        Label labelId = new Label("ID del Cliente:");
        TextField campoId = new TextField();

        Label labelNombre = new Label("Nuevo Nombre:");
        TextField campoNombre = new TextField();

        Label labelTelefono = new Label("Nuevo Telefóno:");
        TextField campoTelefono = new TextField();

        Label labelEmail = new Label("Nueva Dirección:");
        TextField campoEmail = new TextField();

        Button botonActualizar = new Button("Actualizar");
        botonActualizar.setOnAction(e -> {
            String nombre = campoNombre.getText();
            String telefono = campoTelefono.getText();
            String direccion = campoEmail.getText();

            String idTexto = campoId.getText();
            if (idTexto.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese un ID.", Alert.AlertType.ERROR);
                return;
            }

            try {
                if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
                    mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                    return;
                }
                if (!patternLetras.matcher(nombre).matches()) {
                    mostrarAlerta("Error", "Por favor, no ingrese números en el campo Nombre", Alert.AlertType.ERROR);
                    return;
                }
                if (!patternNumeros.matcher(telefono).matches()) {
                    mostrarAlerta("Error", "Por favor, ingrese solo números en el campo Teléfono", Alert.AlertType.ERROR);
                    return;
                }
                int idCliente = Integer.parseInt(idTexto);
                ClienteRepository clienteRepository = new ClienteRepository();
                clienteRepository.actualizarClientePorId(idCliente, campoNombre.getText(), campoTelefono.getText(), campoEmail.getText());


            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El ID debe ser un número entero.", Alert.AlertType.ERROR);
            }

            ventanaModificar.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelId, campoId, labelNombre, campoNombre, labelTelefono, campoTelefono, labelEmail, campoEmail, botonActualizar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;-fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 300, 300);
        ventanaModificar.setScene(scene);
        ventanaModificar.showAndWait();
    }

    public void abrirFormularioEliminar() {
        Stage ventanaEliminar = new Stage();
        ventanaEliminar.initModality(Modality.APPLICATION_MODAL);
        ventanaEliminar.setTitle("Eliminar Cliente");

        Label labelId = new Label("ID del Cliente:");
        TextField campoId = new TextField();

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.setOnAction(e -> {
            String idTexto = campoId.getText();
            if (idTexto.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese un ID.", Alert.AlertType.ERROR);
                return;
            }

            try {
                int idCliente = Integer.parseInt(idTexto);
                ClienteRepository clienteRepository = new ClienteRepository();
                clienteRepository.eliminarClientePorId(idCliente);

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

    @FXML
    public void generarInforme(ActionEvent event){
        List<Cliente> clientes = clienteRepository.obtenerTodosLosClientes();
        informeCliente.generarInforme(clientes);
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
