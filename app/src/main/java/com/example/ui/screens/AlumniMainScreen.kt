package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniMainScreen(
    alumni: AlumniRecord,
    viewModel: MainViewModel
) {
    val activeTab by viewModel.alumniTab.collectAsState()
    val events by viewModel.events.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val filteredAlumni by viewModel.filteredAlumni.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val genderFilter by viewModel.genderFilter.collectAsState()
    val selectedProvince by viewModel.selectedProvince.collectAsState()
    val kitabList by viewModel.kitabList.collectAsState()
    val financeRecords by viewModel.financeRecords.collectAsState()
    val isCardFlipped by viewModel.isCardFlipped.collectAsState()

    var showDistributionMapDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = White,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { viewModel.setAlumniTab(0) },
                    icon = { Icon(if (activeTab == 0) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { viewModel.setAlumniTab(1) },
                    icon = { Icon(if (activeTab == 1) Icons.Filled.People else Icons.Outlined.People, contentDescription = "Direktori") },
                    label = { Text("Direktori", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { viewModel.setAlumniTab(2) },
                    icon = { Icon(if (activeTab == 2) Icons.Filled.MenuBook else Icons.Outlined.MenuBook, contentDescription = "Kitab") },
                    label = { Text("Kitab", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 3,
                    onClick = { viewModel.setAlumniTab(3) },
                    icon = { Icon(if (activeTab == 3) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet, contentDescription = "Kas") },
                    label = { Text("Kas & Infaq", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 4,
                    onClick = { viewModel.setAlumniTab(4) },
                    icon = { Icon(if (activeTab == 4) Icons.Filled.Badge else Icons.Outlined.Badge, contentDescription = "Kartu") },
                    label = { Text("Kartu", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 5,
                    onClick = { viewModel.setAlumniTab(5) },
                    icon = { Icon(if (activeTab == 5) Icons.Filled.Person else Icons.Outlined.Person, contentDescription = "Profil") },
                    label = { Text("Profil", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald700,
                        indicatorColor = Emerald100,
                        selectedTextColor = Emerald700
                    )
                )
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
                0 -> AlumniHomeTab(
                    alumni = alumni,
                    events = events,
                    announcements = announcements,
                    viewModel = viewModel
                )
                1 -> AlumniDirectoryTab(
                    alumniList = filteredAlumni,
                    searchQuery = searchQuery,
                    genderFilter = genderFilter,
                    selectedProvince = selectedProvince,
                    onOpenMap = { showDistributionMapDialog = true },
                    viewModel = viewModel
                )
                2 -> AlumniKitabTab(
                    kitabs = kitabList,
                    onSelectKitab = { viewModel.selectKitabItem(it) }
                )
                3 -> AlumniFinanceTab(
                    financeRecords = financeRecords,
                    onOpenInfaqModal = { viewModel.setShowInfaqModal(true) }
                )
                4 -> AlumniDigitalCardTab(
                    alumni = alumni,
                    isFlipped = isCardFlipped,
                    onFlipCard = { viewModel.toggleCardFlip() }
                )
                5 -> AlumniProfileTab(
                    alumni = alumni,
                    onEditProfile = { showEditProfileDialog = true },
                    onLogoutRequest = { showLogoutConfirmDialog = true }
                )
            }
        }
    }

    // Distribution Map Dialog
    if (showDistributionMapDialog) {
        DistributionMapDialog(
            alumniList = filteredAlumni,
            onDismiss = { showDistributionMapDialog = false }
        )
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            alumni = alumni,
            onDismiss = { showEditProfileDialog = false },
            onSave = { updated ->
                viewModel.updateCurrentAlumniProfile(updated)
                showEditProfileDialog = false
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmDialog = false },
            title = { Text("Keluar dari Akun?", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar dari sesi Portal Alumni At-taroqqy?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmDialog = false
                        viewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red600)
                ) {
                    Text("Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

// ---------------- TAB 0: BERANDA (HOME) ----------------
@Composable
fun AlumniHomeTab(
    alumni: AlumniRecord,
    events: List<EventAgenda>,
    announcements: List<AnnouncementItem>,
    viewModel: MainViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // App Bar Header
        item {
            Card(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Emerald800),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(containerColor = White),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo_ponpes),
                                    contentDescription = "Logo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Ahlan wa Sahlan,",
                                    color = Emerald100,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = alumni.name,
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${alumni.jenjang} • Angkatan ${alumni.gradYear}",
                                    color = Amber500,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Notification Bell
                        IconButton(
                            onClick = { viewModel.setShowNotificationsSheet(true) },
                            modifier = Modifier
                                .background(Color(0x33FFFFFF), CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifikasi",
                                tint = White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Quick Access Islamic Tiles Carousel
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Amalan & Bacaan Santri",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                    )
                    Text(
                        text = "Buka Kitab →",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Emerald700,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable { viewModel.setAlumniTab(2) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IslamicQuickCard(
                        title = "Al-Qur'an",
                        subtitle = "30 Juz Digital",
                        bgRes = R.drawable.bg_alquran,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setAlumniTab(2) }
                    )
                    IslamicQuickCard(
                        title = "Majmu'ah",
                        subtitle = "Doa & Dzikir",
                        bgRes = R.drawable.bg_alquran,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setAlumniTab(2) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IslamicQuickCard(
                        title = "Kitab Maulid",
                        subtitle = "Simtudduror & Diba'",
                        bgRes = R.drawable.bg_aurod,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setAlumniTab(2) }
                    )
                    IslamicQuickCard(
                        title = "Aurod & Ratib",
                        subtitle = "Ratib Al-Haddad",
                        bgRes = R.drawable.bg_aurod,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setAlumniTab(2) }
                    )
                }
            }
        }

        // Reuni Akbar & Agenda Header
        item {
            Text(
                text = "Agenda & Temu Alumni",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Event List Cards
        items(events) { event ->
            EventCard(
                event = event,
                onRsvp = { status, note ->
                    viewModel.rsvpEvent(event.id, status, note)
                },
                onOpenComments = {
                    viewModel.selectEventForComments(event)
                }
            )
        }

        // Official Announcements
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Maklumat & Pengumuman Pondok",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        items(announcements) { ann ->
            AnnouncementCard(
                announcement = ann,
                onToggleLike = { viewModel.toggleAnnouncementLike(ann.id) }
            )
        }
    }
}

@Composable
fun IslamicQuickCard(
    title: String,
    subtitle: String,
    bgRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .height(86.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = bgRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0x33000000), Color(0xDD022C22))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = title,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = subtitle,
                    color = Emerald100,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun EventCard(
    event: EventAgenda,
    onRsvp: (String, String) -> Unit,
    onOpenComments: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            // Poster if present
            event.posterResId?.let { resId ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = event.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xBB000000))
                                )
                            )
                    )
                    Card(
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Amber600),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = event.category.uppercase(),
                            color = White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Emerald700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "${event.date} • ${event.time}", fontSize = 12.sp, color = Slate600)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Emerald700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = event.location, fontSize = 12.sp, color = Slate600)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = event.description,
                    fontSize = 12.sp,
                    color = Slate700,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Attendance Status Counts
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Slate100, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Emerald600, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${event.attendeesCount} Hadir", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Emerald800)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Help, contentDescription = null, tint = Amber600, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${event.uncertainCount} Ragu", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Amber700)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${event.absentCount} Berhalangan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // RSVP Buttons
                Text(
                    text = "Konfirmasi Kehadiran Anda:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate700
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onRsvp("hadir", "Hadir") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (event.userRsvp == "hadir") Emerald700 else Slate200,
                            contentColor = if (event.userRsvp == "hadir") White else Slate800
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 6.dp)
                    ) {
                        Text("Hadir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onRsvp("belum_pasti", "Belum pasti") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (event.userRsvp == "belum_pasti") Amber600 else Slate200,
                            contentColor = if (event.userRsvp == "belum_pasti") White else Slate800
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 6.dp)
                    ) {
                        Text("Belum Pasti", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onRsvp("tidak_hadir", "Berhalangan") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (event.userRsvp == "tidak_hadir") Red600 else Slate200,
                            contentColor = if (event.userRsvp == "tidak_hadir") White else Slate800
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 6.dp)
                    ) {
                        Text("Berhalangan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Comment trigger button
                OutlinedButton(
                    onClick = { onOpenComments() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Emerald700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Komentar & Doa Alumni", color = Emerald700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun AnnouncementCard(
    announcement: AnnouncementItem,
    onToggleLike: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (announcement.isImportant) Red50 else Emerald50
                    )
                ) {
                    Text(
                        text = if (announcement.isImportant) "PENTING" else announcement.category.uppercase(),
                        color = if (announcement.isImportant) Red600 else Emerald700,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = announcement.postedAt,
                    fontSize = 11.sp,
                    color = Slate400
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = announcement.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = announcement.content,
                fontSize = 12.sp,
                color = Slate600,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Oleh: ${announcement.authorName} (${announcement.authorRole})",
                    fontSize = 11.sp,
                    color = Slate500,
                    fontWeight = FontWeight.Medium
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleLike, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (announcement.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Suka",
                            tint = if (announcement.isLiked) Red600 else Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "${announcement.likesCount}",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                }
            }
        }
    }
}

// ---------------- TAB 1: DIREKTORI ALUMNI ----------------
@Composable
fun AlumniDirectoryTab(
    alumniList: List<AlumniRecord>,
    searchQuery: String,
    genderFilter: String,
    selectedProvince: String,
    onOpenMap: () -> Unit,
    viewModel: MainViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Search header
        Card(
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Emerald800),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Direktori Alumni",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    // Map View button
                    Button(
                        onClick = onOpenMap,
                        colors = ButtonDefaults.buttonColors(containerColor = Amber600),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Peta Sebaran", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Cari nama, NIS, kota, profesi...", color = Slate400, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Emerald700) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Hapus", tint = Slate500)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedBorderColor = Emerald500,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Gender Filter Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Semua", "L", "P").forEach { g ->
                        val label = when (g) {
                            "L" -> "Ikhwan (Putra)"
                            "P" -> "Akhwat (Putri)"
                            else -> "Semua Gender"
                        }
                        val isSelected = genderFilter == g
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setGenderFilter(g) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Emerald500,
                                selectedLabelColor = White,
                                containerColor = Color(0x33FFFFFF),
                                labelColor = Emerald100
                            )
                        )
                    }
                }
            }
        }

        // Alumni List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Ditemukan ${alumniList.size} Alumni",
                    fontSize = 12.sp,
                    color = Slate500,
                    fontWeight = FontWeight.Medium
                )
            }

            items(alumniList) { alm ->
                AlumniItemCard(
                    alumni = alm,
                    onClick = { viewModel.selectAlumniDetail(alm) }
                )
            }
        }
    }
}

@Composable
fun AlumniItemCard(
    alumni: AlumniRecord,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Emerald100),
                modifier = Modifier.size(54.dp)
            ) {
                if (alumni.photoUrl.isNotBlank()) {
                    AsyncImage(
                        model = alumni.photoUrl,
                        contentDescription = alumni.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = alumni.name.take(1),
                            color = Emerald800,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = alumni.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Slate900,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (alumni.gender == "L") Blue50 else Amber100
                        )
                    ) {
                        Text(
                            text = if (alumni.gender == "L") "Ikhwan" else "Akhwat",
                            color = if (alumni.gender == "L") Blue600 else Amber700,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "NIS: ${alumni.nis} • Angkatan ${alumni.gradYear}",
                    fontSize = 11.sp,
                    color = Emerald700,
                    fontWeight = FontWeight.Medium
                )

                if (alumni.occupation.isNotBlank()) {
                    Text(
                        text = "${alumni.occupation} @ ${alumni.institution}",
                        fontSize = 11.sp,
                        color = Slate600,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "📍 ${alumni.city}, ${alumni.province}",
                    fontSize = 10.sp,
                    color = Slate400
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Detail",
                tint = Slate400,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ---------------- TAB 2: KITAB & AUROD ----------------
@Composable
fun AlumniKitabTab(
    kitabs: List<KitabItem>,
    onSelectKitab: (KitabItem) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("semua") }

    val filtered = if (selectedCategory == "semua") kitabs else kitabs.filter { it.category == selectedCategory }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Emerald800),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "Kitab & Amalan Santri",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Al-Qur'an, Majmu'ah Syarif, Maulid Nabi & Aurod Masyayikh",
                    color = Emerald100,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val cats = listOf(
                        "semua" to "Semua",
                        "alquran" to "Al-Qur'an",
                        "majmuah" to "Majmu'ah Syarif",
                        "maulid" to "Maulid",
                        "aurod" to "Aurod & Ratib"
                    )
                    items(cats) { (catKey, catLabel) ->
                        FilterChip(
                            selected = selectedCategory == catKey,
                            onClick = { selectedCategory = catKey },
                            label = { Text(catLabel, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Amber500,
                                selectedLabelColor = Slate900,
                                containerColor = Color(0x33FFFFFF),
                                labelColor = Emerald100
                            )
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { kitab ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectKitab(kitab) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = kitab.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Emerald900
                                )
                                Text(
                                    text = kitab.subtitle,
                                    fontSize = 11.sp,
                                    color = Amber700,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = kitab.arabicTitle,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald800,
                                textAlign = TextAlign.End
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = kitab.description,
                            fontSize = 12.sp,
                            color = Slate600
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📖 ${kitab.totalAyatOrFasal}",
                                fontSize = 11.sp,
                                color = Slate400,
                                fontWeight = FontWeight.Medium
                            )

                            Button(
                                onClick = { onSelectKitab(kitab) },
                                colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Mulai Membaca", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- TAB 3: KAS & INFAQ ALUMNI ----------------
@Composable
fun AlumniFinanceTab(
    financeRecords: List<FinanceRecord>,
    onOpenInfaqModal: () -> Unit
) {
    val totalSaldo = 142500000L
    val targetReuni = 180000000L
    val percentage = (totalSaldo.toFloat() / targetReuni.toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Emerald800),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = "Kas & Infaq Alumni",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Transparansi Kas Reuni Akbar & Wakaf Pembangunan Pesantren",
                        color = Emerald100,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Summary Balance Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Total Saldo Kas Alumni Saat Ini",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                    Text(
                        text = "Rp 142.500.000",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Emerald900
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Target Dana Reuni Akbar XXV",
                            fontSize = 11.sp,
                            color = Slate600
                        )
                        Text(
                            text = "Rp 180.000.000",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { percentage },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Amber500,
                        trackColor = Slate200
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${(percentage * 100).toInt()}% dari target terkumpul",
                        fontSize = 10.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onOpenInfaqModal,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Bayar Iuran / Salurkan Infaq (QRIS)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        item {
            Text(
                text = "Riwayat Infaq & Iuran Terkini",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        items(financeRecords) { record ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = Emerald50),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Payment,
                                    contentDescription = null,
                                    tint = Emerald700,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = record.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                            Text(text = "${record.donorName} • ${record.date}", fontSize = 11.sp, color = Slate500)
                            Text(text = "Metode: ${record.paymentMethod}", fontSize = 10.sp, color = Emerald700)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+ Rp ${String.format("%,d", record.amount)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Emerald700
                        )
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Emerald50)
                        ) {
                            Text(
                                text = record.status,
                                fontSize = 9.sp,
                                color = Emerald700,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- TAB 4: KARTU ALUMNI DIGITAL (FLIP 3D) ----------------
@Composable
fun AlumniDigitalCardTab(
    alumni: AlumniRecord,
    isFlipped: Boolean,
    onFlipCard: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "cardFlip"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kartu Tanda Alumni Digital",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Emerald900
            )
        )
        Text(
            text = "Sentuh kartu untuk membalik (Depan / Belakang)",
            fontSize = 12.sp,
            color = Slate500,
            modifier = Modifier.padding(top = 2.dp, bottom = 20.dp)
        )

        // 3D Flip Card Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clickable { onFlipCard() }
        ) {
            if (rotation <= 90f) {
                // Front Side
                DigitalCardFront(alumni = alumni)
            } else {
                // Back Side (counter-rotated so text is readable)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }
                ) {
                    DigitalCardBack(alumni = alumni)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onFlipCard,
                colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.FlipCameraAndroid, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Putar Kartu", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = { /* simulated share */ },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Emerald700, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Bagikan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald700)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Emerald50),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = Emerald700, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Kartu digital ini adalah tanda pengenal sah anggota Ikatan Alumni Pondok Pesantren At-taroqqy. Gunakan kode QR untuk registrasi presensi acara pondok.",
                    fontSize = 11.sp,
                    color = Emerald900,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun DigitalCardFront(alumni: AlumniRecord) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF064E3B), Color(0xFF022C22))
                    )
                )
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Card Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = White),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_ponpes),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("IKATAN ALUMNI AT-TAROQQY", color = White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Pondok Pesantren At-taroqqy Sedan", color = Emerald100, fontSize = 9.sp)
                        }
                    }

                    Text("KARTU ANGGOTA", color = Amber500, fontWeight = FontWeight.ExtraBold, fontSize = 10.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Card Body
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Emerald100),
                        modifier = Modifier.size(65.dp)
                    ) {
                        if (alumni.photoUrl.isNotBlank()) {
                            AsyncImage(
                                model = alumni.photoUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(alumni.name.take(1), fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Emerald900)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(alumni.name, color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("NIS: ${alumni.nis}", color = Amber500, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(alumni.jenjang, color = Emerald100, fontSize = 10.sp)
                        Text("Angkatan: ${alumni.gradYear} • Asrama: ${alumni.asramaDulu}", color = White.copy(alpha = 0.8f), fontSize = 10.sp)
                    }

                    // QR Code icon simulation
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.QrCode, contentDescription = "QR Code", tint = Slate900, modifier = Modifier.size(44.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun DigitalCardBack(alumni: AlumniRecord) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                    )
                )
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "KETENTUAN KEANGGOTAAN ALUMNI",
                    color = Amber500,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "1. Senantiasa menjaga nama baik Masyayikh dan almamater Pondok Pesantren At-taroqqy.\n2. Kartu ini sah apabila identitas pemilik sesuai dengan database resmi santri.\n3. Bila menemukan kartu ini, harap mengembalikan ke Sekretariat Alumni PP At-taroqqy Rembang.",
                    color = Slate300,
                    fontSize = 9.sp,
                    lineHeight = 13.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("Validitas Akun:", color = Slate400, fontSize = 9.sp)
                        Text("Terverifikasi Aktif", color = Emerald500, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text("NISN: ${alumni.nisn.ifBlank { "0012345678" }}", color = Slate400, fontSize = 9.sp)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ketua Umum Ikatan Alumni", color = Slate400, fontSize = 9.sp)
                        Spacer(modifier = Modifier.height(18.dp))
                        Text("K.H. Masduki Syakur", color = White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// ---------------- TAB 5: PROFIL SAYA ----------------
@Composable
fun AlumniProfileTab(
    alumni: AlumniRecord,
    onEditProfile: () -> Unit,
    onLogoutRequest: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Profile Header
        item {
            Card(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Emerald800),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = White),
                        modifier = Modifier.size(80.dp)
                    ) {
                        if (alumni.photoUrl.isNotBlank()) {
                            AsyncImage(
                                model = alumni.photoUrl,
                                contentDescription = alumni.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(alumni.name.take(1), fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Emerald800)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(alumni.name, color = White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("@${alumni.username.ifBlank { "alumni" }}", color = Emerald100, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Amber600)
                    ) {
                        Text(
                            text = "${alumni.jenjang} • Lulus ${alumni.gradYear}",
                            color = White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onEditProfile,
                        colors = ButtonDefaults.buttonColors(containerColor = White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Emerald800, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Perbarui Biodata & Kontak", color = Emerald800, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Biodata Details
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Informasi Kependudukan & Santri", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileFieldRow("Nomor Induk Santri (NIS)", alumni.nis)
                    ProfileFieldRow("Nomor Induk Kependudukan (NIK)", alumni.nik)
                    ProfileFieldRow("Tempat, Tanggal Lahir", "${alumni.tempatLahir}, ${alumni.tanggalLahir}")
                    ProfileFieldRow("Profesi Terkini", "${alumni.occupation} @ ${alumni.institution}")
                    ProfileFieldRow("No. Telepon / WhatsApp", alumni.phone)
                    ProfileFieldRow("Alamat Email", alumni.email)
                    ProfileFieldRow("Domisili", "${alumni.desa}, ${alumni.kecamatan}, ${alumni.city}, ${alumni.province}")
                    ProfileFieldRow("Nama Ayah", alumni.namaAyah)
                    ProfileFieldRow("Nama Ibu", alumni.namaIbu)
                }
            }
        }

        // Logout Button
        item {
            OutlinedButton(
                onClick = onLogoutRequest,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Red600),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = Red600, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Keluar dari Sesi Portal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun ProfileFieldRow(label: String, value: String) {
    if (value.isBlank()) return
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 12.sp, color = Slate800, fontWeight = FontWeight.SemiBold)
        Divider(color = Slate100, modifier = Modifier.padding(top = 4.dp))
    }
}

// ---------------- DIALOGS ----------------
@Composable
fun DistributionMapDialog(
    alumniList: List<AlumniRecord>,
    onDismiss: () -> Unit
) {
    val cityCounts = alumniList.groupBy { it.city }.mapValues { it.value.size }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Emerald700)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Peta Sebaran Alumni", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Text(
                        "Data sebaran wilayah alumni Ponpes At-taroqqy di seluruh Indonesia:",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(cityCounts.toList()) { (city, count) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = city, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Emerald100)
                        ) {
                            Text(
                                text = "$count Alumni",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald900,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Divider(color = Slate100)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", fontWeight = FontWeight.Bold, color = Emerald700)
            }
        }
    )
}

@Composable
fun EditProfileDialog(
    alumni: AlumniRecord,
    onDismiss: () -> Unit,
    onSave: (AlumniRecord) -> Unit
) {
    var phone by remember { mutableStateOf(alumni.phone) }
    var email by remember { mutableStateOf(alumni.email) }
    var occupation by remember { mutableStateOf(alumni.occupation) }
    var institution by remember { mutableStateOf(alumni.institution) }
    var city by remember { mutableStateOf(alumni.city) }
    var bio by remember { mutableStateOf(alumni.bio) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Perbarui Data Profil", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("No. Telepon / WhatsApp", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = occupation,
                    onValueChange = { occupation = it },
                    label = { Text("Pekerjaan / Profesi", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = institution,
                    onValueChange = { institution = it },
                    label = { Text("Instansi / Tempat Kerja", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Kota Domisili", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio Singkat", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        alumni.copy(
                            phone = phone,
                            email = email,
                            occupation = occupation,
                            institution = institution,
                            city = city,
                            bio = bio
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald700)
            ) {
                Text("Simpan Perubahan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
