package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private final String base="zerocontroldb";
    private final String URL="jdbc:mysql://localhost:3306/" + base;
    private final String usuario="root";
    private final String contrasena="";
    private Connection con=null;

    public Connection getConexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, usuario, contrasena);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
