package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AlumniRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AppSession {
    object LoggedOut : AppSession()
    data class AlumniSession(val alumni: AlumniRecord) : AppSession()
    data class AdminSession(val admin: AdminUser) : AppSession()
}

class MainViewModel(
    private val repository: AlumniRepository = AlumniRepository()
) : ViewModel() {

    val alumniList: StateFlow<List<AlumniRecord>> = repository.alumniList
    val adminUser: StateFlow<AdminUser> = repository.adminUser
    val events: StateFlow<List<EventAgenda>> = repository.events
    val announcements: StateFlow<List<AnnouncementItem>> = repository.announcements
    val eventComments: StateFlow<List<EventComment>> = repository.eventComments
    val attendanceSessions: StateFlow<List<AttendanceSession>> = repository.attendanceSessions
    val financeRecords: StateFlow<List<FinanceRecord>> = repository.financeRecords
    val kitabList: StateFlow<List<KitabItem>> = repository.kitabList
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications

    private val _session = MutableStateFlow<AppSession>(AppSession.LoggedOut)
    val session: StateFlow<AppSession> = _session.asStateFlow()

    // Navigation Tabs
    private val _alumniTab = MutableStateFlow(0) // 0: Beranda, 1: Direktori, 2: Kitab, 3: Kas, 4: Kartu, 5: Profil
    val alumniTab: StateFlow<Int> = _alumniTab.asStateFlow()

    private val _adminTab = MutableStateFlow(0) // 0: Alumni, 1: Agenda & Absensi, 2: Maklumat, 3: Statistik
    val adminTab: StateFlow<Int> = _adminTab.asStateFlow()

    // Search and Filters for Directory
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _genderFilter = MutableStateFlow("Semua") // Semua, L, P
    val genderFilter: StateFlow<String> = _genderFilter.asStateFlow()

    private val _selectedProvince = MutableStateFlow("Semua")
    val selectedProvince: StateFlow<String> = _selectedProvince.asStateFlow()

    // Modals and Sheet States
    private val _selectedAlumniDetail = MutableStateFlow<AlumniRecord?>(null)
    val selectedAlumniDetail: StateFlow<AlumniRecord?> = _selectedAlumniDetail.asStateFlow()

    private val _selectedKitabItem = MutableStateFlow<KitabItem?>(null)
    val selectedKitabItem: StateFlow<KitabItem?> = _selectedKitabItem.asStateFlow()

    private val _selectedEventForComments = MutableStateFlow<EventAgenda?>(null)
    val selectedEventForComments: StateFlow<EventAgenda?> = _selectedEventForComments.asStateFlow()

    private val _showInfaqModal = MutableStateFlow(false)
    val showInfaqModal: StateFlow<Boolean> = _showInfaqModal.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    private val _showAddAlumniDialog = MutableStateFlow(false)
    val showAddAlumniDialog: StateFlow<Boolean> = _showAddAlumniDialog.asStateFlow()

    private val _showEditAlumniDialog = MutableStateFlow<AlumniRecord?>(null)
    val showEditAlumniDialog: StateFlow<AlumniRecord?> = _showEditAlumniDialog.asStateFlow()

    private val _showCreateAnnouncementDialog = MutableStateFlow(false)
    val showCreateAnnouncementDialog: StateFlow<Boolean> = _showCreateAnnouncementDialog.asStateFlow()

    private val _showAttendanceScannerDialog = MutableStateFlow(false)
    val showAttendanceScannerDialog: StateFlow<Boolean> = _showAttendanceScannerDialog.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    val filteredAlumni: StateFlow<List<AlumniRecord>> = combine(
        alumniList,
        searchQuery,
        genderFilter,
        selectedProvince
    ) { list, query, gender, prov ->
        list.filter { alm ->
            val matchQuery = query.isBlank() ||
                    alm.name.contains(query, ignoreCase = true) ||
                    alm.nis.contains(query, ignoreCase = true) ||
                    alm.city.contains(query, ignoreCase = true) ||
                    alm.gradYear.contains(query, ignoreCase = true) ||
                    alm.occupation.contains(query, ignoreCase = true)

            val matchGender = gender == "Semua" || alm.gender == gender

            val matchProv = prov == "Semua" || alm.province.contains(prov, ignoreCase = true)

            matchQuery && matchGender && matchProv
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loginAsAlumni(usernameOrNis: String, pass: String): String? {
        val cleanInput = usernameOrNis.trim().removePrefix("@").lowercase()
        val found = alumniList.value.find {
            it.username.lowercase().removePrefix("@") == cleanInput ||
                    it.nis.equals(usernameOrNis.trim(), ignoreCase = true) ||
                    it.email.equals(usernameOrNis.trim(), ignoreCase = true)
        }
        if (found == null) {
            return "Data Alumni tidak ditemukan. Pastikan Username atau NIS sudah benar."
        }
        if (found.password != pass) {
            return "Kata sandi salah. Sandi bawaan akun baru adalah 1234."
        }
        val updated = found.copy(hasLoggedIn = true)
        repository.updateAlumni(updated)
        _session.value = AppSession.AlumniSession(updated)
        _alumniTab.value = 0
        return null
    }

    fun loginAsAdmin(usernameInput: String, pass: String): String? {
        val cleanInput = usernameInput.trim().removePrefix("@").lowercase()
        val admin = adminUser.value
        val validAdminUsers = listOf("admin_pusat", "admin", "superadmin")
        if (cleanInput in validAdminUsers && pass == admin.password) {
            _session.value = AppSession.AdminSession(admin)
            _adminTab.value = 0
            return null
        }
        return "Kredensial Admin tidak cocok. Gunakan username: @admin_pusat / sandi: 1997"
    }

    fun quickLogin(type: String) {
        when (type) {
            "mulia" -> {
                loginAsAlumni("mulia_ningsih", "1234")
            }
            "fauzi" -> {
                loginAsAlumni("fauzi_trq", "passwordfauzi")
            }
            "admin" -> {
                loginAsAdmin("@admin_pusat", "1997")
            }
        }
    }

    fun logout() {
        _session.value = AppSession.LoggedOut
        _alumniTab.value = 0
        _adminTab.value = 0
        _selectedAlumniDetail.value = null
        _selectedKitabItem.value = null
        _selectedEventForComments.value = null
    }

    fun setAlumniTab(tab: Int) {
        _alumniTab.value = tab
    }

    fun setAdminTab(tab: Int) {
        _adminTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setGenderFilter(filter: String) {
        _genderFilter.value = filter
    }

    fun setSelectedProvince(prov: String) {
        _selectedProvince.value = prov
    }

    fun selectAlumniDetail(alm: AlumniRecord?) {
        _selectedAlumniDetail.value = alm
    }

    fun selectKitabItem(kitab: KitabItem?) {
        _selectedKitabItem.value = kitab
    }

    fun selectEventForComments(event: EventAgenda?) {
        _selectedEventForComments.value = event
    }

    fun toggleCardFlip() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun setShowInfaqModal(show: Boolean) {
        _showInfaqModal.value = show
    }

    fun setShowNotificationsSheet(show: Boolean) {
        _showNotificationsSheet.value = show
    }

    fun setShowAddAlumniDialog(show: Boolean) {
        _showAddAlumniDialog.value = show
    }

    fun setShowEditAlumniDialog(alm: AlumniRecord?) {
        _showEditAlumniDialog.value = alm
    }

    fun setShowCreateAnnouncementDialog(show: Boolean) {
        _showCreateAnnouncementDialog.value = show
    }

    fun setShowAttendanceScannerDialog(show: Boolean) {
        _showAttendanceScannerDialog.value = show
    }

    fun updateCurrentAlumniProfile(updated: AlumniRecord) {
        repository.updateAlumni(updated)
        if (_session.value is AppSession.AlumniSession) {
            _session.value = AppSession.AlumniSession(updated)
        }
    }

    fun rsvpEvent(eventId: String, rsvp: String, note: String) {
        repository.rsvpEvent(eventId, rsvp, note)
    }

    fun submitEventComment(eventId: String, content: String, status: String) {
        val currentAuthor = when (val s = _session.value) {
            is AppSession.AlumniSession -> Pair(s.alumni.name, "@${s.alumni.username.ifBlank { "alumni" }}")
            is AppSession.AdminSession -> Pair(s.admin.name, s.admin.username)
            else -> Pair("Alumni", "@alumni")
        }
        repository.addEventComment(eventId, currentAuthor.first, currentAuthor.second, status, content)
    }

    fun toggleCommentLike(commentId: String) {
        repository.toggleCommentLike(commentId)
    }

    fun toggleAnnouncementLike(annId: String) {
        repository.toggleAnnouncementLike(annId)
    }

    fun submitNewAnnouncement(title: String, content: String, category: String, isImportant: Boolean) {
        val currentAuthor = when (val s = _session.value) {
            is AppSession.AdminSession -> s.admin.name
            is AppSession.AlumniSession -> s.alumni.name
            else -> "Pengurus Pondok"
        }
        val newAnn = AnnouncementItem(
            id = "ann-${System.currentTimeMillis()}",
            title = title,
            content = content,
            category = category,
            date = "Hari ini",
            postedAt = "Baru saja",
            authorName = currentAuthor,
            authorHandle = "@admin_pusat",
            authorRole = "Pengurus Pondok",
            isImportant = isImportant,
            likesCount = 0,
            isLiked = false,
            commentsCount = 0
        )
        repository.addAnnouncement(newAnn)
        _showCreateAnnouncementDialog.value = false
    }

    fun submitNewAlumni(alumni: AlumniRecord) {
        repository.addAlumni(alumni)
        _showAddAlumniDialog.value = false
    }

    fun submitUpdateAlumni(alumni: AlumniRecord) {
        repository.updateAlumni(alumni)
        _showEditAlumniDialog.value = null
    }

    fun resetAlumniPassword(id: String) {
        repository.resetAlumniPassword(id)
    }

    fun deleteAlumni(id: String) {
        repository.deleteAlumni(id)
    }

    fun submitInfaqDonation(title: String, category: String, amount: Long, method: String) {
        val donor = when (val s = _session.value) {
            is AppSession.AlumniSession -> s.alumni.name
            else -> "Hamba Allah"
        }
        val record = FinanceRecord(
            id = "fin-${System.currentTimeMillis()}",
            title = title,
            category = category,
            amount = amount,
            date = "Hari ini",
            status = "Berhasil",
            donorName = donor,
            paymentMethod = method
        )
        repository.addFinanceDonation(record)
        _showInfaqModal.value = false
    }

    fun checkInAlumni(alumni: AlumniRecord) {
        val attendee = AttendanceAttendee(
            id = "attnd-${System.currentTimeMillis()}",
            alumniId = alumni.id,
            alumniName = alumni.name,
            alumniNis = alumni.nis,
            city = alumni.city,
            gradYear = alumni.gradYear,
            checkInTime = "Sekarang",
            method = "qr"
        )
        repository.addAttendanceAttendee("att-001", attendee)
    }
}
