package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlumniRepository {

    private val _alumniList = MutableStateFlow<List<AlumniRecord>>(createInitialAlumni())
    val alumniList: StateFlow<List<AlumniRecord>> = _alumniList.asStateFlow()

    private val _adminUser = MutableStateFlow(
        AdminUser(
            id = "adm-001",
            name = "Pengurus Pusat Alumni At-taroqqy",
            username = "@admin_pusat",
            email = "admin@attaroqqy.id",
            phone = "081298765432",
            bio = "Akun resmi Pengurus Pusat Ikatan Alumni Pondok Pesantren At-taroqqy.",
            occupation = "Sekretariat Ikatan Alumni",
            institution = "PP At-taroqqy Rembang",
            city = "Rembang",
            province = "Jawa Tengah",
            role = "super_admin",
            jabatan = "Sekretaris Jenderal",
            password = "1997"
        )
    )
    val adminUser: StateFlow<AdminUser> = _adminUser.asStateFlow()

    private val _events = MutableStateFlow<List<EventAgenda>>(createInitialEvents())
    val events: StateFlow<List<EventAgenda>> = _events.asStateFlow()

    private val _announcements = MutableStateFlow<List<AnnouncementItem>>(createInitialAnnouncements())
    val announcements: StateFlow<List<AnnouncementItem>> = _announcements.asStateFlow()

    private val _eventComments = MutableStateFlow<List<EventComment>>(createInitialComments())
    val eventComments: StateFlow<List<EventComment>> = _eventComments.asStateFlow()

    private val _attendanceSessions = MutableStateFlow<List<AttendanceSession>>(createInitialAttendance())
    val attendanceSessions: StateFlow<List<AttendanceSession>> = _attendanceSessions.asStateFlow()

    private val _financeRecords = MutableStateFlow<List<FinanceRecord>>(createInitialFinance())
    val financeRecords: StateFlow<List<FinanceRecord>> = _financeRecords.asStateFlow()

    private val _kitabList = MutableStateFlow<List<KitabItem>>(createInitialKitabs())
    val kitabList: StateFlow<List<KitabItem>> = _kitabList.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(createInitialNotifications())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun updateAlumni(updated: AlumniRecord) {
        _alumniList.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    fun addAlumni(newAlumni: AlumniRecord) {
        _alumniList.update { listOf(newAlumni) + it }
    }

    fun deleteAlumni(id: String) {
        _alumniList.update { list -> list.filterNot { it.id == id } }
    }

    fun resetAlumniPassword(id: String): Boolean {
        _alumniList.update { list ->
            list.map {
                if (it.id == id) it.copy(password = "1234", isPasswordChanged = false) else it
            }
        }
        return true
    }

    fun rsvpEvent(eventId: String, rsvpStatus: String, note: String) {
        _events.update { list ->
            list.map { event ->
                if (event.id == eventId) {
                    val prevStatus = event.userRsvp
                    var newAttendees = event.attendeesCount
                    var newUncertain = event.uncertainCount
                    var newAbsent = event.absentCount

                    if (prevStatus == "hadir") newAttendees = (newAttendees - 1).coerceAtLeast(0)
                    if (prevStatus == "belum_pasti") newUncertain = (newUncertain - 1).coerceAtLeast(0)
                    if (prevStatus == "tidak_hadir") newAbsent = (newAbsent - 1).coerceAtLeast(0)

                    when (rsvpStatus) {
                        "hadir" -> newAttendees += 1
                        "belum_pasti" -> newUncertain += 1
                        "tidak_hadir" -> newAbsent += 1
                    }

                    event.copy(
                        userRsvp = rsvpStatus,
                        rsvpNote = note,
                        attendeesCount = newAttendees,
                        uncertainCount = newUncertain,
                        absentCount = newAbsent
                    )
                } else event
            }
        }
    }

    fun addEventComment(eventId: String, authorName: String, authorHandle: String, status: String, content: String) {
        val newComment = EventComment(
            id = "comm-${System.currentTimeMillis()}",
            eventId = eventId,
            authorName = authorName,
            authorHandle = authorHandle,
            status = status,
            content = content,
            timeAgo = "Baru saja",
            likesCount = 0,
            isLiked = false
        )
        _eventComments.update { listOf(newComment) + it }
    }

    fun toggleCommentLike(commentId: String) {
        _eventComments.update { list ->
            list.map { c ->
                if (c.id == commentId) {
                    val newLiked = !c.isLiked
                    val newCount = if (newLiked) c.likesCount + 1 else (c.likesCount - 1).coerceAtLeast(0)
                    c.copy(isLiked = newLiked, likesCount = newCount)
                } else c
            }
        }
    }

    fun addAnnouncement(ann: AnnouncementItem) {
        _announcements.update { listOf(ann) + it }
    }

    fun toggleAnnouncementLike(annId: String) {
        _announcements.update { list ->
            list.map { a ->
                if (a.id == annId) {
                    val newLiked = !a.isLiked
                    val newCount = if (newLiked) a.likesCount + 1 else (a.likesCount - 1).coerceAtLeast(0)
                    a.copy(isLiked = newLiked, likesCount = newCount)
                } else a
            }
        }
    }

    fun addAttendanceAttendee(sessionId: String, attendee: AttendanceAttendee) {
        _attendanceSessions.update { sessions ->
            sessions.map { s ->
                if (s.id == sessionId) {
                    s.copy(attendees = listOf(attendee) + s.attendees)
                } else s
            }
        }
    }

    fun addFinanceDonation(record: FinanceRecord) {
        _financeRecords.update { listOf(record) + it }
    }

    private fun createInitialAlumni(): List<AlumniRecord> {
        return listOf(
            AlumniRecord(
                id = "alm-001",
                nik = "3507123456780001",
                noKk = "3507123456780000",
                nis = "TRQ-2017-089",
                nism = "131233170001",
                nisn = "0012345678",
                name = "Mulia Ningsih, S.Pd.",
                username = "mulia_ningsih",
                gender = "P",
                tempatLahir = "Rembang",
                tanggalLahir = "1998-05-14",
                anakKe = 2,
                dariBersaudara = 5,
                gradYear = "2020",
                entryYear = "2014",
                entryDate = "2014-07-15",
                gradDate = "2020-06-20",
                jenjang = "Madrasah Aliyah Keagamaan (MAK)",
                asramaDulu = "Komplek Khodijah Lt. 2",
                email = "mulia.ningsih@alumni.attaroqqy.id",
                phone = "081234567890",
                city = "Rembang",
                province = "Jawa Tengah",
                kecamatan = "Sedan",
                desa = "Karas",
                alamatLengkap = "Jl. Karas No. 12, RT 02 / RW 01",
                coordinates = Coordinates(-6.7423, 111.4589),
                occupation = "Guru Bahasa Arab & Penulis",
                institution = "MAN 1 Rembang",
                namaAyah = "H. Abdul Rasyid",
                pekerjaanAyah = "Wiraswasta / Petani",
                namaIbu = "Hj. Siti Maryam",
                pekerjaanIbu = "Ibu Rumah Tangga",
                password = "1234",
                isPasswordChanged = false,
                hasLoggedIn = false,
                bio = "Alumni MAK angkatan 2020. Khidmah pada pendidikan literasi dan bahasa Arab santri putri.",
                photoUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=256",
                shareContact = true,
                shareLocationTag = true,
                status = "alumni"
            ),
            AlumniRecord(
                id = "alm-002",
                nik = "3507123456780002",
                nis = "TRQ-2015-045",
                name = "Ahmad Fauzi, S.Kom.",
                username = "fauzi_trq",
                gender = "L",
                tempatLahir = "Surabaya",
                tanggalLahir = "1996-08-21",
                anakKe = 1,
                dariBersaudara = 3,
                gradYear = "2018",
                entryYear = "2012",
                jenjang = "Madrasah Aliyah Jurusan IPA",
                asramaDulu = "Komplek Al-Ghazali No. 14",
                email = "ahmad.fauzi@techindonesia.co.id",
                phone = "085712345678",
                city = "Surabaya",
                province = "Jawa Timur",
                kecamatan = "Gubeng",
                desa = "Airlangga",
                alamatLengkap = "Jl. Dharmawangsa Barat No. 88",
                coordinates = Coordinates(-7.2575, 112.7521),
                occupation = "Software Engineer",
                institution = "PT Solusi Digital Nusantara",
                namaAyah = "Drs. H. Mulyono",
                pekerjaanAyah = "PNS",
                namaIbu = "Hj. Nurul Hidayah",
                pekerjaanIbu = "Dosen",
                password = "passwordfauzi",
                isPasswordChanged = true,
                hasLoggedIn = true,
                bio = "Pegiat teknologi informasi dan digitalisasi sistem pondok pesantren nusantara.",
                photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=256",
                shareContact = true,
                shareLocationTag = true,
                status = "alumni"
            ),
            AlumniRecord(
                id = "alm-003",
                nik = "3507123456780003",
                nis = "TRQ-2019-112",
                name = "Muhammad Ridwan, S.H.",
                username = "ridwan_law",
                gender = "L",
                tempatLahir = "Jakarta",
                tanggalLahir = "2000-11-05",
                anakKe = 3,
                dariBersaudara = 4,
                gradYear = "2022",
                entryYear = "2016",
                jenjang = "Kulliyatul Mu'allimin Al-Islamiyyah",
                asramaDulu = "Komplek Ibnu Rusyd Lt. 1",
                email = "m.ridwan@advokat.id",
                phone = "081398765432",
                city = "Jakarta Selatan",
                province = "DKI Jakarta",
                kecamatan = "Kebayoran Baru",
                desa = "Senayan",
                alamatLengkap = "Jl. Senopati Dalam No. 19",
                coordinates = Coordinates(-6.2415, 106.7992),
                occupation = "Legal Consultant",
                institution = "Ridwan & Partners Law Office",
                password = "1234",
                isPasswordChanged = false,
                hasLoggedIn = false,
                bio = "Alumni KMI 2022. Konsultan hukum syariah & perdata bisnis.",
                photoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=256",
                shareContact = true,
                shareLocationTag = true,
                status = "alumni"
            ),
            AlumniRecord(
                id = "alm-004",
                nik = "3507123456780004",
                nis = "TRQ-2016-033",
                name = "Dr. Hj. Siti Fatimah, M.Ag.",
                username = "fatimah_dosen",
                gender = "P",
                tempatLahir = "Kudus",
                tanggalLahir = "1994-03-12",
                gradYear = "2015",
                entryYear = "2009",
                jenjang = "Madrasah Aliyah Keagamaan (MAK)",
                asramaDulu = "Komplek Aisyah Lt. 3",
                email = "siti.fatimah@iainkudus.ac.id",
                phone = "081987654321",
                city = "Kudus",
                province = "Jawa Tengah",
                kecamatan = "Kota Kudus",
                desa = "Kauman",
                alamatLengkap = "Jl. Menara Kudus No. 45",
                coordinates = Coordinates(-6.8048, 110.8405),
                occupation = "Dosen Ushuluddin",
                institution = "IAIN Kudus",
                password = "1234",
                isPasswordChanged = false,
                hasLoggedIn = true,
                bio = "Peneliti naskah kuno pesantren & dosen tafsir hadits.",
                photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&q=80&w=256",
                shareContact = true,
                shareLocationTag = true,
                status = "alumni"
            ),
            AlumniRecord(
                id = "alm-005",
                nik = "3507123456780005",
                nis = "TRQ-2018-099",
                name = "K.H. Abdullah Hasan",
                username = "gus_hasan",
                gender = "L",
                tempatLahir = "Tuban",
                tanggalLahir = "1997-09-02",
                gradYear = "2019",
                entryYear = "2013",
                jenjang = "Madrasah Diniyah Salafiyah Ulya",
                asramaDulu = "Komplek Darussalam No. 01",
                email = "gus.hasan@pesantren.org",
                phone = "082134567899",
                city = "Tuban",
                province = "Jawa Timur",
                kecamatan = "Palang",
                desa = "Tasikmadu",
                alamatLengkap = "Komplek PP Al-Ikhlas Tasikmadu",
                coordinates = Coordinates(-6.8981, 112.0645),
                occupation = "Pengasuh Pondok Pesantren",
                institution = "PP Al-Ikhlas Tuban",
                password = "1234",
                isPasswordChanged = true,
                hasLoggedIn = true,
                bio = "Khadimul ma'had Al-Ikhlas Tuban. Melanjutkan sanad ilmu salaf At-taroqqy.",
                photoUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&q=80&w=256",
                shareContact = true,
                shareLocationTag = true,
                status = "alumni"
            )
        )
    }

    private fun createInitialEvents(): List<EventAgenda> {
        return listOf(
            EventAgenda(
                id = "ev-001",
                title = "Reuni Akbar XXV & Haul Masyayikh Ke-40",
                date = "15 Agustus 2026",
                time = "08:00 - 15:30 WIB",
                location = "Halaman Utama Ponpes At-taroqqy, Rembang",
                category = "reuni",
                description = "Temu kangen seluruh alumni lintas angkatan dari tahun 1990 hingga 2025. Dilanjutkan Tahlil Akbar dan Mau'idhoh Hasanah oleh para Ulama alumni dan Masyayikh Pondok Pesantren At-taroqqy.",
                attendeesCount = 384,
                uncertainCount = 42,
                absentCount = 18,
                userRsvp = "hadir",
                rsvpNote = "Insya Allah hadir membawa keluarga",
                posterResId = R.drawable.alumni_illustration,
                authorName = "Pengurus Pusat Alumni At-taroqqy",
                authorHandle = "@admin_pusat",
                postedAt = "2 hari yang lalu"
            ),
            EventAgenda(
                id = "ev-002",
                title = "Kajian Kitab Ihya' Ulumiddin & Halaqah Ilmiah",
                date = "28 September 2026",
                time = "19:30 WIB - Selesai",
                location = "Masjid Jami' Pondok Pesantren At-taroqqy / Live Zoom",
                category = "kajian",
                description = "Kajian bulanan mengkaji Kitab Ihya' Ulumuddin bab Adab Mu'asyarah dipandu oleh Dewan Asatidz dan sesepuh alumni.",
                attendeesCount = 145,
                uncertainCount = 20,
                absentCount = 5,
                userRsvp = null,
                posterResId = null,
                authorName = "Divisi Dakwah & Pengajian Alumni",
                authorHandle = "@dakwah_attaroqqy",
                postedAt = "5 hari yang lalu"
            ),
            EventAgenda(
                id = "ev-003",
                title = "Silaturahmi Korda Alumni Jawa Timur & Madura",
                date = "12 Oktober 2026",
                time = "09:00 - 13:00 WIB",
                location = "Gedung Islamic Center Surabaya",
                category = "korda",
                description = "Musyawarah pembentukan koperasi alumni dan penguatan jejaring wirausaha santri Jawa Timur dan Madura.",
                attendeesCount = 92,
                uncertainCount = 14,
                absentCount = 2,
                userRsvp = "belum_pasti",
                posterResId = null,
                authorName = "Korda Alumni Jatim",
                authorHandle = "@korda_jatim",
                postedAt = "1 minggu yang lalu"
            )
        )
    }

    private fun createInitialAnnouncements(): List<AnnouncementItem> {
        return listOf(
            AnnouncementItem(
                id = "ann-001",
                title = "Maklumat Resmi: Registrasi Digital & Verifikasi Data Alumni 2026",
                date = "1 Oktober 2026",
                postedAt = "Kemarin",
                category = "maklumat",
                content = "Diberitahukan kepada segenap alumni Ponpes At-taroqqy di seluruh penjuru tanah air untuk memperbarui data nomor kontak, alamat domisili, dan profesi terkini melalui aplikasi Portal Alumni. Verifikasi ini diperlukan dalam rangka persiapan pendataan Reuni Akbar dan penerbitan Kartu Tanda Alumni Digital.",
                authorName = "K.H. Masduki Syakur",
                authorHandle = "@pengasuh_attaroqqy",
                authorRole = "Pengasuh Pondok Pesantren",
                isImportant = true,
                likesCount = 248,
                isLiked = true,
                commentsCount = 37
            ),
            AnnouncementItem(
                id = "ann-002",
                title = "Program Beasiswa Kader Pesantren Santri Berprestasi 2026/2027",
                date = "25 September 2026",
                postedAt = "1 minggu yang lalu",
                category = "beasiswa",
                content = "Badan Wakaf dan Alumni At-taroqqy membuka kesempatan beasiswa penuh bagi putra-putri alumni dan santri berprestasi yang ingin melanjutkan studi S1/S2 ke Universitas Al-Azhar Kairo, Mesir dan Ma'had Aly Nusantara. Pendaftaran dibuka hingga 15 November 2026.",
                authorName = "Pengurus Pusat Alumni At-taroqqy",
                authorHandle = "@admin_pusat",
                authorRole = "Badan Otonom Pendidikan",
                isImportant = false,
                likesCount = 186,
                isLiked = false,
                commentsCount = 19
            ),
            AnnouncementItem(
                id = "ann-003",
                title = "Laporan Transparansi Pembangunan Asrama Santri Tahfidz",
                date = "15 September 2026",
                postedAt = "2 minggu yang lalu",
                category = "kegiatan",
                content = "Alhamdulillah pengerjaan lantai 3 asrama santri tahfidz telah mencapai progres 85%. Kami ucapkan jazakumullah khairal jaza' kepada segenap alumni yang telah mengikhlaskan sebagian rezekinya untuk infaq jariyah pondok tercinta.",
                authorName = "Panitia Wakaf & Pembangunan",
                authorHandle = "@wakaf_attaroqqy",
                authorRole = "Tim Pembangunan",
                isImportant = false,
                likesCount = 312,
                isLiked = true,
                commentsCount = 42
            )
        )
    }

    private fun createInitialComments(): List<EventComment> {
        return listOf(
            EventComment(
                id = "comm-1",
                eventId = "ev-001",
                authorName = "K.H. Abdullah Hasan",
                authorHandle = "@gus_hasan",
                status = "hadir",
                content = "Bismillah, rombongan alumni Tuban - Lamongan siap memberangkatkan 3 bus ke Rembang!",
                timeAgo = "1 jam yang lalu",
                likesCount = 24,
                isLiked = true
            ),
            EventComment(
                id = "comm-2",
                eventId = "ev-001",
                authorName = "Mulia Ningsih, S.Pd.",
                authorHandle = "@mulia_ningsih",
                status = "hadir",
                content = "Alhamdulillah siap sowan Masyayikh dan kangen-kangenan dengan teman seangkatan MAK 2020.",
                timeAgo = "3 jam yang lalu",
                likesCount = 18,
                isLiked = false
            ),
            EventComment(
                id = "comm-3",
                eventId = "ev-001",
                authorName = "Ahmad Fauzi, S.Kom.",
                authorHandle = "@fauzi_trq",
                status = "hadir",
                content = "Insya Allah hadir sekaligus membantu registrasi QR code kehadiran di meja panitia.",
                timeAgo = "5 jam yang lalu",
                likesCount = 31,
                isLiked = true
            )
        )
    }

    private fun createInitialAttendance(): List<AttendanceSession> {
        return listOf(
            AttendanceSession(
                id = "att-001",
                title = "Check-in Presensi Reuni Akbar XXV",
                date = "15 Agustus 2026",
                attendees = listOf(
                    AttendanceAttendee(
                        id = "attnd-1",
                        alumniId = "alm-002",
                        alumniName = "Ahmad Fauzi, S.Kom.",
                        alumniNis = "TRQ-2015-045",
                        city = "Surabaya",
                        gradYear = "2018",
                        checkInTime = "07:45 WIB",
                        method = "qr"
                    ),
                    AttendanceAttendee(
                        id = "attnd-2",
                        alumniId = "alm-005",
                        alumniName = "K.H. Abdullah Hasan",
                        alumniNis = "TRQ-2018-099",
                        city = "Tuban",
                        gradYear = "2019",
                        checkInTime = "08:10 WIB",
                        method = "manual"
                    )
                )
            )
        )
    }

    private fun createInitialFinance(): List<FinanceRecord> {
        return listOf(
            FinanceRecord(
                id = "fin-1",
                title = "Iuran Wajib Reuni Akbar XXV",
                category = "Iuran Reuni",
                amount = 250000,
                date = "02 Okt 2026",
                status = "Berhasil",
                donorName = "Ahmad Fauzi, S.Kom.",
                paymentMethod = "QRIS"
            ),
            FinanceRecord(
                id = "fin-2",
                title = "Infaq Santri Yatim & Tahfidz",
                category = "Infaq",
                amount = 500000,
                date = "28 Sep 2026",
                status = "Berhasil",
                donorName = "Dr. Hj. Siti Fatimah, M.Ag.",
                paymentMethod = "Transfer Bank Syariah"
            ),
            FinanceRecord(
                id = "fin-3",
                title = "Wakaf Pembangunan Menara & Aula",
                category = "Wakaf Bangunan",
                amount = 1000000,
                date = "20 Sep 2026",
                status = "Berhasil",
                donorName = "Muhammad Ridwan, S.H.",
                paymentMethod = "Transfer Bank Syariah"
            ),
            FinanceRecord(
                id = "fin-4",
                title = "Infaq Pengembangan Portal Digital Pesantren",
                category = "Infaq",
                amount = 350000,
                date = "15 Sep 2026",
                status = "Berhasil",
                donorName = "Mulia Ningsih, S.Pd.",
                paymentMethod = "QRIS"
            )
        )
    }

    private fun createInitialKitabs(): List<KitabItem> {
        return listOf(
            KitabItem(
                id = "kitab-1",
                category = "alquran",
                title = "Surat Al-Fatihah",
                arabicTitle = "سُورَةُ الْفَاتِحَةِ",
                subtitle = "Pembukaan (7 Ayat)",
                totalAyatOrFasal = "7 Ayat",
                description = "Surah pertama dalam Al-Qur'an, Ummul Kitab, wajib dibaca dalam setiap rakaat shalat.",
                ayats = listOf(
                    KitabAyat(1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillahir rahmanir rahim", "Dengan menyebut nama Allah Yang Maha Pengasih lagi Maha Penyayang."),
                    KitabAyat(2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "Alhamdulillahi rabbil 'alamin", "Segala puji bagi Allah, Tuhan semesta alam."),
                    KitabAyat(3, "الرَّحْمَٰنِ الرَّحِيمِ", "Ar-Rahmanir Rahim", "Maha Pengasih lagi Maha Penyayang."),
                    KitabAyat(4, "مَالِكِ يَوْمِ الدِّينِ", "Maliki yaumid-din", "Pemilik hari pembalasan."),
                    KitabAyat(5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyaka na'budu wa iyyaka nasta'in", "Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami memohon pertolongan."),
                    KitabAyat(6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Ihdinas-siratal mustaqim", "Tunjukilah kami jalan yang lurus,"),
                    KitabAyat(7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "Siratalladhina an'amta 'alaihim ghairil-maghdubi 'alaihim walad-dallin", "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya; bukan (jalan) mereka yang dimurkai, dan bukan (pula jalan) mereka yang sesat.")
                )
            ),
            KitabItem(
                id = "kitab-2",
                category = "alquran",
                title = "Surat Al-Mulk",
                arabicTitle = "سُورَةُ الْمُلْكِ",
                subtitle = "Kerajaan (30 Ayat)",
                totalAyatOrFasal = "30 Ayat",
                description = "Surah penyelamat dari azab kubur yang disunnahkan dibaca setiap malam oleh para santri.",
                ayats = listOf(
                    KitabAyat(1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Tabarakallazi biyadihil-mulku wa huwa 'ala kulli syai'in qadir", "Maha Suci Allah yang di tangan-Nya lah segala kerajaan, dan Dia Maha Kuasa atas segala sesuatu."),
                    KitabAyat(2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا وَهُوَ الْعَزِيزُ الْغَفُورُ", "Allazi khalaqal-mauta wal-hayata liyabluwakum ayyukum ahsanu 'amala, wa huwal-'azizul-ghafur", "Yang menciptakan mati dan hidup, untuk menguji kamu, siapa di antara kamu yang lebih baik amalnya. Dan Dia Maha Perkasa, Maha Pengampun.")
                )
            ),
            KitabItem(
                id = "kitab-3",
                category = "majmuah",
                title = "Majmu'ah Syarif: Doa Kanzul 'Arasy",
                arabicTitle = "دُعَاءُ كَنْزِ الْعَرْشِ",
                subtitle = "Doa Perbendaharaan 'Arasy",
                totalAyatOrFasal = "Doa & Dzikir",
                description = "Doa mustajab agung yang masyhur diamalkan para Ulama Salaf untuk hajat dunia dan akhirat.",
                ayats = listOf(
                    KitabAyat(1, "لَا إِلٰهَ إِلَّا اللهُ سُبْحَانَ الْمَلِكِ الْقُدُّوسِ", "La ilaha illallah subhanal malikil quddus", "Tiada Tuhan selain Allah, Maha Suci Raja Yang Maha Suci."),
                    KitabAyat(2, "لَا إِلٰهَ إِلَّا اللهُ سُبْحَانَ الْعَزِيزِ الْجَبَّارِ", "La ilaha illallah subhanal 'azizil jabbar", "Tiada Tuhan selain Allah, Maha Suci Dzat Yang Maha Perkasa lagi Maha Memaksa.")
                )
            ),
            KitabItem(
                id = "kitab-4",
                category = "maulid",
                title = "Maulid Simtudduror",
                arabicTitle = "مَوْلِدُ سِمْطِ الدُّرَرِ",
                subtitle = "Untaian Mutiara Kisah Nabi SAW",
                totalAyatOrFasal = "Karya Habib Ali bin Muhammad Al-Habsyi",
                description = "Kitab maulid masyhur berisi untaian shalawat dan sirah Rasulullah SAW yang dibaca setiap malam Jumat di pondok.",
                ayats = listOf(
                    KitabAyat(1, "يَا رَبِّ صَلِّ عَلَى مُحَمَّدْ مَا لَاحَ فِي الْأُفُقِ نُورُ كَوْكَبْ", "Ya Rabbi shalli 'ala Muhammad ma laha fil-ufuqi nuru kaukab", "Wahai Tuhanku, limpahkanlah rahmat kepada Nabi Muhammad, selama sinar bintang gemerlap di ufuk."),
                    KitabAyat(2, "يَا رَبِّ صَلِّ عَلَى مُحَمَّدْ اَلْفَاتِحِ الْخَاتِمِ الْمُقَرَّبْ", "Ya Rabbi shalli 'ala Muhammad al-fatihil khatimil muqarrab", "Wahai Tuhanku, limpahkanlah rahmat kepada Nabi Muhammad, sang pembuka, sang penutup, yang didekatkan.")
                )
            ),
            KitabItem(
                id = "kitab-5",
                category = "aurod",
                title = "Ratib Al-Haddad",
                arabicTitle = "رَاتِبُ الْحَدَّادِ",
                subtitle = "Wirid Al-Imam Abdullah bin Alwi Al-Haddad",
                totalAyatOrFasal = "Wirid Perlindungan & Berkah",
                description = "Amalan benteng diri dan keluarga yang diijazahkan para guru pesantren untuk dibaca setelah maghrib.",
                ayats = listOf(
                    KitabAyat(1, "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ (٣×)", "Bismillahilladzi la yadurru ma'asmihi syai'un fil-ardi wa la fis-sama'i wa huwas-sami'ul 'alim (3x)", "Dengan nama Allah yang bila disebut nama-Nya, tiada sesuatu pun di bumi dan di langit yang dapat mendatangkan bahaya, dan Dia Maha Mendengar lagi Maha Mengetahui."),
                    KitabAyat(2, "رَضِينَا بِاللَّهِ رَبًّا وَبِالْإِسْلَامِ دِينًا وَبِمُحَمَّدٍ نَبِيًّا (٣×)", "Radhina billahi rabba, wa bil-islami dina, wa bi Muhammadin nabiyya (3x)", "Kami ridho Allah sebagai Tuhan kami, Islam sebagai agama kami, dan Nabi Muhammad sebagai Nabi kami.")
                )
            )
        )
    }

    private fun createInitialNotifications(): List<NotificationItem> {
        return listOf(
            NotificationItem(
                id = "notif-1",
                type = "system",
                title = "Registrasi Reuni Akbar XXV",
                message = "RSVP Kehadiran Anda telah tercatat. Simpan QR Code di Kartu Alumni untuk scan saat registrasi.",
                time = "10 menit yang lalu",
                read = false
            ),
            NotificationItem(
                id = "notif-2",
                type = "finance",
                title = "Pembayaran Iuran Diterima",
                message = "Terima kasih, pembayaran Iuran Reuni Akbar XXV sebesar Rp 250.000 telah diverifikasi sistem.",
                time = "2 jam yang lalu",
                read = true
            ),
            NotificationItem(
                id = "notif-3",
                type = "like",
                title = "Komentar Disukai",
                message = "K.H. Abdullah Hasan menyukai komentar Anda di Agenda Reuni Akbar XXV.",
                time = "1 hari yang lalu",
                read = true
            )
        )
    }
}
