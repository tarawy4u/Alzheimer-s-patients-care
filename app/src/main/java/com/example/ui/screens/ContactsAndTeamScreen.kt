package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.HelperContact
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.HealthCriticalContainer
import com.example.ui.theme.HealthCriticalRed

@Composable
fun ContactsAndTeamScreen(
  contacts: List<HelperContact>,
  language: AppLanguage,
  isLargeText: Boolean,
  isLetterAudioPlaying: Boolean = false,
  onToggleLetterAudio: (() -> Unit)? = null,
  onCall: (Context, String) -> Unit,
  onSms: (Context, String, String) -> Unit,
  onAddContact: (name: String, nameAr: String, number: String, relationship: String) -> Unit,
  onDeleteContact: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isArabic = language == AppLanguage.ARABIC
  var showAddDialog by remember { mutableStateOf(false) }

  val titleSize = if (isLargeText) 26.sp else 22.sp
  val bodySize = if (isLargeText) 18.sp else 15.sp

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      MedicalDisclaimerBanner(language = language)
    }

    // Emergency Line (123)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = HealthCriticalContainer),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("emergency_hotline_card")
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
                color = HealthCriticalRed,
                modifier = Modifier.size(44.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    Icons.Default.Emergency,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = if (isArabic) "خط الطوارئ السريع (123)" else "Emergency Assistance (123)",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HealthCriticalRed,
                    fontSize = 17.sp
                  )
                )
                Text(
                  text = if (isArabic) "للاتصال الفوري في الحالات الحرجة" else "Instant emergency dialer",
                  style = MaterialTheme.typography.bodySmall,
                  color = HealthCriticalRed.copy(alpha = 0.85f)
                )
              }
            }

            Button(
              onClick = { onCall(context, "123") },
              colors = ButtonDefaults.buttonColors(containerColor = HealthCriticalRed),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("dial_123_button")
            ) {
              Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("123", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Medical Supervisor & Caregiver Contacts
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isArabic) "المشرف الطبي ومقدم الرعاية" else "Medical Supervisor & Family",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = (titleSize.value - 4).sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        OutlinedButton(
          onClick = { showAddDialog = true },
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(if (isArabic) "إضافة" else "Add", fontSize = 13.sp)
        }
      }
    }

    items(contacts.filterNot { it.isEmergencyDemo }, key = { it.id }) { contact ->
      val name = if (isArabic && contact.nicknameAr.isNotBlank()) contact.nicknameAr else contact.nickname
      val relation = if (isArabic && contact.relationshipAr.isNotBlank()) contact.relationshipAr else contact.relationship

      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(46.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = name.take(1),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = name,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = bodySize),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = relation,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = contact.number,
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.primary
            )
          }

          Row {
            IconButton(
              onClick = { onCall(context, contact.number) },
              modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
            ) {
              Icon(
                Icons.Default.Call,
                contentDescription = "Call $name",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
              onClick = {
                val smsMsg = if (isArabic)
                  "السلام عليكم، أرسل لك تحديثاً عن حالة الوالدة ومتابعة أدوية الضغط والسكر."
                else
                  "Hello, sending an update on mother's condition and medications."
                onSms(context, contact.number, smsMsg)
              },
              modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
            ) {
              Icon(
                Icons.Default.Message,
                contentDescription = "Message $name",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }

    // Alzheimer's Loving Care Guidance Section
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp)
          .testTag("alzheimer_guidance_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              Icons.Default.Favorite,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = if (isArabic) "إرشادات برّ ورعاية الوالدة (مريضة الزهايمر)" else "Mother's Alzheimer Care Principles",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isArabic) "رعاية ست الحبايب • رفق ومودة واطمئنان" else "Gentle, respectful dementia care principles",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          val points = if (isArabic) {
            listOf(
              "❤️ التحدث دائماً بنبرة صوت دافئة وهادئة، وتجنب إشعارها بالنسيان أو التقصير.",
              "⏰ تثبيت روتين يومي ثابت لمواعيد دوائها (دواء الضغط والسكر الساعة 9:00 صباحاً بانتظام).",
              "🥛 تشجيعها على شرب الماء بلطف وتوفير بيئة هادئة ومريحة خالية من الضوضاء.",
              "📲 إرسال تأكيد الجرعة فورياً للمشرفة الطبية للاطمئنان والتنسيق المستمر.",
              "🤲 الإكثار من الدعاء لها وإظهار التقدير والمحبة ورد الجميل في كل لحظة."
            )
          } else {
            listOf(
              "❤️ Speak with a warm, gentle, calm voice; avoid pointing out memory lapses.",
              "⏰ Maintain a predictable daily routine with 9:00 AM medications.",
              "🥛 Encourage hydration and maintain a quiet, comfortable living space.",
              "📲 Keep the medical supervisor updated upon dose confirmations.",
              "🤲 Surround her with appreciation, patience, and comforting reassurance."
            )
          }

          points.forEach { point ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Text(
                text = point,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
              )
            }
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

  // Add Contact Dialog
  if (showAddDialog) {
    var newName by remember { mutableStateOf("") }
    var newNumber by remember { mutableStateOf("") }
    var newRelation by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = {
        Text(
          text = if (isArabic) "إضافة جهة اتصال" else "Add Contact",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text(if (isArabic) "الاسم" else "Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = newNumber,
            onValueChange = { newNumber = it },
            label = { Text(if (isArabic) "رقم الهاتف" else "Phone Number") },
            placeholder = { Text("01xxxxxxxxx") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = newRelation,
            onValueChange = { newRelation = it },
            label = { Text(if (isArabic) "الدور / المشرف" else "Role") },
            placeholder = { Text(if (isArabic) "طبيب معالج، ممرضة، فرد عائلة" else "Doctor, Caregiver") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newName.isNotBlank() && newNumber.isNotBlank()) {
              onAddContact(newName, newName, newNumber, newRelation)
              showAddDialog = false
            }
          },
          enabled = newName.isNotBlank() && newNumber.isNotBlank()
        ) {
          Text(if (isArabic) "إضافة" else "Add")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showAddDialog = false }) {
          Text(if (isArabic) "إلغاء" else "Cancel")
        }
      }
    )
  }
}
