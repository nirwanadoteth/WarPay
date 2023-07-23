/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package warpay.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author irkham
 */
public class Conn {

    private static Connection conn = null;

    public synchronized static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                FileInputStream fis = new FileInputStream("lib/database.ini");
                Properties props = new Properties();
                props.load(fis);

                String driver = props.getProperty("DBDriver");
                String database = props.getProperty("DBDatabase");
                String user = props.getProperty("DBUsername");
                String pass = props.getProperty("DbPassword");

                Class.forName(driver);
                conn = DriverManager.getConnection(database, user, pass);
            }
        } catch (IOException | ClassNotFoundException | SQLException  e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,
                    e.getMessage(), "Error",
                    JOptionPane.INFORMATION_MESSAGE
            );
            System.exit(0);
        }
        return conn;
    }
}
