# Portal Alumni At-taroqqy (Android Application)

Aplikasi resmi **Portal Alumni Pondok Pesantren At-taroqqy** dibangun dengan **Kotlin**, **Jetpack Compose**, dan **Material Design 3**, menghubungkan para santri lulusan lintas generasi dalam satu ekosistem digital terpadu.

## Arsitektur & Teknologi Android

- **Bahasa & Framework**: Kotlin + Jetpack Compose (Declarative UI)
- **Design System**: Material Design 3 (M3) dengan tema khas Islami (Emerald Green `#065F46` & Amber Gold `#D97706`)
- **Arsitektur**: MVVM (Model-View-ViewModel) + StateFlow Reactive Streams
- **Build System**: Gradle Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`)
- **Package / Namespace**: `com.example`
- **Application ID**: `com.aistudio.portalalumni.kxrthq`
- **Target SDK**: Android 35 (Min SDK 24)

## Fitur Utama yang Disediakan

### 1. Autentikasi & Akun Santri / Admin
- Login role-based: **Alumni Santri** dan **Pengurus / Admin Pondok**
- Sandi bawaan santri baru: `1234`
- Akun cepat uji coba (Demo):
  - Mulia Ningsih, S.Pd. (Alumni MAK 2020)
  - Ahmad Fauzi, S.Kom. (Alumni IPA 2018)
  - Pengurus Pusat (`@admin_pusat`, Sandi: `1997`)

### 2. Beranda & Agenda Temu Alumni
- Sambutan islami *"Ahlan wa Sahlan"* & identitas santri
- Akses cepat Kitab digital (Al-Qur'an, Majmu'ah Syarif, Maulid, Aurod)
- Banner & Agenda **Reuni Akbar XXV & Haul Masyayikh Ke-40**:
  - Konfirmasi kehadiran interaktif (Hadir / Belum Pasti / Berhalangan)
  - Penghitung jumlah hadirin real-time
  - Forum komentar & doa restu alumni

### 3. Direktori Alumni & Peta Sebaran
- Pencarian cerdas berdasarkan Nama, NIS, Kota, Angkatan, dan Profesi
- Filter gender: Ikhwan (Putra) dan Akhwat (Putri)
- Ringkasan peta persebaran alumni di berbagai provinsi di Indonesia

### 4. Kitab Digital & Bacaan Santri
- **Al-Qur'an Digital** (Surat Al-Fatihah, Al-Mulk, Yasin, Al-Waqi'ah)
- **Majmu'ah Syarif** (Doa Kanzul 'Arasy, Shalawat Nariyah)
- **Kitab Maulid** (Maulid Simtudduror - Habib Ali Al-Habsyi, Maulid Ad-Diba'i)
- **Aurod & Ratib** (Ratib Al-Haddad, Ratib Al-Attas)

### 5. Kas & Transparansi Infaq Alumni
- Laporan saldo kas alumni dan target donasi Reuni Akbar
- Simulator pembayaran iuran & infaq digital via **QRIS** dan Transfer Bank Syariah

### 6. Kartu Tanda Alumni Digital (3D Interactive Flip)
- Kartu anggota digital resmi yang dapat diputar 3D (Depan & Belakang)
- Dilengkapi foto santri, NIS, Angkatan, Jenjang pendidikan, dan QR Code verifikasi keaslian

### 7. Panel Khusus Administrator / Pengurus
- Manajemen data alumni: Tambah alumni baru, edit biodata, reset kata sandi ke 1234
- Sistem scanner presensi event (QR scan simulation & manual check-in)
- Publikasi maklumat pondok dengan penargetan audiens
- Statistik komprehensif alumni (rasio gender, sebaran wilayah, dan aktivitas login)
