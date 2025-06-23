package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.ConsultaUsuario;
import model.Sesion;
import model.Usuario;
import utils.Paths;

import javax.swing.*;
import java.io.IOException;

public class Login {

    Usuario usuario = new Usuario();
    private ConsultaUsuario consultaUsuario = new ConsultaUsuario();

    @FXML
    private Button btnIngresar;

    @FXML
    private Label lblContrasena;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField pwdContrasena;

    @FXML
    void click(ActionEvent event) {
        usuario.setUsuario(txtUsuario.getText());
        usuario.setContrasena(pwdContrasena.getText());

        if (consultaUsuario.buscar(usuario)) {
            Sesion.getInstance().iniciarSesion(usuario);
            System.out.println("Bienvenido al sistema");

            // Abrir vista del controlador de cabinas
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.ControladorFXML));
                AnchorPane root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Controlador de Cabinas");
                stage.setScene(new Scene(root));
                stage.show();

                // Cerrar ventana actual (login)
                Stage loginStage = (Stage) btnIngresar.getScene().getWindow();
                loginStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar la vista de cabinas.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }
    }
}
