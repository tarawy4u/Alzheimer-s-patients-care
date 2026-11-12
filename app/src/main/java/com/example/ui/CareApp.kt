package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ContactPhone
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.components.EmergencyAlertDialog
import com.example.ui.screens.CompanionScreen
import com.example.ui.screens.ContactsAndTeamScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.VitalsScreen
import com.example.ui.screens.VoiceAndComfortScreen
import com.example.viewmodel.CareViewModel

enum class CareTab(
  val labelEn: String,
  val labelAr: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  TASKS("Meds", "أدوية الوالدة", Icons.Filled.Medication, Icons.Outlined.Medication),
  VOICE_COMFORT("Voice", "صوت وسكينة", Icons.Filled.RecordVoiceOver, Icons.Outlined.RecordVoiceOver),
  VITALS("Vitals", "المؤشرات", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
  COMPANION("Companion", "الرفيق والذاكرة", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
  CONTACTS("Supervisor", "المشرف والعائلة", Icons.Filled.ContactPhone, Icons.Outlined.ContactPhone)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareApp(
  viewModel: CareViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val language by viewModel.language.collectAsState()
  val isLargeText by viewModel.largeTextMode.collectAsState()
  val isTtsEnabled by viewModel.ttsEnabled.collectAsState()

  val reminders by viewModel.medicationReminders.collectAsState()
  val contacts by viewModel.contacts.collectAsState()
  val vitalsHistory by viewModel.vitalsHistory.collectAsState()
  val chatMessages by viewModel.chatMessages.collectAsState()
  val currentAssessment by viewModel.currentAssessment.collectAsState()
  val showEmergencyAlert by viewModel.showEmergencyAlert.collectAsState()
  val lastNotifiedMedication by viewModel.lastNotifiedMedication.collectAsState()
  val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
  val musicVolume by viewModel.musicVolume.collectAsState()
  val motherFeedbackStatus by viewModel.motherFeedbackStatus.collectAsState()
  val isLetterAudioPlaying by viewModel.isLetterAudioPlaying.collectAsState()

  var currentTab by remember { mutableStateOf(CareTab.TASKS) }

  // BackHandler: Return to TASKS screen on back press
  BackHandler(enabled = currentTab != CareTab.TASKS) {
    currentTab = CareTab.TASKS
  }

  val isArabic = language == AppLanguage.ARABIC
  val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    Scaffold(
      modifier = modifier.fillMaxSize(),
      topBar = {
        CenterAlignedTopAppBar(
          title = {
            Text(
              text = if (isArabic) "محمد • رعاية الوالدة الغالية" else "Mohamed • Mother's Care",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (isLargeText) 20.sp else 18.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
          },
          colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          actions = {
            // Text size accessibility toggle
            IconButton(
              onClick = { viewModel.toggleLargeText() },
              modifier = Modifier.testTag("toggle_text_size_button")
            ) {
              Icon(
                imageVector = Icons.Default.FormatSize,
                contentDescription = "Toggle Text Size",
                tint = if (isLargeText) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            // Audio TTS voice toggle
            IconButton(
              onClick = { viewModel.toggleTts() },
              modifier = Modifier.testTag("toggle_tts_button")
            ) {
              Icon(
                imageVector = if (isTtsEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                contentDescription = "Toggle Voice Reminders",
                tint = if (isTtsEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            // Language Switcher (AR / EN)
            IconButton(
              onClick = { viewModel.toggleLanguage() },
              modifier = Modifier.testTag("toggle_language_button")
            ) {
              Text(
                text = if (isArabic) "EN" else "عربي",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        )
      },
      bottomBar = {
        NavigationBar(
          containerColor = MaterialTheme.colorScheme.surface,
          tonalElevation = 6.dp
        ) {
          CareTab.values().forEach { tab ->
            val isSelected = currentTab == tab
            val label = if (isArabic) tab.labelAr else tab.labelEn

            NavigationBarItem(
              selected = isSelected,
              onClick = { currentTab = tab },
              icon = {
                Icon(
                  imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                  contentDescription = label,
                  modifier = Modifier.size(24.dp)
                )
              },
              label = {
                Text(
                  text = label,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
            )
          }
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        when (currentTab) {
          CareTab.TASKS -> {
            TasksScreen(
              reminders = reminders,
              language = language,
              isLargeText = isLargeText,
              lastNotifiedMedication = lastNotifiedMedication,
              isLetterAudioPlaying = isLetterAudioPlaying,
              onToggleLetterAudio = { viewModel.toggleLetterWithSoftMusic() },
              onConfirmTaken = { ctx, rem -> viewModel.confirmMedicationTaken(ctx, rem) },
              onSpeak9AmReminder = { viewModel.speak9AmMedicationReminder() },
              onSpeakReminder = { viewModel.speakReminder(it) },
              onResetDaily = { viewModel.resetDailyMedications() },
              onAddMedication = { n, ne, d, de, t, c, p ->
                viewModel.addMedication(n, ne, d, de, t, c, p)
              },
              onDeleteMedication = { viewModel.deleteMedication(it) },
              onDismissNotificationBanner = { viewModel.dismissSupervisorNotification() }
            )
          }
          CareTab.VOICE_COMFORT -> {
            VoiceAndComfortScreen(
              isMusicPlaying = isMusicPlaying,
              musicVolume = musicVolume,
              language = language,
              isLargeText = isLargeText,
              motherFeedbackStatus = motherFeedbackStatus,
              isLetterAudioPlaying = isLetterAudioPlaying,
              onToggleMusic = { viewModel.toggleCalmingMusic() },
              onSetMusicVolume = { viewModel.setMusicVolume(it) },
              onSpeakText = { viewModel.speak(it) },
              onSpeakLetter = { viewModel.toggleLetterWithSoftMusic() },
              onMotherRespond = { type, ctx -> viewModel.handleMotherResponse(type, ctx) },
              onDismissFeedbackStatus = { viewModel.dismissMotherFeedbackStatus() }
            )
          }
          CareTab.VITALS -> {
            VitalsScreen(
              vitalsHistory = vitalsHistory,
              currentAssessment = currentAssessment,
              language = language,
              isLargeText = isLargeText,
              onAnalyzeVitals = { s, d, p -> viewModel.analyzeAndRecordVitals(s, d, p) },
              onSpeakText = { viewModel.speak(it) }
            )
          }
          CareTab.COMPANION -> {
            CompanionScreen(
              chatMessages = chatMessages,
              language = language,
              isLargeText = isLargeText,
              onSendMessage = { viewModel.sendEmpathyMessage(it) },
              onSpeakText = { viewModel.speak(it) },
              onClearChat = { viewModel.clearChat() }
            )
          }
          CareTab.CONTACTS -> {
            ContactsAndTeamScreen(
              contacts = contacts,
              language = language,
              isLargeText = isLargeText,
              isLetterAudioPlaying = isLetterAudioPlaying,
              onToggleLetterAudio = { viewModel.toggleLetterWithSoftMusic() },
              onCall = { ctx, num -> viewModel.dialPhoneNumber(ctx, num) },
              onSms = { ctx, num, text -> viewModel.sendSmsMessage(ctx, num, text) },
              onAddContact = { n, na, num, r -> viewModel.addContact(n, na, num, r) },
              onDeleteContact = { viewModel.deleteContact(it) }
            )
          }
        }

        // Emergency Alert Dialog
        if (showEmergencyAlert) {
          EmergencyAlertDialog(
            language = language,
            onDismiss = { viewModel.dismissEmergencyAlert() },
            onCallHelp = { num ->
              viewModel.dismissEmergencyAlert()
              viewModel.dialPhoneNumber(context, num)
            }
          )
        }
      }
    }
  }
}
