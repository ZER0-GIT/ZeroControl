package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javax.swing.JOptionPane;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import javafx.util.converter.DefaultStringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import model.Cabina;
import utils.Config;

import java.net.URL;
import java.util.ResourceBundle;

public class Controlador implements Initializable {

    @FXML private TableView<Cabina> tablaCabinas;
    @FXML private TableColumn<Cabina, Integer> colNumero;
    @FXML private TableColumn<Cabina, String> colInicio;
    @FXML private TableColumn<Cabina, String> colContador;
    @FXML private TableColumn<Cabina, String> colEstado;
    @FXML private TableColumn<Cabina, String> colMonto;
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
        colMonto.setCellValueFactory(cellData -> cellData.getValue().montoProperty());

        colParaA.setCellValueFactory(cellData -> cellData.getValue().paraAProperty());
        colParaA.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        colParaA.setOnEditCommit(event -> {
            Cabina cabina = event.getRowValue();
            LocalTime horaInicio=LocalTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
            cabina.setInicio(horaInicio.format(formato));
            String nuevoParaA = event.getNewValue().trim();
            try {
                LocalTime hora = LocalTime.parse(nuevoParaA);
                nuevoParaA = hora.format(formato);
                cabina.setParaA(nuevoParaA);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora inválido: " + nuevoParaA);
                cabina.setParaA("00:00:00");
            }
            int segundos = calcularDiferenciaEnSegundos(cabina.getInicio(), nuevoParaA);
            cabina.setContadorSegundos(segundos);
            cabina.setMonto(calcularImporte(segundos));

            if (segundos > 0) {
                cabina.setEstado("Activo");
            } else {
                JOptionPane.showMessageDialog(null, "Hora acabada", "La hora de la PC X", JOptionPane.INFORMATION_MESSAGE);
                cabina.setEstado("Desconectado");
            }
        });

        listaCabinas = FXCollections.observableArrayList();
        for (int i = 1; i <= Config.cantPc; i++) {
            listaCabinas.add(new Cabina(i,"",0,"Desconectado","","",""));
        }
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
    private double calcularImporte(int segTotales){ return (double) Math.round((segTotales/60)*Config.precioMin*10)/10; }
    private int calcularDiferenciaEnSegundos(String inicio, String paraA) {
        try {
            String[] ini = inicio.trim().split(":");
            String[] fin = paraA.trim().split(":");

            if (ini.length == 3 && fin.length == 3) {
                int iniHoras = Integer.parseInt(ini[0]);
                int iniMin = Integer.parseInt(ini[1]);
                int iniSeg = Integer.parseInt(ini[2]);

                int finHoras = Integer.parseInt(fin[0]);
                int finMin = Integer.parseInt(fin[1]);
                int finSeg = Integer.parseInt(fin[2]);

                int iniTotal = iniHoras * 3600 + iniMin * 60 + iniSeg;
                int finTotal = finHoras * 3600 + finMin * 60 + finSeg;

                return Math.max(0, finTotal - iniTotal);
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido en hora: " + e.getMessage());
        }
        return 0;
    }

}
