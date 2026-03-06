# Pesanan Dapur - Kitchen Order Management

Aplikasi Android offline untuk mencatat dan mengelola pesanan makanan (sistem PO/Katering).

## Fitur Utama

- **Mode Kasir**: Catat pesanan pelanggan dengan tanggal pengiriman
- **Mode Dapur**: Lihat ringkasan produksi per menu
- **Export PDF**: Bagikan daftar pesanan via WhatsApp/Bluetooth
- **Offline 100%**: Tidak memerlukan internet

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


Private use - Dibuat untuk kebutuhan internal UMKM.
