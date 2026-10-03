package com.example.data.model

data class EventComment(
    val id: String,
    val eventId: String,
    val authorName: String,
    val authorHandle: String = "",
    val authorAvatar: String = "",
    val status: String = "hadir", // hadir | tidak_hadir
    val content: String,
    val timeAgo: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class EventAgenda(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val category: String, // "reuni" | "haul" | "kajian" | "korda"
    val description: String,
    val attendeesCount: Int = 0,
    val absentCount: Int = 0,
    val uncertainCount: Int = 0,
    val userRsvp: String? = null, // "hadir" | "belum_pasti" | "tidak_hadir"
    val rsvpNote: String = "",
    val posterResId: Int? = null,
    val authorName: String = "Panitia Reuni Akbar",
    val authorHandle: String = "@panitia_reuni",
    val postedAt: String = "Baru saja",
    val targetAudience: AudienceTarget = AudienceTarget()
)

data class AnnouncementItem(
    val id: String,
    val title: String,
    val date: String,
    val postedAt: String = "Baru saja",
    val category: String = "maklumat", // maklumat | kegiatan | beasiswa | umum
    val content: String,
    val authorName: String,
    val authorHandle: String = "@admin_pusat",
    val authorRole: String = "Pengurus Pondok",
    val isImportant: Boolean = false,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val commentsCount: Int = 0
)

data class AttendanceAttendee(
    val id: String,
    val alumniId: String,
    val alumniName: String,
    val alumniNis: String,
    val city: String = "",
    val gradYear: String = "",
    val checkInTime: String,
    val method: String = "qr" // qr | manual
)

data class AttendanceSession(
    val id: String,
    val title: String,
    val date: String,
    val attendees: List<AttendanceAttendee> = emptyList()
)

data class NotificationItem(
    val id: String,
    val type: String, // reply | system | finance | like
    val title: String,
    val message: String,
    val time: String,
    val read: Boolean = false
)

data class FinanceRecord(
    val id: String,
    val title: String,
    val category: String, // "Infaq", "Iuran Reuni", "Wakaf Bangunan", "Donasi Sosial"
    val amount: Long,
    val date: String,
    val status: String, // "Berhasil", "Menunggu Verifikasi"
    val donorName: String,
    val paymentMethod: String // "QRIS", "Transfer Bank Syariah"
)

data class KitabAyat(
    val number: Int,
    val arabic: String,
    val latin: String,
    val translation: String
)

data class KitabItem(
    val id: String,
    val category: String, // "alquran", "majmuah", "maulid", "aurod"
    val title: String,
    val arabicTitle: String,
    val subtitle: String,
    val totalAyatOrFasal: String,
    val description: String,
    val ayats: List<KitabAyat> = emptyList()
)
