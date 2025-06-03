package model;

public class Sesion {
    private static Sesion instance;
    private Usuario usuario;

    private Sesion() {}

    public static Sesion getInstance() {
        if (instance == null) {
            instance = new Sesion();
        }
        return instance;
    }
    public void iniciarSesion(Usuario usuario) {
        this.usuario = usuario;
    }
    public void cerrarSesion() {
        this.usuario = null;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public boolean isLoggedIn() {
        return usuario != null;
    }
    public String getUsuarioNombre() {
        return usuario != null ? usuario.getUsuario() : null;
    }
}
