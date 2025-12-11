# 📸 Jepret Blur - Sistem Informasi Rental Kamera

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-4285F4?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000f?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

**Jepret Blur System** adalah aplikasi desktop berbasis **JavaFX** untuk manajemen penyewaan kamera. Sistem ini dirancang untuk menggantikan pencatatan manual, meminimalisir kesalahan data, dan mempercepat proses transaksi sewa-menyewa kamera.

Project ini menerapkan arsitektur **MVC (Model-View-Controller)**, keamanan password menggunakan **BCrypt**, dan manajemen hak akses (Role-Based Access Control) yang ketat.

---

## 🚀 Fitur Utama

Sistem ini memiliki pembagian hak akses (Role) yang jelas:

### 🛠 Administrator (Admin)
* **Full Access:** Mengelola seluruh operasional sistem.
* **Master Data:** Mengelola Data Kamera (CRUD) dan Data Pelanggan.
* **Manajemen Stok:** Stok kamera otomatis berkurang saat disewa dan bertambah saat kembali.
* **Transaksi:** Memproses peminjaman dan pengembalian.
* **Denda Otomatis:** Sistem otomatis menghitung denda jika pengembalian terlambat.
* **Kelola User:** Menambah akun Admin lain atau Manager.

### 📊 Manager
* **Monitoring:** Fokus pada pengawasan kinerja bisnis.
* **Laporan:** Melihat riwayat transaksi lengkap dan rekapitulasi penyewaan.

### 🤝 Pemilik Unit (Mitra)
* **Titip Sewa:** Mitra dapat login untuk menitipkan kamera mereka ke dalam sistem rental.
* **Monitoring Aset:** Melihat status kamera milik pribadi (sedang dipinjam siapa dan sampai kapan).
* **Keamanan:** Tidak bisa melihat data keuangan atau data kamera milik orang lain.

---

## 🛠 Teknologi yang Digunakan
* **Bahasa Pemrograman:** Java (JDK 21)
* **Framework UI:** JavaFX
* **Build Tool:** Apache Maven
* **Database:** MySQL
* **Libraries:**
    * `mysql-connector-j` (Koneksi Database)
    * `jbcrypt` (Keamanan Password)
    * `lombok` (Boilerplate code reduction)

---

## 📸 Screenshots

---

## 🔐 Akun Demo (Default)
Role: admin
Username: Riokecap
Password:123123123

---

## 📄 Referensi
Project ini dikembangkan berdasarkan studi kasus pada jurnal: Kurniawan, D., Handayani, T., & Sa'diah, H. (2024). Perancangan Sistem Informasi Rental Kamera Berbasis Java Netbeans di Jepret Blur Depok. Jurnal Riset dan Aplikasi Mahasiswa Informatika (JRAMI).

---

## ⚙️ Cara Instalasi & Menjalankan

Ikuti langkah ini untuk menjalankan aplikasi di komputer lokal:

### 1. Prasyarat
* Java JDK 17 atau 21 terinstall.
* Maven terinstall.
* XAMPP (untuk MySQL Database).

### 2. Setup Database
1.  Nyalakan **Apache** dan **MySQL** di XAMPP.
2.  Buka `http://localhost/phpmyadmin`.
3.  Buat database baru dengan nama: **`db_jepretblur`**.
4.  Import file SQL yang ada di dalam repository ini: `db_jepretblur_final.sql`.

### 3. Clone & Run
Buka terminal/CMD, lalu jalankan perintah berikut:

```bash
# Clone repository
git clone [https://github.com/USERNAME_LO/JepretBlur-Rental-System.git](https://github.com/USERNAME_LO/JepretBlur-Rental-System.git)

# Masuk ke folder project
cd JepretBlur-Rental-System

# Jalankan aplikasi menggunakan Maven
mvn clean javafx:run
