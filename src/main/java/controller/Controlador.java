package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import javafx.util.converter.DefaultStringConverter;
import model.Cabina;

import java.net.URL;
import java.util.ResourceBundle;

public class Controlador implements Initializable {

    @FXML private TableView<Cabina> tablaCabinas;
    @FXML private TableColumn<Cabina, Integer> colNumero;
    @FXML private TableColumn<Cabina, String> colInicio;
    @FXML private TableColumn<Cabina, String> colContador;
    @FXML private TableColumn<Cabina, String> colEstado;
    @FXML private TableColumn<Cabina, String> colParaA;
    @FXML private TableColumn<Cabina, String> colMensaje;

    private ObservableList<Cabina> listaCabinas;
    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tablaCabinas.setEditable(true);

        colNumero.setCellValueFactory(cellData -> cellData.getValue().numeroProperty().asObject());
        colInicio.setCellValueFactory(cellData -> cellData.getValue().inicioProperty());
        colContador.setCellValueFactory(cellData -> cellData.getValue().contadorProperty());
        colEstado.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());
        colMensaje.setCellValueFactory(cellData -> cellData.getValue().mensajeProperty());

        colParaA.setCellValueFactory(cellData -> cellData.getValue().paraAProperty());
        colParaA.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        colParaA.setOnEditCommit(event -> {
            Cabina cabina = event.getRowValue();
            String nuevoParaA = event.getNewValue().trim();
            cabina.setParaA(nuevoParaA);

            int segundos = calcularDiferenciaEnSegundos(cabina.getInicio(), nuevoParaA);
            cabina.setContadorSegundos(segundos);

            if (segundos > 0) {
                cabina.setEstado("Activo");
            } else {
                cabina.setEstado("Desconectado");
            }
        });

        listaCabinas = FXCollections.observableArrayList();
        listaCabinas.add(new Cabina(1, "10:00", 15 * 60, "Activo", "10:15", "Cliente frecuente"));

        tablaCabinas.setItems(listaCabinas);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            for (Cabina cabina : listaCabinas) {
                if (!cabina.isTerminado()) {
                    cabina.restarSegundos(1);
                    if (cabina.isTerminado()) {
                        cabina.setEstado("Desconectado");
                    }
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private int calcularDiferenciaEnSegundos(String inicio, String paraA) {
        try {
            String[] ini = inicio.trim().split(":");
            String[] fin = paraA.trim().split(":");
            if (ini.length == 2 && fin.length == 2) {
                int iniHoras = Integer.parseInt(ini[0]);
                int iniMin = Integer.parseInt(ini[1]);

                int finHoras = Integer.parseInt(fin[0]);
                int finMin = Integer.parseInt(fin[1]);

                int iniTotal = iniHoras * 3600 + iniMin * 60;
                int finTotal = finHoras * 3600 + finMin * 60;

                return Math.max(0, finTotal - iniTotal);
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido en hora: " + e.getMessage());
        }
        return 0;
    }
}
