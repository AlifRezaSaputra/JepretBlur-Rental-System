package com.jepretblur.controller;

import com.jepretblur.dao.UserDAO;
import com.jepretblur.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField regUsernameField;
    @FXML private PasswordField regPasswordField;
    @FXML private ComboBox<String> roleComboBox;

    private UserDAO userDAO;

    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        
        // SECURITY: Hapus semua role sakti.
        // Cuma izinkan register untuk 'pemilik' (mitra yang nitip kamera)
        roleComboBox.getItems().clear();
        roleComboBox.getItems().add("pemilik");
        
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleRegister() {
        String user = regUsernameField.getText();
        String pass = regPasswordField.getText();
        String role = roleComboBox.getValue();

        // Validasi input
        if (user.isEmpty() || pass.isEmpty() || role == null) {
            showAlert(Alert.AlertType.WARNING, "Data Kurang", "Mohon isi semua data!");
            return;
        }

        // Bikin object User baru
        User newUser = new User();
        newUser.setUsername(user);
        newUser.setPassword(pass); // Password mentah dikirim ke DAO
        newUser.setRole(role);

        // Panggil DAO buat simpan
        if (userDAO.registerUser(newUser)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Registrasi Berhasil! Silakan Login.");
            handleBackToLogin(); // Otomatis balik ke halaman login
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Username mungkin sudah dipakai.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            // Pindah Scene ke Login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/Login.fxml"));
            Stage stage = (Stage) regUsernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}