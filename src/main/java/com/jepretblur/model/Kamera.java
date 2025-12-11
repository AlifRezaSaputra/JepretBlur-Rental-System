package com.jepretblur.model;

public class Kamera {
    private int id;
    private String merk;
    private String tipe;
    private double hargaSewa;
    private int stok;
    private int ownerId;      // ID Pemilik
    private String ownerName; // Nama Pemilik (Buat display di tabel Admin)

    public Kamera() {}

    // Constructor Lengkap
    public Kamera(int id, String merk, String tipe, double hargaSewa, int stok, int ownerId, String ownerName) {
        this.id = id;
        this.merk = merk;
        this.tipe = tipe;
        this.hargaSewa = hargaSewa;
        this.stok = stok;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    // Getter & Setter Manual
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMerk() { return merk; }
    public void setMerk(String merk) { this.merk = merk; }

    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }

    public double getHargaSewa() { return hargaSewa; }
    public void setHargaSewa(double hargaSewa) { this.hargaSewa = hargaSewa; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    @Override
    public String toString() {
        return merk + " " + tipe + " - Rp" + (int)hargaSewa;
    }
}