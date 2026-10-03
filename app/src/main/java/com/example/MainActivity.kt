package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.PortalAlumniTheme
import com.example.ui.viewmodel.AppSession
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PortalAlumniTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val mainViewModel: MainViewModel = viewModel()
                    PortalAlumniApp(viewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
fun PortalAlumniApp(viewModel: MainViewModel) {
    val session by viewModel.session.collectAsState()
    val alumniTab by viewModel.alumniTab.collectAsState()
    val adminTab by viewModel.adminTab.collectAsState()

    // Modals states
    val selectedAlumniDetail by viewModel.selectedAlumniDetail.collectAsState()
    val selectedKitab by viewModel.selectedKitabItem.collectAsState()
    val selectedEventForComments by viewModel.selectedEventForComments.collectAsState()
    val showInfaqModal by viewModel.showInfaqModal.collectAsState()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsState()
    val showAddAlumniDialog by viewModel.showAddAlumniDialog.collectAsState()
    val showEditAlumniDialog by viewModel.showEditAlumniDialog.collectAsState()
    val showCreateAnnouncementDialog by viewModel.showCreateAnnouncementDialog.collectAsState()
    val showAttendanceScannerDialog by viewModel.showAttendanceScannerDialog.collectAsState()

    val comments by viewModel.eventComments.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val allAlumni by viewModel.alumniList.collectAsState()

    // BackHandler: Return to Home tab if nested
    BackHandler(enabled = session is AppSession.AlumniSession && alumniTab != 0) {
        viewModel.setAlumniTab(0)
    }

    BackHandler(enabled = session is AppSession.AdminSession && adminTab != 0) {
        viewModel.setAdminTab(0)
    }

    // Main App Screen Switcher
    when (val s = session) {
        is AppSession.LoggedOut -> {
            LoginScreen(viewModel = viewModel)
        }
        is AppSession.AlumniSession -> {
            AlumniMainScreen(alumni = s.alumni, viewModel = viewModel)
        }
        is AppSession.AdminSession -> {
            AdminMainScreen(admin = s.admin, viewModel = viewModel)
        }
    }

    // Global Modal & Sheets
    selectedAlumniDetail?.let { alm ->
        AlumniDetailModal(
            alumni = alm,
            onDismiss = { viewModel.selectAlumniDetail(null) }
        )
    }

    selectedKitab?.let { kitab ->
        KitabReaderDialog(
            kitab = kitab,
            onDismiss = { viewModel.selectKitabItem(null) }
        )
    }

    selectedEventForComments?.let { event ->
        EventCommentsDialog(
            event = event,
            comments = comments,
            onDismiss = { viewModel.selectEventForComments(null) },
            onSubmitComment = { content, status ->
                viewModel.submitEventComment(event.id, content, status)
            },
            onToggleLike = { commentId ->
                viewModel.toggleCommentLike(commentId)
            }
        )
    }

    if (showInfaqModal) {
        InfaqPaymentDialog(
            onDismiss = { viewModel.setShowInfaqModal(false) },
            onSubmitDonation = { title, cat, amt, method ->
                viewModel.submitInfaqDonation(title, cat, amt, method)
            }
        )
    }

    if (showNotificationsSheet) {
        NotificationsSheet(
            notifications = notifications,
            onDismiss = { viewModel.setShowNotificationsSheet(false) }
        )
    }

    if (showAddAlumniDialog) {
        AddAlumniDialog(
            onDismiss = { viewModel.setShowAddAlumniDialog(false) },
            onSubmit = { newAlm -> viewModel.submitNewAlumni(newAlm) }
        )
    }

    showEditAlumniDialog?.let { alm ->
        EditProfileDialog(
            alumni = alm,
            onDismiss = { viewModel.setShowEditAlumniDialog(null) },
            onSave = { updated -> viewModel.submitUpdateAlumni(updated) }
        )
    }

    if (showCreateAnnouncementDialog) {
        CreateAnnouncementDialog(
            onDismiss = { viewModel.setShowCreateAnnouncementDialog(false) },
            onSubmit = { title, content, cat, isImportant ->
                viewModel.submitNewAnnouncement(title, content, cat, isImportant)
            }
        )
    }

    if (showAttendanceScannerDialog) {
        AttendanceScannerDialog(
            alumniList = allAlumni,
            onDismiss = { viewModel.setShowAttendanceScannerDialog(false) },
            onCheckIn = { alm -> viewModel.checkInAlumni(alm) }
        )
    }
}
