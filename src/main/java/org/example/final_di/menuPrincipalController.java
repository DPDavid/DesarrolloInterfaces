package org.example.final_di;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class menuPrincipalController {
    @FXML
    private Button btnCliente;
    @FXML
    private Button btnProductos;
    @FXML
    private Button btnPedidos;



    //Metodo para ir al XML de Clientes
    @FXML
    private void btnIrCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestionCliente.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCliente.getScene().getWindow();

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo para ir al XML de Pedidos
    @FXML
    private void btnIrPedidos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestionPedidos.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnPedidos.getScene().getWindow();

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo para ir al XML de Productos
    @FXML
    private void btnIrProductos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestionProductos.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnProductos.getScene().getWindow();

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}