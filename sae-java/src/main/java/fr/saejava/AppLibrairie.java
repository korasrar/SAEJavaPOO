package fr.saejava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class AppLibrairie {
    public static void main(String[] argv) {
        Properties connConfig = new Properties();
        connConfig.setProperty("maubert", "maubert");
        connConfig.setProperty("maubert", "maubert");

        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/Librairie", connConfig)) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet contact_list = stmt.executeQuery("SELECT , last_name, email FROM test.contacts")) {
                    while (contact_list.next()) {
                        System.out.println(String.format("%s %s <%s>",
                            contact_list.getString("first_name"),
                            contact_list.getString("last_name"),
                            contact_list.getString("email")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
