/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package warpay.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import warpay.data.AccountData;

/**
 *
 * @author irkham
 */
public class Extension {

    public static void addPlaceholderStyle(JTextField field) {
        Font font = field.getFont();
        font = font.deriveFont(Font.ITALIC);
        field.setFont(font);
        field.setForeground(Color.gray);
    }

    public static void removePlaceholderStyle(JTextField field) {
        Font font = field.getFont();
        font = font.deriveFont(Font.PLAIN);
        field.setFont(font);
        field.setForeground(Color.black);
    }

    public static boolean isPassCorrect(String pass) {
        try (PreparedStatement pst = Conn.getConnection().prepareStatement("SELECT * FROM account_customers WHERE account_id = ? AND customer_id = ? AND passcode = MD5(?)")) {
            pst.setInt(1, AccountData.getInstance().getAccountId());
            pst.setInt(2, AccountData.getInstance().getCustomerId());
            pst.setString(3, pass);

            ResultSet res = pst.executeQuery();
            if (res.next()) {
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Your passcode are incorrect, please check it carefully");
        }
        return false;
    }

    public static java.sql.Date getCurrentDate() throws ParseException {
        String localDate = java.time.LocalDate.now().toString();
        java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(localDate);
        java.sql.Date currentDate = new java.sql.Date(date.getTime());

        return currentDate;
    }

    public static ResultSet getAccountBalance(int id) throws SQLException {
        PreparedStatement pst = Conn.getConnection().prepareStatement("SELECT account_balance FROM accounts WHERE account_id = ?");

        pst.setInt(1, id);

        ResultSet res = pst.executeQuery();

        return res;
    }

    public static int insertTransactionDetails(String type, double amount) throws SQLException, ParseException {
        PreparedStatement pst = Conn.getConnection().prepareStatement("INSERT INTO transactions (transaction_type, amount, date, customer_id) VALUES (?, ?, ?, ?)");

        pst.setString(1, type);
        pst.setDouble(2, amount);
        pst.setDate(3, getCurrentDate());
        pst.setInt(4, AccountData.getInstance().getCustomerId());

        return pst.executeUpdate();
    }

    public static int updateAccountBalance(double newBalance) throws SQLException {
        PreparedStatement pst = Conn.getConnection().prepareStatement("UPDATE accounts SET account_balance = ? WHERE account_id = ?");

        pst.setDouble(1, newBalance);
        pst.setInt(2, AccountData.getInstance().getAccountId());

        return pst.executeUpdate();
    }

    public static int updateAccountBalance(int id, double newBalance) throws SQLException {
        PreparedStatement pst = Conn.getConnection().prepareStatement("UPDATE accounts SET account_balance = ? WHERE account_id = ?");

        pst.setDouble(1, newBalance);
        pst.setInt(2, id);

        return pst.executeUpdate();
    }

    public static double addBalance(double amount) {
        return AccountData.getInstance().getBalance() + amount;
    }

    public static double reduceBalance(double amount) {
        return AccountData.getInstance().getBalance() - amount;
    }

    public static double addBalance(double balance, double amount) {
        return balance + amount;
    }

    public static double reduceBalance(double balance, double amount) {
        return balance - amount;
    }
    
    public static ImageIcon smoothIcon(URL uri, int x, int y) {
        ImageIcon icon = new ImageIcon(uri);
        Image img = icon.getImage();
        img = img.getScaledInstance(x, y, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    public static Image getFrameIcon(URL uri) {
        ImageIcon icon = new ImageIcon(uri);
        Image img = icon.getImage();
        img = img.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        return img;
    }

    public static void showAccountBalance(JTextField field) {
        double balance = AccountData.getInstance().getBalance();
        field.setText(String.format("%.2f", balance));
    }

    public static void showAccountId(JTextField field) {
        int id = AccountData.getInstance().getAccountId();
        field.setText(String.valueOf(id));
    }
}
