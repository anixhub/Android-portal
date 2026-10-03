package com.example.data.model

data class Coordinates(
    val lat: Double,
    val lng: Double
)

data class AlumniRecord(
    val id: String,
    val nik: String = "",
    val noKk: String = "",
    val nis: String = "",
    val nism: String = "",
    val nisn: String = "",
    val name: String = "",
    val username: String = "",
    val gender: String = "L", // "L" or "P"
    val gradYear: String = "",
    val entryYear: String = "",
    val gradDate: String = "",
    val entryDate: String = "",
    val jenjang: String = "",
    val asramaDulu: String = "",
    val email: String = "",
    val phone: String = "",
    val city: String = "",
    val province: String = "",
    val kecamatan: String = "",
    val desa: String = "",
    val alamatLengkap: String = "",
    val coordinates: Coordinates? = null,
    val occupation: String = "",
    val institution: String = "",
    val password: String = "1234",
    val isPasswordChanged: Boolean = false,
    val source: String = "hostinger_sync",
    val syncTime: String = "",
    val bio: String = "",
    val photoUrl: String = "",
    val coverPhotoUrl: String = "",
    val hasLoggedIn: Boolean = false,
    val shareContact: Boolean = true,
    val shareEmail: Boolean = false,
    val shareFullAddress: Boolean = false,
    val shareLocationTag: Boolean = true,
    val status: String = "alumni", // "alumni" | "santri_aktif" | "admin"
    val tempatLahir: String = "",
    val tanggalLahir: String = "",
    val anakKe: Int = 1,
    val dariBersaudara: Int = 1,
    val namaAyah: String = "",
    val nikAyah: String = "",
    val pekerjaanAyah: String = "",
    val pendidikanAyah: String = "",
    val namaIbu: String = "",
    val nikIbu: String = "",
    val pekerjaanIbu: String = "",
    val pendidikanIbu: String = ""
)

data class AdminUser(
    val id: String = "adm-001",
    val name: String = "Pengurus Pusat Alumni At-taroqqy",
    val username: String = "@admin_pusat",
    val email: String = "admin@attaroqqy.id",
    val phone: String = "081298765432",
    val bio: String = "Akun resmi Pengurus Pusat Ikatan Alumni Pondok Pesantren At-taroqqy.",
    val occupation: String = "Sekretariat Ikatan Alumni",
    val institution: "PP At-taroqqy",
    val city: String = "Rembang",
    val province: String = "Jawa Tengah",
    val role: String = "super_admin",
    val jabatan: String = "Sekretaris Jenderal",
    val avatar: String = "",
    val password: String = "1997"
)

data class AudienceTarget(
    val gender: String = "semua", // "semua" | "L" | "P"
    val regionScope: String = "semua", // "semua" | "khusus"
    val provinceName: String = "",
    val regencyName: String = "",
    val districtName: String = "",
    val villageName: String = ""
)
