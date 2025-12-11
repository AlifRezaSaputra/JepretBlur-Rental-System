package com.jepretblur.controller;

import java.io.IOException;

import com.jepretblur.dao.UserDAO;
import com.jepretblur.model.User;
import com.jepretblur.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    
    @FXML
    private Label errorLabel;

    private UserDAO userDAO;

    // Method ini otomatis dipanggil saat file FXML di-load
    @FXML
    public void initialize() {
        userDAO = new UserDAO();
    }

    // Method yang dipanggil saat tombol MASUK diklik
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 1. Validasi Input Kosong
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Username dan Password tidak boleh kosong!");
            return;
        }

        // 2. Cek ke Database via DAO
        User user = userDAO.validateLogin(username, password);

        if (user != null) {
            // 1. Simpan user ke session
            UserSession.setSession(user);

            // 2. Pindah ke Menu Utama
            try {
                // Load FXML Menu Utama
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
                Parent root = loader.load();
                
                // Ambil Stage (Jendela) saat ini & ganti Scene-nya
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.centerOnScreen(); // Biar pas di tengah layar
                
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat Menu Utama!");
            }
            
        } else {
            // Code Login Gagal tetep sama kayak sebelumnya
            showAlert(Alert.AlertType.ERROR, "Gagal", "Username atau Password salah!");
            passwordField.clear();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void switchToRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/Register.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}