package com.jepretblur.controller;

import com.jepretblur.dao.KameraDAO;
import com.jepretblur.model.Kamera;
import com.jepretblur.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class InputKameraController {

    @FXML private TextField txtMerk;
    @FXML private TextField txtTipe;
    @FXML private TextField txtStok;

    private KameraDAO dao = new KameraDAO();

    @FXML
    private void simpanKamera() {
        try {
            String merk = txtMerk.getText();
            String tipe = txtTipe.getText();
            int stok = Integer.parseInt(txtStok.getText());
            int ownerId = UserSession.getSession().getUser().getId(); // Ambil ID si Pemilik yg Login

            // Harga diset 0 dulu, nunggu admin
            Kamera k = new Kamera(0, merk, tipe, 0, stok, ownerId, null);
            
            if (dao.addKamera(k)) {
                showAlert("Sukses", "Kamera berhasil diajukan! Tunggu admin menentukan harga.");
                kembali();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Stok harus angka!");
        }
    }

    @FXML
    private void kembali() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
            Stage stage = (Stage) txtMerk.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}