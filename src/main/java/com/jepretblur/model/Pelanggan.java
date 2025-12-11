package com.jepretblur.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pelanggan {
    private int id;
    private String noKtp;
    private String nama;
    private String alamat;
    private String noHp;

    @Override
    public String toString() {
        return nama + " (" + noKtp + ")"; // Ini yang bakal muncul di pilihan ComboBox
    }
}