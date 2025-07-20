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
    main app = new main(); //crea un objeto de la clase main con todos sus metodos abrir y cerrar ventanas

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
        usuario.setUsuario(txtUsuario.getText()); // guarda el usuario ingresado
        usuario.setContrasena(pwdContrasena.getText());//guarda el password ingresado
        if(consultaUsuario.buscar(usuario)){ // llama al metodo para buscar si el usuario y clave con correctos
            Sesion.getInstance().iniciarSesion(usuario);// si es correcto inicia sesion
            app.abrirVentana(Paths.ControladorFXML); //abre la ventaba Controlador
            app.cerrarVentana((Stage) btnIngresar.getScene().getWindow());//“Cierra la ventana donde está el botón ‘Ingresar’.”
        }else{
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }//mensaje de error si es incorrecto los datos
    }
}
