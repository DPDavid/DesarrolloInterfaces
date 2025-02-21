    package org.example.final_di;

    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.stage.Modality;
    import javafx.stage.Stage;
    import restaurante.Cliente;
    import restaurante.Pedido;
    import restaurante.PedidoDetalle;
    import restaurante.Productos;
    import restauranteRepository.ClienteRepository;
    import restauranteRepository.PedidoRepository;
    import restauranteRepository.ProductosRepository;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.regex.Pattern;

    public class gestionPedidosController {
        @FXML
        private Button btnSalir;

        Pattern patternNumeros = Pattern.compile("^[\\d,.]+$");

        public void abrirFormularioAgregar() {
            Stage ventanaAgregar = new Stage();
            ventanaAgregar.initModality(Modality.APPLICATION_MODAL);
            ventanaAgregar.setTitle("Agregar Pedido");

            // ComboBox para Cliente (solo nombre)
            Label labelCliente = new Label("Cliente:");
            ComboBox<String> comboCliente = new ComboBox<>();
            List<Cliente> listaClientes = new ClienteRepository().obtenerTodosLosClientes();
            comboCliente.getItems().addAll(listaClientes.stream().map(Cliente::getNombre).toList());

            // ComboBox para Productos (solo nombre)
            Label labelProductos = new Label("Productos:");
            ComboBox<String> comboProducto = new ComboBox<>();
            List<Productos> listaProductos = new ProductosRepository().obtenerTodosLosProductos();
            comboProducto.getItems().addAll(listaProductos.stream().map(Productos::getNombre).toList());

            // ComboBox para Estado
            Label labelEstado = new Label("Estado:");
            ComboBox<String> comboEstado = new ComboBox<>();
            comboEstado.getItems().addAll("Pendiente", "En preparación", "Entregado");
            comboEstado.setValue("Pendiente");

            // Campo de texto para Cantidad
            Label labelCantidad = new Label("Cantidad:");
            TextField campoCantidad = new TextField();

            // Botón Guardar
            Button botonGuardar = new Button("Guardar");
            botonGuardar.setOnAction(e -> {
                String nombreCliente = comboCliente.getValue();
                String nombreProducto = comboProducto.getValue();
                String cantidadTexto = campoCantidad.getText();
                String estado = comboEstado.getValue();

                // Validaciones
                if (nombreCliente == null || nombreProducto == null || cantidadTexto.isEmpty()  || estado.isEmpty()) {
                    mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                    return;
                }
                if (!Pattern.matches("\\d+", cantidadTexto)) {
                    mostrarAlerta("Error", "La cantidad y el total deben ser números válidos.", Alert.AlertType.ERROR);
                    return;
                }

                // Obtener Cliente y Producto seleccionados
                Cliente clienteSeleccionado = listaClientes.stream()
                        .filter(c -> c.getNombre().equals(nombreCliente))
                        .findFirst()
                        .orElse(null);

                Productos productoSeleccionado = listaProductos.stream()
                        .filter(p -> p.getNombre().equals(nombreProducto))
                        .findFirst()
                        .orElse(null);

                if (clienteSeleccionado == null || productoSeleccionado == null) {
                    mostrarAlerta("Error", "Cliente o Producto no encontrado.", Alert.AlertType.ERROR);
                    return;
                }

                // Crear Pedido y Detalle
                int cantidad = Integer.parseInt(cantidadTexto);

                LocalDateTime fechaHoraPedido = LocalDateTime.now();

                Pedido pedido = new Pedido(clienteSeleccionado, fechaHoraPedido,0.0 ,estado);
                pedido.addDetalle(new PedidoDetalle(pedido, productoSeleccionado, cantidad));

                // Guardar Pedido
                PedidoRepository pedidoRepository = new PedidoRepository();
                pedidoRepository.guardarPedido(pedido);
                ventanaAgregar.close();
            });

            // Botón Cancelar
            Button botonCancelar = new Button("Cancelar");
            botonCancelar.setOnAction(e -> ventanaAgregar.close());

            // Diseño de la Ventana
            VBox layout = new VBox(10);
            HBox hBox = new HBox(10);
            hBox.getChildren().addAll(botonGuardar, botonCancelar);

            layout.getChildren().addAll(
                    labelCliente, comboCliente,
                    labelProductos, comboProducto,
                    labelCantidad, campoCantidad,
                    labelEstado, comboEstado,
                    hBox
            );
            layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #D3D3D3;");

            Scene scene = new Scene(layout, 500, 600);
            ventanaAgregar.setScene(scene);
            ventanaAgregar.showAndWait();
        }



        public void abrirFormularioListar() {
            Stage ventanaListar = new Stage();
            ventanaListar.initModality(Modality.APPLICATION_MODAL);
            ventanaListar.setTitle("Lista de Pedidos");

            TableView<Pedido> tablaPedidos = new TableView<>();

            TableColumn<Pedido, Integer> columnaId = new TableColumn<>("ID");
            columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Pedido, String> columnaCliente = new TableColumn<>("Cliente");
            columnaCliente.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getCliente().getNombre()));

            TableColumn<Pedido, LocalDateTime> columnaFecha = new TableColumn<>("Fecha y Hora");
            columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

            TableColumn<Pedido, Double> columnaTotal = new TableColumn<>("Total");
            columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

            TableColumn<Pedido, String> columnaEstado = new TableColumn<>("Estado");
            columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

            tablaPedidos.getColumns().addAll(columnaId, columnaCliente, columnaFecha, columnaTotal, columnaEstado);

            PedidoRepository pedidoRepository = new PedidoRepository();
            List<Pedido> listaPedidos = pedidoRepository.obtenerTodosLosPedidos();
            tablaPedidos.getItems().addAll(listaPedidos);

            Button botonCerrar = new Button("Cerrar");
            botonCerrar.setOnAction(e -> ventanaListar.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(tablaPedidos, botonCerrar);
            layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #D3D3D3;");

            Scene scene = new Scene(layout, 600, 400);
            ventanaListar.setScene(scene);
            ventanaListar.showAndWait();
        }

        public void abrirFormularioModificar() {
            Stage ventanaModificar = new Stage();
            ventanaModificar.initModality(Modality.APPLICATION_MODAL);
            ventanaModificar.setTitle("Modificar Pedido");

            Label labelId = new Label("ID del Pedido:");
            TextField campoId = new TextField();

            Label labelEstado = new Label("Nuevo Estado:");
            ComboBox<String> comboEstado = new ComboBox<>();
            comboEstado.getItems().addAll("Pendiente", "En preparación", "Entregado");

            Button botonActualizar = new Button("Actualizar");
            botonActualizar.setOnAction(e -> {
                String idTexto = campoId.getText();
                String estado = comboEstado.getValue();

                if (idTexto.isEmpty() || estado == null) {
                    mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                    return;
                }

                try {
                    int idPedido = Integer.parseInt(idTexto);
                    PedidoRepository pedidoRepository = new PedidoRepository();
                    pedidoRepository.actualizarPedido(idPedido, estado);
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "El ID debe ser un número entero.", Alert.AlertType.ERROR);
                }

                ventanaModificar.close();
            });

            Button botonCancelar = new Button("Cancelar");
            botonCancelar.setOnAction(e -> ventanaModificar.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(labelId, campoId, labelEstado, comboEstado, botonActualizar, botonCancelar);
            layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #D3D3D3;");

            Scene scene = new Scene(layout, 400, 300);
            ventanaModificar.setScene(scene);
            ventanaModificar.showAndWait();
        }


        public void abrirFormularioEliminar() {
            Stage ventanaEliminar = new Stage();
            ventanaEliminar.initModality(Modality.APPLICATION_MODAL);
            ventanaEliminar.setTitle("Eliminar Pedido");

            Label labelId = new Label("ID del Pedido:");
            TextField campoId = new TextField();

            Button botonEliminar = new Button("Eliminar");
            botonEliminar.setOnAction(e -> {
                String idTexto = campoId.getText();
                if (idTexto.isEmpty()) {
                    mostrarAlerta("Error", "Por favor, ingrese un ID.", Alert.AlertType.ERROR);
                    return;
                }

                try {
                    int idPedido = Integer.parseInt(idTexto);
                    PedidoRepository pedidoRepository = new PedidoRepository();
                    pedidoRepository.eliminarPedidoPorId(idPedido);
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "El ID debe ser un número entero.", Alert.AlertType.ERROR);
                }

                ventanaEliminar.close();
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(labelId, campoId, botonEliminar);
            layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #D3D3D3;");

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