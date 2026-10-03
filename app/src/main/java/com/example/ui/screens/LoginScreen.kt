package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LoginScreen(
    viewModel: MainViewModel
) {
    var selectedRole by remember { mutableStateOf("alumni") } // "alumni" or "admin"
    var username by remember { mutableStateOf("mulia_ningsih") }
    var password by remember { mutableStateOf("1234") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Header Logo & Pesantren Title
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.size(80.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_ponpes),
                    contentDescription = "Logo Ponpes At-taroqqy",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "PORTAL ALUMNI",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Emerald900,
                    letterSpacing = 1.sp
                )
            )

            Text(
                text = "Pondok Pesantren At-taroqqy",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Emerald700,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = "Menjalin Silaturahmi dan Kolaborasi Antar Alumni",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Slate500,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Highfive illustration banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Emerald50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.alumni_illustration),
                        contentDescription = "Ilustrasi Alumni Silaturahmi",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xAA064E3B))
                                )
                            )
                    )
                    Text(
                        text = "Ukhuwah Islamiyyah & Khidmah Alumni",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Role Selector Tab
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    // Alumni tab
                    Button(
                        onClick = {
                            selectedRole = "alumni"
                            username = "mulia_ningsih"
                            password = "1234"
                            errorMessage = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole == "alumni") Emerald700 else Color.Transparent,
                            contentColor = if (selectedRole == "alumni") White else Slate700
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        elevation = if (selectedRole == "alumni") ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Alumni Santri",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Admin tab
                    Button(
                        onClick = {
                            selectedRole = "admin"
                            username = "@admin_pusat"
                            password = "1997"
                            errorMessage = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole == "admin") Emerald700 else Color.Transparent,
                            contentColor = if (selectedRole == "admin") White else Slate700
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        elevation = if (selectedRole == "admin") ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Admin Pondok",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Error banner
            AnimatedVisibility(visible = errorMessage != null) {
                errorMessage?.let { msg ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Red50),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = Red600,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = msg,
                                color = Red600,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { errorMessage = null },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Tutup",
                                    tint = Red600,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Input Form Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = if (selectedRole == "alumni") "Username, NIS, atau Email" else "Username Admin",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Slate700,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = if (selectedRole == "alumni") Icons.Default.Person else Icons.Default.Security,
                                contentDescription = null,
                                tint = Emerald700
                            )
                        },
                        placeholder = {
                            Text(
                                text = if (selectedRole == "alumni") "cth: mulia_ningsih atau TRQ-2017-089" else "@admin_pusat",
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Kata Sandi",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Slate700,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Emerald700
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Sembunyikan" else "Tampilkan",
                                    tint = Slate500
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Login Pertama? Sandi: 1234",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Emerald700,
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Text(
                            text = "Lupa Sandi?",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Amber600,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.clickable {
                                showForgotPasswordDialog = true
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (selectedRole == "alumni") {
                                val err = viewModel.loginAsAlumni(username, password)
                                errorMessage = err
                            } else {
                                val err = viewModel.loginAsAdmin(username, password)
                                errorMessage = err
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Masuk ke Portal",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Demo Accounts Section
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = Amber600,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Akun Uji Coba Cepat (Demo)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate800
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                selectedRole = "alumni"
                                username = "mulia_ningsih"
                                password = "1234"
                                viewModel.quickLogin("mulia")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Mulia (MAK)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Emerald700
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                selectedRole = "alumni"
                                username = "fauzi_trq"
                                password = "passwordfauzi"
                                viewModel.quickLogin("fauzi")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Fauzi (IPA)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Emerald700
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                selectedRole = "admin"
                                username = "@admin_pusat"
                                password = "1997"
                                viewModel.quickLogin("admin")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Admin Pusat",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Amber700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showHelpDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Outlined.HelpOutline,
                    contentDescription = null,
                    tint = Slate500,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Bantuan Aktivasi Akun Alumni & Kontak Pengurus",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Slate500,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = Amber600)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Bantuan Lupa Kata Sandi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Bagi alumni yang lupa kata sandi akun:",
                        fontSize = 13.sp,
                        color = Slate700
                    )
                    Text(
                        "1. Jika belum pernah merubah sandi, gunakan sandi bawaan: 1234",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                    Text(
                        "2. Jika sudah pernah dirubah dan lupa, hubungi Sekretariat Alumni via WhatsApp untuk reset kata sandi ke '1234'.",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                    Text(
                        "Hotline WhatsApp Pengurus: 0812-9876-5432 (Ustadz Ridwan)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald700
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Tutup", fontWeight = FontWeight.Bold, color = Emerald700)
                }
            }
        )
    }

    // Help Dialog
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Emerald700)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Panduan Akses Portal Alumni", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Portal Alumni At-taroqqy terhubung dengan database resmi santri dan alumni.",
                        fontSize = 13.sp,
                        color = Slate700
                    )
                    Text(
                        "Setiap alumni yang telah lulus telah terdaftar berdasarkan NIS (Nomor Induk Santri). Gunakan NIS atau Username Anda untuk masuk.",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                    Text(
                        "Sekretariat Ikatan Alumni At-taroqqy:\nPondok Pesantren At-taroqqy, Sedan, Rembang, Jawa Tengah.",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Mengerti", fontWeight = FontWeight.Bold, color = Emerald700)
                }
            }
        )
    }
}
