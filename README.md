# Pesanan Dapur - Kitchen Order Management

Aplikasi Android offline untuk mencatat dan mengelola pesanan makanan (sistem PO/Katering).

## Fitur Utama

- **Mode Kasir**: Catat pesanan pelanggan dengan tanggal pengiriman
- **Mode Dapur**: Lihat ringkasan produksi per menu
- **Export PDF**: Bagikan daftar pesanan via WhatsApp/Bluetooth
- **Offline 100%**: Tidak memerlukan internet

## Cara Build

### Prasyarat

1. **Android Studio** (versi Hedgehog atau lebih baru)
   - Download: https://developer.android.com/studio
   
2. **JDK 17** (biasanya sudah termasuk di Android Studio)

### Langkah Build

#### Opsi A: Menggunakan Android Studio (Recommended)

1. Buka Android Studio
2. Pilih **File > Open** lalu pilih folder `MakE`
3. Tunggu Gradle sync selesai (biasanya 2-5 menit pertama kali)
4. Klik **Build > Build Bundle(s) / APK(s) > Build APK(s)**
5. APK akan tersedia di: `app/build/outputs/apk/debug/app-debug.apk`

#### Opsi B: Menggunakan Command Line

```bash
# Masuk ke folder project
cd /path/to/MakE

# Download Gradle wrapper (jika belum ada gradle-wrapper.jar)
# Bisa skip jika sudah punya Android Studio

# Build debug APK
./gradlew assembleDebug

# APK tersedia di:
# app/build/outputs/apk/debug/app-debug.apk
```

### Install ke HP

1. **Aktifkan "Unknown Sources"** di HP:
   - Buka Settings > Security > Unknown Sources (ON)
   - Atau Settings > Apps > Special Access > Install Unknown Apps
   
2. **Transfer APK** ke HP via:
   - Kabel USB
   - Google Drive
   - WhatsApp (kirim ke diri sendiri)
   
3. **Tap file APK** untuk install

## Struktur Aplikasi

```
app/src/main/java/com/enigma/kitchenorders/
├── data/
│   ├── local/
│   │   ├── dao/          # Room DAO (database queries)
│   │   ├── entity/       # Database entities
│   │   └── AppDatabase.kt
│   └── repository/       # Repository implementation
├── domain/
│   └── repository/       # Repository interface
├── ui/
│   ├── screens/          # Compose UI screens
│   ├── viewmodel/        # ViewModels
│   ├── theme/            # Colors & Theme
│   └── navigation/       # Navigation routes
├── util/                 # PDF Export utility
├── KitchenApp.kt         # Application class
└── MainActivity.kt       # Entry point
```

## Penggunaan

### Pertama Kali

1. Buka aplikasi
2. Tap **Settings** (icon gear)
3. Ubah nama bisnis
4. Tambah menu-menu yang dijual

### Mencatat Pesanan (Kasir)

1. Dari Home, tap **KASIR**
2. Pilih tanggal pengiriman
3. Isi nama pelanggan
4. Tap **Tambah Item**, pilih menu, quantity, notes
5. Tap **SIMPAN PESANAN**

### Melihat Ringkasan Dapur

1. Dari Home, tap **DAPUR**
2. Pilih tanggal yang ingin dilihat
3. Tap card menu untuk melihat detail customer
4. Gunakan filter untuk cari catatan khusus (misal: "pedas")
5. Tap icon Share untuk export PDF

## Lisensi

Private use - Dibuat untuk kebutuhan internal UMKM.
