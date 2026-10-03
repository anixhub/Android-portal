package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

// 1. KITAB READER FULLSCREEN DIALOG
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitabReaderDialog(
    kitab: KitabItem,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(kitab.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
                            Text(kitab.subtitle, fontSize = 11.sp, color = Emerald100)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Emerald800)
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Slate50)
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Emerald50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = kitab.arabicTitle,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald900,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = kitab.description,
                                fontSize = 12.sp,
                                color = Emerald800,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                items(kitab.ayats) { ayat ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    shape = CircleShape,
                                    colors = CardDefaults.cardColors(containerColor = Emerald100),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text("${ayat.number}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald900)
                                    }
                                }

                                Text(
                                    text = ayat.arabic,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth(0.85f),
                                    lineHeight = 38.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = ayat.latin,
                                fontSize = 12.sp,
                                color = Emerald700,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = ayat.translation,
                                fontSize = 12.sp,
                                color = Slate700,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// 2. EVENT COMMENTS DIALOG
@Composable
fun EventCommentsDialog(
    event: EventAgenda,
    comments: List<EventComment>,
    onDismiss: () -> Unit,
    onSubmitComment: (String, String) -> Unit,
    onToggleLike: (String) -> Unit
) {
    var newCommentText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("hadir") }

    val eventComments = comments.filter { it.eventId == event.id }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Emerald700)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Komentar & Doa Alumni", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
            ) {
                Text(event.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (eventComments.isEmpty()) {
                        item {
                            Text("Belum ada komentar. Jadilah yang pertama memberikan doa!", fontSize = 12.sp, color = Slate400)
                        }
                    }
                    items(eventComments) { c ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(c.authorName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                    Text(c.timeAgo, fontSize = 10.sp, color = Slate400)
                                }
                                Text(c.content, fontSize = 12.sp, color = Slate700, modifier = Modifier.padding(vertical = 4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { onToggleLike(c.id) }, modifier = Modifier.size(24.dp)) {
                                        Icon(
                                            imageVector = if (c.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = null,
                                            tint = if (c.isLiked) Red600 else Slate400,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text("${c.likesCount}", fontSize = 10.sp, color = Slate500)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    placeholder = { Text("Tuliskan doa atau pesan silaturahmi...", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (newCommentText.isNotBlank()) {
                            onSubmitComment(newCommentText, selectedStatus)
                            newCommentText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kirim Komentar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", color = Slate600)
            }
        }
    )
}

// 3. ALUMNI DETAIL MODAL
@Composable
fun AlumniDetailModal(
    alumni: AlumniRecord,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Emerald100),
                    modifier = Modifier.size(44.dp)
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
                            Text(alumni.name.take(1), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Emerald900)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(alumni.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
                    Text("NIS: ${alumni.nis} • Angkatan ${alumni.gradYear}", fontSize = 11.sp, color = Emerald700)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    if (alumni.bio.isNotBlank()) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Emerald50)
                        ) {
                            Text(
                                text = "\"${alumni.bio}\"",
                                fontSize = 11.sp,
                                color = Emerald900,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
                item { ProfileFieldRow("Jenjang Pendidikan", alumni.jenjang) }
                item { ProfileFieldRow("Asrama Waktu Mondok", alumni.asramaDulu) }
                item { ProfileFieldRow("Profesi Terkini", "${alumni.occupation} @ ${alumni.institution}") }
                item { ProfileFieldRow("Domisili", "${alumni.desa}, ${alumni.kecamatan}, ${alumni.city}, ${alumni.province}") }
                item { ProfileFieldRow("Nomor Telepon", if (alumni.shareContact) alumni.phone else "Nomor dirahasiakan oleh alumni") }
                item { ProfileFieldRow("Email", if (alumni.shareEmail) alumni.email else "Email pribadi") }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", fontWeight = FontWeight.Bold, color = Emerald700)
            }
        }
    )
}

// 4. INFAQ & QRIS MODAL
@Composable
fun InfaqPaymentDialog(
    onDismiss: () -> Unit,
    onSubmitDonation: (String, String, Long, String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Iuran Reuni") }
    var selectedAmount by remember { mutableStateOf(100000L) }
    var paymentMethod by remember { mutableStateOf("QRIS") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.QrCode2, contentDescription = null, tint = Emerald700)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pembayaran Iuran & Infaq", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Pilih Kategori Iuran / Infaq:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Iuran Reuni", "Infaq Pesantren", "Wakaf Pembangunan").forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Emerald700,
                                selectedLabelColor = White
                            )
                        )
                    }
                }

                Text("Pilih Nominal Donasi:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(50000L, 100000L, 250000L, 500000L).forEach { amt ->
                        FilterChip(
                            selected = selectedAmount == amt,
                            onClick = { selectedAmount = amt },
                            label = { Text("Rp ${amt / 1000}k", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Amber600,
                                selectedLabelColor = White
                            )
                        )
                    }
                }

                // QRIS Simulation Box
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("QRIS Standar Pembayaran Nasional", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate800)
                        Spacer(modifier = Modifier.height(6.dp))
                        Icon(Icons.Default.QrCode, contentDescription = "QRIS", modifier = Modifier.size(90.dp), tint = Slate900)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Scan menggunakan BCA, Mandiri, BRI, BSI, GoPay, OVO, Dana", fontSize = 10.sp, color = Slate500, textAlign = TextAlign.Center)
                    }
                }

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Emerald50)
                ) {
                    Text(
                        text = "Transfer Manual: BSI (Bank Syariah Indonesia)\nNo. Rekening: 7123-4567-89 a.n. Alumni At-taroqqy",
                        fontSize = 10.sp,
                        color = Emerald900,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmitDonation(selectedCategory, selectedCategory, selectedAmount, paymentMethod)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald700)
            ) {
                Text("Konfirmasi Pembayaran")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

// 5. NOTIFICATION SHEET
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSheet(
    notifications: List<NotificationItem>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Pusat Notifikasi", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                TextButton(onClick = onDismiss) {
                    Text("Tutup", color = Emerald700)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notifications) { notif ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = if (notif.read) Slate50 else Emerald50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = when (notif.type) {
                                    "finance" -> Icons.Default.Payments
                                    "like" -> Icons.Default.Favorite
                                    else -> Icons.Default.Notifications
                                },
                                contentDescription = null,
                                tint = Emerald700,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                Text(notif.message, fontSize = 11.sp, color = Slate600)
                                Text(notif.time, fontSize = 9.sp, color = Slate400, modifier = Modifier.padding(top = 2.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// 6. ATTENDANCE SCANNER MODAL (SIMULATOR)
@Composable
fun AttendanceScannerDialog(
    alumniList: List<AlumniRecord>,
    onDismiss: () -> Unit,
    onCheckIn: (AlumniRecord) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedAlumni by remember { mutableStateOf<AlumniRecord?>(null) }
    var checkInSuccessMsg by remember { mutableStateOf<String?>(null) }

    val filtered = alumniList.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.nis.contains(searchQuery, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Emerald700)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scanner Presensi QR / Manual", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Simulasi Scan QR Kartu Alumni atau cari nama/NIS:",
                    fontSize = 11.sp,
                    color = Slate600
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Ketik nama atau NIS...", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                checkInSuccessMsg?.let { msg ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Emerald100),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = msg,
                            color = Emerald900,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filtered) { alm ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedAlumni?.id == alm.id) Emerald100 else Slate100
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedAlumni = alm }
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(alm.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                    Text("NIS: ${alm.nis} • ${alm.city}", fontSize = 10.sp, color = Slate600)
                                }
                                Button(
                                    onClick = {
                                        onCheckIn(alm)
                                        checkInSuccessMsg = "✅ Berhasil Presensi: ${alm.name} (NIS: ${alm.nis})"
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Check-In", fontSize = 10.sp)
                                }
                            }
                        }
                    }
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

// 7. CREATE ANNOUNCEMENT DIALOG
@Composable
fun CreateAnnouncementDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("maklumat") }
    var isImportant by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Publikasikan Maklumat Baru", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Maklumat", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Isi Maklumat & Pengumuman", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isImportant, onCheckedChange = { isImportant = it })
                    Text("Tandai sebagai Maklumat Penting / Segera", fontSize = 11.sp, color = Slate700)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSubmit(title, content, category, isImportant)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald700)
            ) {
                Text("Publikasikan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

// 8. ADD ALUMNI DIALOG
@Composable
fun AddAlumniDialog(
    onDismiss: () -> Unit,
    onSubmit: (AlumniRecord) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var nis by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("L") }
    var gradYear by remember { mutableStateOf("2024") }
    var jenjang by remember { mutableStateOf("Madrasah Aliyah Keagamaan (MAK)") }
    var city by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("Jawa Tengah") }
    var phone by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Data Alumni Baru", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Lengkap", fontSize = 11.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = nis, onValueChange = { nis = it }, label = { Text("NIS Santri", fontSize = 11.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = nik, onValueChange = { nik = it }, label = { Text("NIK (KTP)", fontSize = 11.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = gender == "L", onClick = { gender = "L" }, label = { Text("Ikhwan (L)") })
                    FilterChip(selected = gender == "P", onClick = { gender = "P" }, label = { Text("Akhwat (P)") })
                }
                OutlinedTextField(value = gradYear, onValueChange = { gradYear = it }, label = { Text("Tahun Lulus", fontSize = 11.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Kota Asal", fontSize = 11.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("No. WhatsApp", fontSize = 11.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = occupation, onValueChange = { occupation = it }, label = { Text("Pekerjaan", fontSize = 11.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && nis.isNotBlank()) {
                        val newAlm = AlumniRecord(
                            id = "alm-${System.currentTimeMillis()}",
                            name = name,
                            username = name.lowercase().replace(" ", "_"),
                            nis = nis,
                            nik = nik,
                            gender = gender,
                            gradYear = gradYear,
                            jenjang = jenjang,
                            city = city,
                            province = province,
                            phone = phone,
                            occupation = occupation,
                            password = "1234",
                            isPasswordChanged = false,
                            hasLoggedIn = false
                        )
                        onSubmit(newAlm)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald700)
            ) {
                Text("Simpan Alumni")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
