package model;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;

public class Cabina {
    private final IntegerProperty numero;
    private final StringProperty inicio;
    private final IntegerProperty contadorSegundos;
    private final StringBinding contadorFormateado;
    private final StringProperty estado;
    private final StringProperty paraA;
    private final StringProperty mensaje;

    public Cabina(int numero, String inicio, int contadorSegundos, String estado, String paraA, String mensaje) {
        this.numero = new SimpleIntegerProperty(numero);
        this.inicio = new SimpleStringProperty(inicio);
        this.contadorSegundos = new SimpleIntegerProperty(contadorSegundos);
        this.estado = new SimpleStringProperty(estado);
        this.paraA = new SimpleStringProperty(paraA);
        this.mensaje = new SimpleStringProperty(mensaje);

        this.contadorFormateado = new StringBinding() {
            {
                bind(Cabina.this.contadorSegundos);
            }

            @Override
            protected String computeValue() {
                int totalSegundos = Cabina.this.contadorSegundos.get();
                int horas = totalSegundos / 3600;
                int minutos = (totalSegundos % 3600) / 60;
                int segundos = totalSegundos % 60;
                return String.format("%02d:%02d:%02d", horas, minutos, segundos);
            }
        };
    }

    // Getters
    public int getNumero() { return numero.get(); }
    public String getInicio() { return inicio.get(); }
    public int getContadorSegundos() { return contadorSegundos.get(); }
    public String getEstado() { return estado.get(); }
    public String getParaA() { return paraA.get(); }
    public String getMensaje() { return mensaje.get(); }

    // Setters
    public void setInicio(String inicio) { this.inicio.set(inicio); }
    public void setContadorSegundos(int segundos) { this.contadorSegundos.set(segundos); }
    public void setEstado(String estado) { this.estado.set(estado); }
    public void setParaA(String paraA) { this.paraA.set(paraA); }
    public void setMensaje(String mensaje) { this.mensaje.set(mensaje); }

    // Propiedades para TableView
    public IntegerProperty numeroProperty() { return numero; }
    public StringProperty inicioProperty() { return inicio; }
    public IntegerProperty contadorSegundosProperty() { return contadorSegundos; }
    public StringProperty estadoProperty() { return estado; }
    public StringProperty paraAProperty() { return paraA; }
    public StringProperty mensajeProperty() { return mensaje; }

    public StringBinding contadorProperty() {
        return contadorFormateado;
    }

    public void restarSegundos(int segundos) {
        int nuevo = contadorSegundos.get() - segundos;
        if (nuevo < 0) nuevo = 0;
        contadorSegundos.set(nuevo);
    }

    public boolean isTerminado() {
        return contadorSegundos.get() <= 0;
    }
}
