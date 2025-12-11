package com.jepretblur.controller;

import com.jepretblur.dao.UserDAO;
import com.jepretblur.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import com.jepretblur.util.UserSession;

public class MasterKaryawanController {

    @FXML private TableView<User> tableUser;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colUser;
    @FXML private TableColumn<User, String> colRole;

    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private ComboBox<String> comboRole;

    private UserDAO dao;

    @FXML
    public void initialize() {
        dao = new UserDAO();
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Admin bisa bikin akun buat sesama Admin, Manager, atau Karyawan
        comboRole.getItems().addAll("admin", "manager", "karyawan");
        
        loadData();
    }

    private void loadData() {
        tableUser.setItems(dao.getAllStaff());
    }

    @FXML
    private void tambahUser() {
        String user = txtUser.getText();
        String pass = txtPass.getText();
        String role = comboRole.getValue();

        if (user.isEmpty() || pass.isEmpty() || role == null) {
            showAlert("Error", "Data tidak lengkap!");
            return;
        }

        User u = new User();
        u.setUsername(user);
        u.setPassword(pass); // DAO registerUser akan nge-hash ini otomatis
        u.setRole(role);

        if (dao.registerUser(u)) { // Kita reuse method registerUser yg udh ada hash-nya
            showAlert("Sukses", "Akun berhasil dibuat!");
            loadData();
            txtUser.clear(); txtPass.clear();
        } else {
            showAlert("Error", "Gagal (Username mungkin kembar)");
        }
    }

    @FXML
    private void hapusUser() {
        User selected = tableUser.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Pilih user dulu!");
            return;
        }

        // CEGAH HAPUS DIRI SENDIRI
        int myId = UserSession.getSession().getUser().getId();
        if (selected.getId() == myId) {
            showAlert("Dilarang", "Gabisa hapus akun yang lagi login bro!");
            return;
        }
        
        // Konfirmasi Hapus (Opsional biar ga kepencet)
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin hapus user " + selected.getUsername() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.NO) return;

        // Eksekusi Hapus
        if (dao.deleteUser(selected.getId())) {
            showAlert("Sukses", "User berhasil dihapus.");
            loadData();
        } else {
            showAlert("Gagal", "Gagal menghapus user (Cek Database).");
        }
    }

    @FXML
    private void kembali() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
            Stage stage = (Stage) tableUser.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}