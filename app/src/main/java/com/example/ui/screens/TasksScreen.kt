package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MedicationReminderEntity
import com.example.model.AppLanguage
import com.example.ui.theme.HealthOptimalContainer
import com.example.ui.theme.HealthOptimalGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
  reminders: List<MedicationReminderEntity>,
  language: AppLanguage,
  isLargeText: Boolean,
  lastNotifiedMedication: String?,
  isLetterAudioPlaying: Boolean = false,
  onToggleLetterAudio: (() -> Unit)? = null,
  onConfirmTaken: (Context, MedicationReminderEntity) -> Unit,
  onSpeak9AmReminder: () -> Unit,
  onSpeakReminder: (MedicationReminderEntity) -> Unit,
  onResetDaily: () -> Unit,
  onAddMedication: (name: String, nameEn: String, dosage: String, dosageEn: String, time: String, category: String, prompt: String) -> Unit,
  onDeleteMedication: (Long) -> Unit,
  onDismissNotificationBanner: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isArabic = language == AppLanguage.ARABIC
  var showAddSheet by remember { mutableStateOf(false) }

  val completedCount = reminders.count { it.isCompleted }
  val totalCount = reminders.size
  val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

  val titleSize = if (isLargeText) 26.sp else 22.sp
  val bodySize = if (isLargeText) 18.sp else 15.sp

  // Time & Date format for Alzheimer's cognitive orientation
  val locale = if (isArabic) Locale("ar") else Locale.ENGLISH
  val dayFormat = SimpleDateFormat("EEEE", locale)
  val dateFormat = SimpleDateFormat("d MMMM yyyy", locale)
  val currentDay = dayFormat.format(Date())
  val currentDate = dateFormat.format(Date())

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Alzheimer's Daily Orientation Banner
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("alzheimer_orientation_banner")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = if (isArabic) "صباح الخير يا أمي الغالية ❤️" else "Good Morning, Dear Mother ❤️",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = titleSize
                  ),
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "$currentDay • $currentDate",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (bodySize.value + 1).sp
                  ),
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = if (isArabic)
                "يوم مبارك بصحة وسكينة. هذا التطبيق يرافقك خطوة بخطوة لمواعيد دوائك وطمأنة المشرف الطبي فور أخذ الجرعة."
              else
                "A blessed day of health and peace. This companion gently guides your daily medications and notifies your supervisor.",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = (bodySize.value - 2).sp, lineHeight = 20.sp),
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
            )
          }
        }
      }

      // 2. Son's Message to Mother & Scientific Disclaimer (Beginning of App)
      item {
        com.example.ui.components.MotherCareDedicationAndDisclaimerCard(
          language = language,
          isEndPosition = false,
          isLetterAudioPlaying = isLetterAudioPlaying,
          onToggleLetterAudio = onToggleLetterAudio
        )
      }

      // 3. 9:00 AM Priority Voice Reminder Card (Blood Pressure & Diabetes)
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0F2FE)
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("nine_am_priority_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = CircleShape,
                  color = Color(0xFF0369A1),
                  modifier = Modifier.size(44.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(
                      Icons.Default.NotificationsActive,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(24.dp)
                    )
                  }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = if (isArabic) "⏰ أدوية الساعة 9:00 صباحاً" else "⏰ 9:00 AM Medications",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 17.sp,
                      color = Color(0xFF0C4A6E)
                    )
                  )
                  Text(
                    text = if (isArabic) "دواء الضغط + دواء السكر مع وجبة الإفطار" else "Blood Pressure + Diabetes with Breakfast",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF0369A1)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = onSpeak9AmReminder,
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("play_9am_voice_reminder_button")
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isArabic) "🔊 استمعي للتذكير الصوتي للوالدة (9:00 ص)" else "🔊 Play 9:00 AM Voice Reminder",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          }
        }
      }

      // 3. Supervisor Notification Confirmation Banner
      item {
        AnimatedVisibility(visible = lastNotifiedMedication != null) {
          lastNotifiedMedication?.let { medName ->
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = HealthOptimalContainer),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = HealthOptimalGreen,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = if (isArabic) "✅ تم إشعار المشرف الطبي بنجاح" else "✅ Medical Supervisor Notified",
                    fontWeight = FontWeight.Bold,
                    color = HealthOptimalGreen,
                    fontSize = 14.sp
                  )
                  Text(
                    text = if (isArabic) "تم إرسال تأكيد أخذ جرعة [$medName] بالكامل."
                    else "Dose confirmation for [$medName] sent successfully.",
                    fontSize = 12.sp,
                    color = HealthOptimalGreen
                  )
                }
                IconButton(onClick = onDismissNotificationBanner) {
                  Text("✕", color = HealthOptimalGreen, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // 4. Progress and Reset Bar
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (isArabic) "متابعة أدوية اليوم ($completedCount/$totalCount)"
                else "Today's Intake Status ($completedCount/$totalCount)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
              )

              IconButton(onClick = onResetDaily) {
                Icon(
                  Icons.Default.RestartAlt,
                  contentDescription = "Reset for new day",
                  tint = MaterialTheme.colorScheme.primary
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = HealthOptimalGreen,
              trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
          }
        }
      }

      // 5. Medication Reminders List (From Room Database)
      items(reminders, key = { it.id }) { reminder ->
        val name = if (isArabic) reminder.name else reminder.nameEn.ifBlank { reminder.name }
        val dosage = if (isArabic) reminder.dosage else reminder.dosageEn.ifBlank { reminder.dosage }

        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (reminder.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = if (reminder.isCompleted) 0.dp else 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("medication_card_${reminder.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Pill or Water Icon
              val iconVector = when (reminder.category) {
                "HYDRATION" -> Icons.Default.LocalDrink
                else -> Icons.Default.Medication
              }

              Surface(
                shape = CircleShape,
                color = if (reminder.isCompleted) HealthOptimalGreen.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                modifier = Modifier.size(48.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = if (reminder.isCompleted) HealthOptimalGreen else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.width(12.dp))

              // Title and Dosage
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = name,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = (bodySize.value + 1).sp,
                    textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                  ),
                  color = if (reminder.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                  else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 6.dp)
                  ) {
                    Text(
                      text = "📦 $dosage",
                      fontSize = 12.sp,
                      color = MaterialTheme.colorScheme.onSecondaryContainer,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }

                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = reminder.scheduledTime,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                      )
                    }
                  }
                }
              }

              // Audio Listen Button
              IconButton(
                onClick = { onSpeakReminder(reminder) },
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
              ) {
                Icon(
                  Icons.Default.VolumeUp,
                  contentDescription = "Listen to audio reminder",
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Confirm Intake Button (with supervisor alert)
            Button(
              onClick = { onConfirmTaken(context, reminder) },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (reminder.isCompleted) HealthOptimalGreen else MaterialTheme.colorScheme.primary
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("confirm_medication_button_${reminder.id}")
            ) {
              Icon(
                imageVector = if (reminder.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (reminder.isCompleted) {
                  if (isArabic) "✅ تم أخذ الدواء وتنبيه المشرف الطبي" else "✅ Taken • Supervisor Notified"
                } else {
                  if (isArabic) "تأكيد أخذ الدواء الآن وإشعار المشرف" else "Confirm Taken & Notify Supervisor"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
            }
          }
        }
      }

      // Son's Message to Mother & Scientific Disclaimer (End of App)
      item {
        com.example.ui.components.MotherCareDedicationAndDisclaimerCard(
          language = language,
          isEndPosition = true,
          isLetterAudioPlaying = isLetterAudioPlaying,
          onToggleLetterAudio = onToggleLetterAudio
        )
      }
    }

    // Floating Action Button to Add Medication
    FloatingActionButton(
      onClick = { showAddSheet = true },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(bottom = 24.dp, end = 20.dp)
        .testTag("add_medication_fab"),
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
      Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Add, contentDescription = "Add Medication")
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (isArabic) "إضافة دواء" else "Add Medicine")
      }
    }

    // Modal Bottom Sheet for adding new medication reminder
    if (showAddSheet) {
      AddMedicationBottomSheet(
        language = language,
        onDismiss = { showAddSheet = false },
        onSave = { name, nameEn, dosage, dosageEn, time, category, prompt ->
          onAddMedication(name, nameEn, dosage, dosageEn, time, category, prompt)
          showAddSheet = false
        }
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationBottomSheet(
  language: AppLanguage,
  onDismiss: () -> Unit,
  onSave: (name: String, nameEn: String, dosage: String, dosageEn: String, time: String, category: String, prompt: String) -> Unit
) {
  val isArabic = language == AppLanguage.ARABIC
  var name by remember { mutableStateOf("") }
  var dosage by remember { mutableStateOf("1 قرص") }
  var scheduleTime by remember { mutableStateOf("09:00") }
  var category by remember { mutableStateOf("GENERAL") }
  var categoryExpanded by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 12.dp)
        .padding(bottom = 32.dp)
    ) {
      Text(
        text = if (isArabic) "إضافة دواء جديد لجدول الوالدة" else "Add New Medication for Mother",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text(if (isArabic) "اسم الدواء" else "Medication Name") },
        placeholder = { Text(if (isArabic) "مثال: دواء الضغط، دواء السكر" else "e.g. Blood Pressure") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("medication_name_input"),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = dosage,
          onValueChange = { dosage = it },
          label = { Text(if (isArabic) "الجرعة" else "Dosage") },
          modifier = Modifier.weight(1f),
          singleLine = true
        )

        OutlinedTextField(
          value = scheduleTime,
          onValueChange = { scheduleTime = it },
          label = { Text(if (isArabic) "الوقت (24س)" else "Time") },
          placeholder = { Text("09:00") },
          modifier = Modifier.weight(1f),
          singleLine = true
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      ExposedDropdownMenuBox(
        expanded = categoryExpanded,
        onExpandedChange = { categoryExpanded = !categoryExpanded }
      ) {
        OutlinedTextField(
          value = category,
          onValueChange = {},
          readOnly = true,
          label = { Text(if (isArabic) "نوع العلاج" else "Category") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
          modifier = Modifier
            .fillMaxWidth()
            .menuAnchor()
        )
        ExposedDropdownMenu(
          expanded = categoryExpanded,
          onDismissRequest = { categoryExpanded = false }
        ) {
          listOf("BLOOD_PRESSURE", "DIABETES", "HYDRATION", "GENERAL").forEach { cat ->
            DropdownMenuItem(
              text = { Text(cat) },
              onClick = {
                category = cat
                categoryExpanded = false
              }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        OutlinedButton(onClick = onDismiss) {
          Text(if (isArabic) "إلغاء" else "Cancel")
        }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
          onClick = {
            if (name.isNotBlank()) {
              val prompt = if (isArabic) "يا أمي الغالية، حان الآن موعد أخذ $name ($dosage)." else "Mother, time for $name."
              onSave(name, name, dosage, dosage, scheduleTime, category, prompt)
            }
          },
          enabled = name.isNotBlank(),
          modifier = Modifier.testTag("save_medication_button")
        ) {
          Text(if (isArabic) "حفظ الدواء" else "Save Medicine")
        }
      }
    }
  }
}
