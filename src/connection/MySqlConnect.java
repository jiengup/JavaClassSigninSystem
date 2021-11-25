package connection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnect {
    Connection conn = null;
    public static Connection connectDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://cdb-7b4hqwgw.cd.tencentcdb.com:10082/sys?useUnicode=true&characterEncoding=utf-8", "root", "imagoodboy456");
            System.out.println("Connected to database");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}
