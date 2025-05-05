package fr.saejava;

import java.sql.*;
import java.util.Properties;

public class AppLibrairie {
    // Connection avec JDBC 
    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        Connection c;
        System.out.println("Tentative de connection...");
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            c=DriverManager.getConnection(
            "jdbc:mysql://servinfo-maria:3306/Librairie",
            "maubert","maubert");
            System.out.println("Connection réussi");
            } catch (SQLException ex){
                System.out.println("Msg:"+ex.getMessage()+
                ex.getErrorCode());
            }
            catch (ClassNotFoundException ex){
                System.out.println("Driver pas trouvé");
            }
    }

    // Application avec menu 
}
