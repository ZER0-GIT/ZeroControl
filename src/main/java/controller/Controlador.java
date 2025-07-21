package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javax.swing.JOptionPane;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.util.converter.DefaultStringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import model.Cabina;
import utils.Config;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    @FXML private Button btnCortar;
    @FXML private Button btnHoraLibre;
    @FXML private Button btnMediaHora;
    @FXML private Button btnUnaHora;
    @FXML private Button btnDosHora;
    @FXML private ListView<String> listHistorial;

    private ObservableList<Cabina> listaCabinas;
    private List<String> historialBase = new ArrayList<>();
    private Timeline timeline;
//estudiar estos dos metodos!!!


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
                JOptionPane.showMessageDialog(null, "Formato de hora inválido: " + nuevoParaA+"/n Use el formato 00:00:00" );
                cabina.setParaA("00:00:00");
            }
            int segundos = calcularDiferenciaEnSegundos(cabina.getInicio(), nuevoParaA);
            cabina.setContadorSegundos(segundos);
            int totalSegundos = segundos;
            cabina.setSegundosTotales(totalSegundos);
            cabina.setMonto(cabina.calcularImporte(cabina.getSegundosTotales()));

            if (segundos > 0) {
                cabina.setEstado("Activo");
            } else {
                cabina.setEstado("Desconectado");
            }
        });

        colMensaje.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        colMensaje.setOnEditCommit(event -> {
            Cabina cabina = event.getRowValue();
            String nuevoMensaje = event.getNewValue().trim();
            cabina.setMensaje(nuevoMensaje);
        });


        listaCabinas = FXCollections.observableArrayList();
        for (int i = 1; i <= Config.cantPc; i++) {//Crea las cabinas que hay (cantidad definida en Config)
            listaCabinas.add(new Cabina(i,"",0,"Desconectado","","","0.0"));
        }
        tablaCabinas.setItems(listaCabinas);
        iniciarTiempo();
    }
    private void iniciarTiempo() {
            if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
                return;
            }

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                for (Cabina cabina : listaCabinas) {
                    if (!cabina.isTerminado()) {
                        if (cabina.getEstado().equals("Libre")) {
                            cabina.sumarSegundos(1); // Cuenta progresiva
                        } else {
                            cabina.restarSegundos(1); // Cuenta regresiva
                            if (cabina.isTerminado()) {
                                cortarHora(new ActionEvent());
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Hora concluida",
                                        "La hora de la PC " + cabina.getNumero(),
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                            }
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
    @FXML
    void actionMediaHora(ActionEvent event) {
        agregarTiempoACabinaSeleccionada(30);
    }

    @FXML
    void actionUnaHora(ActionEvent event) {
        agregarTiempoACabinaSeleccionada(60);
    }

    @FXML
    void actionDosHoras(ActionEvent event) {
        agregarTiempoACabinaSeleccionada(120);
    }

    @FXML
    void cortarHora(ActionEvent event) {
        Cabina cabina = tablaCabinas.getSelectionModel().getSelectedItem();
        if (cabina != null) {
            String mensaje = String.format("Cabina %d terminó a las %s - Tiempo: %s seg - Monto: %s",
                    cabina.getNumero(),
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    cabina.getSegundosTotales(),
                    cabina.getMonto());
            historialBase.add(mensaje);
            listHistorial.getItems().setAll(historialBase);
            cabina.setEstado("Desconectado");
            cabina.setInicio("");
            cabina.setMonto(0);
            cabina.setContadorSegundos(0);
            cabina.setParaA("");
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una cabina primero.");
        }
    }

    @FXML
    void actionHoraLibre(ActionEvent event) {
        Cabina cabina = tablaCabinas.getSelectionModel().getSelectedItem();
        if (cabina.getParaA().isEmpty()){
            cabina.setEstado("Libre");
            cabina.setInicio(formatearHora(LocalTime.now()));
            iniciarTiempo();

        }else{
            cortarHora(event);
            actionHoraLibre(event);
        }
    }

    private void agregarTiempoACabinaSeleccionada(int minutos) {
        Cabina cabinaSeleccionada = tablaCabinas.getSelectionModel().getSelectedItem();
        if (cabinaSeleccionada == null) {
            JOptionPane.showMessageDialog(null, "Seleccione una cabina primero.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime horaInicio;
        LocalTime horaFin;
        if (cabinaSeleccionada.getInicio().isEmpty()) {
            horaInicio = LocalTime.now();
            cabinaSeleccionada.setInicio(horaInicio.format(formatter));
            horaFin = horaInicio.plusMinutes(minutos);
        } else {
            horaInicio = LocalTime.parse(cabinaSeleccionada.getInicio(), formatter);

            if (!cabinaSeleccionada.getParaA().isEmpty()) {
                horaFin = LocalTime.parse(cabinaSeleccionada.getParaA(), formatter).plusMinutes(minutos);
            } else {
                horaFin = horaInicio.plusMinutes(minutos);
            }
        }

        cabinaSeleccionada.setParaA(horaFin.format(formatter));

        int segundosTotales = calcularDiferenciaEnSegundos(horaInicio.format(formatter), horaFin.format(formatter));
        cabinaSeleccionada.setSegundosTotales(segundosTotales);
        cabinaSeleccionada.setContadorSegundos(segundosTotales);
        cabinaSeleccionada.setMonto(cabinaSeleccionada.calcularImporte(segundosTotales));
        cabinaSeleccionada.setEstado("Activo");
        iniciarTiempo();
    }


    private String formatearHora(LocalTime hora) {
        return hora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

}
