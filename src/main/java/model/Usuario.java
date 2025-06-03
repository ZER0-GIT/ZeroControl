package model;

public class Usuario {
    private String usuario;
    private String contrasena;
    private String NombreApellido;
    private String correo;
    private String telefono;
    private String DNI;
    private boolean isAdmin;

    public Usuario(String usuario, String contrasena, String NombreApellido, String correo, String telefono, String DNI) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.NombreApellido = NombreApellido;
        this.correo = correo;
        this.telefono = telefono;
        this.DNI = DNI;
        this.isAdmin = false;
    }

    public Usuario() {}

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreApellido() {
        return NombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        NombreApellido = nombreApellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
