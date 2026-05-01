# 🌱 SiPangan - Food Security Management System

SiPangan adalah aplikasi manajemen ketahanan pangan berbasis web yang dibangun menggunakan **Java Spring Boot**, **Thymeleaf**, dan **MySQL**.

## 📋 Persyaratan Sistem (Prerequisites)

Sebelum menjalankan project ini, pastikan di laptop kamu sudah terinstall perangkat lunak berikut:

1. **Java Development Kit (JDK)** (Disarankan Java 17 atau 21)
2. **Database MySQL** (Bisa menggunakan paket seperti [Laragon](https://laragon.org/) atau [XAMPP](https://www.apachefriends.org/))
3. **IDE / Text Editor** seperti Visual Studio Code (dengan ekstensi Java) atau IntelliJ IDEA.

## 🚀 Panduan Setup & Menjalankan Aplikasi

Ikuti langkah-langkah di bawah ini untuk menjalankan aplikasi di laptop kamu:

### 1. Jalankan Server Database MySQL
Pastikan server MySQL kamu sudah berjalan. Jika kamu menggunakan **Laragon** atau **XAMPP**, klik tombol **Start** pada bagian MySQL.

### 2. Konfigurasi Database
Aplikasi ini sudah diatur agar secara otomatis membuat database bernama `sipangan_db`. Namun, kamu perlu menyesuaikan password MySQL kamu.

1. Buka file konfigurasi di: `src/main/resources/application.yml`
2. Cari bagian konfigurasi `datasource:`
3. Ubah bagian `password:` sesuai dengan password MySQL di laptopmu. Jika kamu menggunakan Laragon/XAMPP dan belum pernah mengubah password MySQL-nya, biarkan kosong.

```yaml
  datasource:
    url: jdbc:mysql://localhost:3306/sipangan_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true
    username: root
    password: isi_dengan_password_mysql_kamu
```

### 3. Menjalankan Aplikasi

**Cara 1: Menggunakan Terminal / Command Prompt (Rekomendasi)**
Buka terminal (Command Prompt / PowerShell) di dalam folder project ini (`sipangan`), lalu jalankan perintah berikut:

Untuk pengguna Windows:
```powershell
.\mvnw spring-boot:run
```
Untuk pengguna Mac / Linux:
```bash
./mvnw spring-boot:run
```
*(Perintah ini akan secara otomatis mendownload Maven dan menjalankan aplikasinya)*

**Cara 2: Menggunakan IDE (VS Code / IntelliJ)**
1. Buka project ini di IDE kamu.
2. Cari file utama: `src/main/java/com/foodtrack/FoodTrackApplication.java`
3. Klik tombol **Run** atau **Play** pada class tersebut.

Tunggu beberapa saat sampai proses *download* (jika baru pertama kali) dan proses *booting* selesai. Kamu akan melihat tulisan `Started FoodTrackApplication` di terminal.

## 🔐 Akun Login Default (Data Seeder)

Saat aplikasi pertama kali dijalankan, sistem akan otomatis membuat tabel dan mengisi beberapa data contoh. Kamu bisa menggunakan akun berikut untuk login:

### 👨‍💼 Akun Admin
* **URL Login:** http://localhost:8080/admin/login
* **Username:** `admin`
* **Password:** `admin123`

### 🌾 Akun Petani
* **URL Login:** http://localhost:8080/login-petani
* **Username:** `budi` | **Password:** `budi123`
* **Username:** `siti` | **Password:** `siti123`
* **Username:** `agus` | **Password:** `agus123`

## 🔗 Link Penting
* **Halaman Utama (Public):** http://localhost:8080
* **Dashboard Admin:** http://localhost:8080/admin/dashboard
* **Dashboard Petani:** http://localhost:8080/petani/dashboard

---
*Happy Coding!* 🚀
