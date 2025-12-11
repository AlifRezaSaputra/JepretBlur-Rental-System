package com.jepretblur.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaksi {
    private int id;
    private Pelanggan pelanggan; // Kita simpan Object Pelanggan langsung
    private Kamera kamera;       // Kita simpan Object Kamera langsung
    private LocalDate tglSewa;
    private LocalDate tglKembali;
    private double totalHarga;
    private String status;
}