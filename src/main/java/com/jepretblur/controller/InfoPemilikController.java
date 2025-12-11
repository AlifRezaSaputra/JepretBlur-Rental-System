package com.jepretblur.controller;

import com.jepretblur.dao.TransaksiDAO;
import com.jepretblur.model.Transaksi;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;

public class InfoPemilikController {
    @FXML private TableView<Transaksi> tableStatus;
    @FXML private TableColumn<Transaksi, String> colKamera;
    @FXML private TableColumn<Transaksi, String> colPenyewa;
    @FXML private TableColumn<Transaksi, String> colDurasi;

    @FXML
    public void initialize() {
        TransaksiDAO dao = new TransaksiDAO();
        
        colKamera.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKamera().getMerk() + " " + c.getValue().getKamera().getTipe()));
        // Cuma nampilin NAMA penyewa (Privacy terjaga, no HP/Alamat ga dikasih liat)
        colPenyewa.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPelanggan().getNama())); 
        colDurasi.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTglKembali().toString()));

        tableStatus.setItems(dao.getActiveTransactions());
    }

    @FXML
    private void kembali() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
            Stage stage = (Stage) tableStatus.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}