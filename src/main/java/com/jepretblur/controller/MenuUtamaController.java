package com.jepretblur.controller;

import com.jepretblur.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuUtamaController {

    @FXML private Label welcomeLabel;
    
    // Inject Tombol
    @FXML private Button btnKamera;
    @FXML private Button btnPelanggan;
    @FXML private Button btnSewa;
    @FXML private Button btnKembali;
    @FXML private Button btnLaporan;
    @FXML private Button btnInfoPemilik;
    @FXML private Button btnInputUnit;
    @FXML private Button btnKaryawan;

    @FXML
    public void initialize() {
        if (UserSession.getSession() != null) {
            String role = UserSession.getSession().getUser().getRole();
            String username = UserSession.getSession().getUser().getUsername();
            welcomeLabel.setText("Halo, " + username + "\n(" + role + ")");
            
            // JALANKAN LOGIKA HAK AKSES
            applyAccessControl(role);
        }
    }

    private void applyAccessControl(String role) {
        // 1. Reset: Sembunyiin SEMUA tombol dulu biar bersih
        setButtonVisible(btnKamera, false);
        setButtonVisible(btnPelanggan, false);
        setButtonVisible(btnSewa, false);
        setButtonVisible(btnKembali, false);
        setButtonVisible(btnLaporan, false);
        setButtonVisible(btnInfoPemilik, false);
        setButtonVisible(btnInputUnit, false); // Jangan lupa sembunyiin ini juga defaultnya
        setButtonVisible(btnKaryawan, false);

        // 2. Tampilkan sesuai Role
        if (role.equalsIgnoreCase("admin")) {
            // ADMIN: Full Control
            setButtonVisible(btnKamera, true);
            setButtonVisible(btnPelanggan, true);
            setButtonVisible(btnSewa, true);
            setButtonVisible(btnKembali, true);
            setButtonVisible(btnLaporan, true);
            setButtonVisible(btnKaryawan, true);
        } else if (role.equalsIgnoreCase("manager")) {
            // MANAGER: Cuma Laporan
            setButtonVisible(btnLaporan, true);
            setButtonVisible(btnKaryawan, true);
        } else if (role.equalsIgnoreCase("pemilik")) {
            // PEMILIK: Info Status & Input Unit (GABUNG DISINI)
            setButtonVisible(btnInfoPemilik, true);
            setButtonVisible(btnInputUnit, true);
        }
    }

    private void setButtonVisible(Button btn, boolean visible) {
        btn.setVisible(visible);
        btn.setManaged(visible);
    }

    // --- NAVIGASI ---

    @FXML private void showMasterKamera() { 
        nav("/com/jepretblur/view/MasterKamera.fxml"); }
    @FXML private void showMasterPelanggan() { 
        nav("/com/jepretblur/view/MasterPelanggan.fxml"); }
    @FXML private void showTransaksi() { 
        nav("/com/jepretblur/view/TransaksiSewa.fxml"); }
    @FXML private void showPengembalian() { 
        nav("/com/jepretblur/view/TransaksiKembali.fxml"); }
    @FXML private void showLaporan() { 
        nav("/com/jepretblur/view/Laporan.fxml"); }
    @FXML private void showMasterKaryawan() { 
        nav("/com/jepretblur/view/MasterKaryawan.fxml"); 
    }

    @FXML 
    private void showInfoPemilik() { 
        nav("/com/jepretblur/view/InfoPemilik.fxml"); 
    }

    @FXML 
    private void showInputUnit() { 
        nav("/com/jepretblur/view/InputKamera.fxml"); 
    }
    
    @FXML
    private void handleLogout() {
        try {
            UserSession.cleanSession();
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/Login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private void nav(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}