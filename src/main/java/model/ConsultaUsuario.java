package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultaUsuario extends Conexion implements Consulta<Usuario> {
    public ConsultaUsuario() {
    }
    @Override
    public boolean registrar(Usuario usuario) {
        return false;
    }
    @Override
    public boolean modificar(Usuario usuario) {
        return false;
    }
    @Override
    public boolean eliminar(Usuario usuario) {
        return false;
    }

    @Override
    public boolean buscar(Usuario usuario){
        String sql="SELECT * FROM usuario WHERE usuario = ? AND contrasena = ?";
        try(Connection con = getConexion(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContrasena());
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    usuario.setUsuario(rs.getString("usuario"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setNombreApellido(rs.getString("nombreApellido"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setDNI(rs.getString("DNI"));
                    usuario.setAdmin(rs.getBoolean("isAdmin"));
                    return true;
                }
                return false;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
