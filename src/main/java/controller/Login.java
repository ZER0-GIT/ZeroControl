package controller;

import app.main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ConsultaUsuario;
import model.Sesion;
import model.Usuario;
import utils.Paths;

import javax.swing.*;

public class Login {

    Usuario usuario = new Usuario();
    private ConsultaUsuario consultaUsuario = new ConsultaUsuario();
    main app = new main();

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
        if(consultaUsuario.buscar(usuario)){
            Sesion.getInstance().iniciarSesion(usuario);
            app.abrirVentana(Paths.ControladorFXML);
            app.cerrarVentana((Stage) btnIngresar.getScene().getWindow());
        }else{
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }
    }
}
