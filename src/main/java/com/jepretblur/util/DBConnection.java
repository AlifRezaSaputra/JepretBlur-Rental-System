package com.jepretblur.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Sesuaikan konfigurasi database lo di sini
    private static final String URL = "jdbc:mysql://localhost:3306/db_jepretblur";
    private static final String USER = "root";     // Default XAMPP/MySQL
    private static final String PASS = " ";         // Kosongin kalau gak ada password

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load driver MySQL (kadang opsional di versi baru, tapi aman dipasang)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Koneksi Gagal: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}