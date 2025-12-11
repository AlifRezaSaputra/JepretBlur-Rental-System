# 📸 Jepret Blur - Sistem Informasi Rental Kamera

Aplikasi desktop untuk manajemen penyewaan kamera, dibangun menggunakan **JavaFX**, **Maven**, dan **MySQL**. Project ini menerapkan arsitektur **MVC (Model-View-Controller)** dan prinsip **OOP**.

## 🚀 Fitur Utama
- **Role-Based Access Control:**
  - 🛠 **Admin:** Kelola Master Data, Transaksi Sewa/Kembali.
  - 📊 **Manager:** Akses Laporan Keuangan.
  - 🤝 **Pemilik:** Titip sewa unit kamera & pantau status peminjaman.
- **Manajemen Stok Otomatis:** Stok berkurang saat disewa, bertambah saat kembali.
- **Perhitungan Denda:** Otomatis menghitung biaya keterlambatan.
- **Keamanan:** Password hashing menggunakan **BCrypt**.

## 🛠 Tech Stack
- **Language:** Java (JDK 17/21)
- **GUI Framework:** JavaFX
- **Build Tool:** Maven
- **Database:** MySQL
- **Libraries:** Lombok, jBCrypt, MySQL Connector

## 📸 Screenshots


## ⚙️ Cara Menjalankan
1. Clone repository ini.
2. Import database `db_jepretblur.sql` ke MySQL/XAMPP.
3. Jalankan perintah:
   ```bash
   mvn clean javafx:run
