package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainScreen(
    admin: AdminUser,
    viewModel: MainViewModel
) {
    val activeTab by viewModel.adminTab.collectAsState()
    val alumniList by viewModel.filteredAlumni.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val events by viewModel.events.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val attendanceSessions by viewModel.attendanceSessions.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = White),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_ponpes),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Panel Admin Pondok",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = admin.jabatan,
                                color = Amber500,
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Keluar", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Emerald800
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = White,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { viewModel.setAdminTab(0) },
                    icon = { Icon(if (activeTab == 0) Icons.Filled.People else Icons.Outlined.People, contentDescription = "Alumni") },
                    label = { Text("Data Alumni", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { viewModel.setAdminTab(1) },
                    icon = { Icon(if (activeTab == 1) Icons.Filled.QrCodeScanner else Icons.Outlined.QrCodeScanner, contentDescription = "Absensi") },
                    label = { Text("Presensi Event", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { viewModel.setAdminTab(2) },
                    icon = { Icon(if (activeTab == 2) Icons.Filled.Campaign else Icons.Outlined.Campaign, contentDescription = "Maklumat") },
                    label = { Text("Maklumat", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 3,
                    onClick = { viewModel.setAdminTab(3) },
                    icon = { Icon(if (activeTab == 3) Icons.Filled.BarChart else Icons.Outlined.BarChart, contentDescription = "Statistik") },
                    label = { Text("Statistik", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
            }
        },
        floatingActionButton = {
            when (activeTab) {
                0 -> {
                    FloatingActionButton(
                        onClick = { viewModel.setShowAddAlumniDialog(true) },
                        containerColor = Emerald700,
                        contentColor = White
                    ) {
                        Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Tambah Alumni")
                    }
                }
                1 -> {
                    FloatingActionButton(
                        onClick = { viewModel.setShowAttendanceScannerDialog(true) },
                        containerColor = Emerald700,
                        contentColor = White
                    ) {
                        Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "Scan Presensi")
                    }
                }
                2 -> {
                    FloatingActionButton(
                        onClick = { viewModel.setShowCreateAnnouncementDialog(true) },
                        containerColor = Amber600,
                        contentColor = White
                    ) {
                        Icon(imageVector = Icons.Default.AddComment, contentDescription = "Buat Maklumat")
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Slate50)
                .padding(paddingValues)
        ) {
            when (activeTab) {
                0 -> AdminAlumniTab(
                    alumniList = alumniList,
                    searchQuery = searchQuery,
                    viewModel = viewModel
                )
                1 -> AdminAttendanceTab(
                    events = events,
                    attendanceSessions = attendanceSessions,
                    viewModel = viewModel
                )
                2 -> AdminAnnouncementsTab(
                    announcements = announcements,
                    viewModel = viewModel
                )
                3 -> AdminStatisticsTab(
                    alumniList = viewModel.alumniList.collectAsState().value
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Keluar dari Panel Admin?", fontWeight = FontWeight.Bold) },
            text = { Text("Anda akan keluar dari sesi administrator pondok.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red600)
                ) {
                    Text("Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

// ---------------- ADMIN TAB 0: DATA ALUMNI ----------------
@Composable
fun AdminAlumniTab(
    alumniList: List<AlumniRecord>,
    searchQuery: String,
    viewModel: MainViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Cari nama, NIS, NIK alumni...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Emerald700) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Data: ${alumniList.size} Alumni",
                        fontSize = 12.sp,
                        color = Slate600,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sandi Default: 1234",
                        fontSize = 11.sp,
                        color = Emerald700,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            items(alumniList) { alm ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = alm.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = "NIS: ${alm.nis} • Lulus ${alm.gradYear}",
                                    fontSize = 11.sp,
                                    color = Emerald700,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${alm.jenjang} | ${alm.city}",
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }

                            Card(
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (alm.hasLoggedIn) Emerald50 else Slate100
                                )
                            ) {
                                Text(
                                    text = if (alm.hasLoggedIn) "Aktif" else "Belum Login",
                                    fontSize = 9.sp,
                                    color = if (alm.hasLoggedIn) Emerald700 else Slate500,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.setShowEditAlumniDialog(alm) },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = Emerald700)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit Data", fontSize = 11.sp, color = Emerald700)
                            }

                            OutlinedButton(
                                onClick = { viewModel.resetAlumniPassword(alm.id) },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.LockReset, contentDescription = null, modifier = Modifier.size(14.dp), tint = Amber700)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset Sandi", fontSize = 11.sp, color = Amber700)
                            }

                            IconButton(
                                onClick = { viewModel.deleteAlumni(alm.id) },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Red600, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- ADMIN TAB 1: PRESENSI EVENT ----------------
@Composable
fun AdminAttendanceTab(
    events: List<EventAgenda>,
    attendanceSessions: List<AttendanceSession>,
    viewModel: MainViewModel
) {
    val currentSession = attendanceSessions.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Emerald800),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sistem Presensi Event & Reuni", color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Verifikasi kehadiran alumni dengan scan QR code kartu alumni digital.", color = Emerald100, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.setShowAttendanceScannerDialog(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = Amber500, contentColor = Slate900),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Buka Scanner Presensi", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        item {
            Text(
                text = "Daftar Hadir Terkini: ${currentSession?.title ?: "Presensi"}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Slate800
            )
        }

        currentSession?.attendees?.let { attendees ->
            items(attendees) { attnd ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald600, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(attnd.alumniName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                                Text("NIS: ${attnd.alumniNis} • ${attnd.city}", fontSize = 11.sp, color = Slate500)
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Emerald50)
                        ) {
                            Text(
                                text = "${attnd.checkInTime} (${attnd.method.uppercase()})",
                                fontSize = 10.sp,
                                color = Emerald700,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- ADMIN TAB 2: MAKLUMAT PONDOK ----------------
@Composable
fun AdminAnnouncementsTab(
    announcements: List<AnnouncementItem>,
    viewModel: MainViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Maklumat Pondok",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Slate900
                )
                Button(
                    onClick = { viewModel.setShowCreateAnnouncementDialog(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Buat Baru", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(announcements) { ann ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = ann.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Slate900,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = ann.date,
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = ann.content,
                        fontSize = 12.sp,
                        color = Slate600,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "❤️ ${ann.likesCount} Suka",
                            fontSize = 11.sp,
                            color = Emerald700,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Author: ${ann.authorName}",
                            fontSize = 10.sp,
                            color = Slate500
                        )
                    }
                }
            }
        }
    }
}

// ---------------- ADMIN TAB 3: STATISTIK WILAYAH ----------------
@Composable
fun AdminStatisticsTab(
    alumniList: List<AlumniRecord>
) {
    val total = alumniList.size
    val ikhwanCount = alumniList.count { it.gender == "L" }
    val akhwatCount = alumniList.count { it.gender == "P" }
    val loggedInCount = alumniList.count { it.hasLoggedIn }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Statistik & Ringkasan Alumni", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Emerald50),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Total Terdata", fontSize = 11.sp, color = Emerald900)
                        Text("$total", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Emerald800)
                        Text("Santri Lulusan", fontSize = 10.sp, color = Emerald700)
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Amber100),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Akun Aktif", fontSize = 11.sp, color = Amber700)
                        Text("$loggedInCount", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Amber700)
                        Text("Sudah Login", fontSize = 10.sp, color = Slate700)
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Perbandingan Gender", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ikhwan (Putra): $ikhwanCount alumni", fontSize = 12.sp, color = Blue600, fontWeight = FontWeight.SemiBold)
                        Text("Akhwat (Putri): $akhwatCount alumni", fontSize = 12.sp, color = Amber700, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val ratio = if (total > 0) ikhwanCount.toFloat() / total.toFloat() else 0.5f
                    LinearProgressIndicator(
                        progress = { ratio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Blue600,
                        trackColor = Amber500
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sebaran Wilayah Terbanyak", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                    Spacer(modifier = Modifier.height(10.dp))

                    val grouped = alumniList.groupBy { it.province }.toList().sortedByDescending { it.second.size }
                    grouped.forEach { (prov, list) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(prov.ifBlank { "Lainnya" }, fontSize = 12.sp, color = Slate700)
                            Text("${list.size} Santri", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald800)
                        }
                    }
                }
            }
        }
    }
}
