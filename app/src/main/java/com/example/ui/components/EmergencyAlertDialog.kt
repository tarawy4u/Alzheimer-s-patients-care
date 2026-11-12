package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.HealthCriticalContainer
import com.example.ui.theme.HealthCriticalRed

@Composable
fun EmergencyAlertDialog(
  language: AppLanguage,
  onDismiss: () -> Unit,
  onCallHelp: (number: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val isArabic = language == AppLanguage.ARABIC
  val title = if (isArabic) "⚠️ تنبيه حرج: ارتفاع ضغط دم خطير!" else "⚠️ CRITICAL ALERT: Hypertensive Crisis!"
  val message = if (isArabic) {
    "قراءة ضغط الدم مرتفعة جداً (الانقباضي ≥ 180 أو الانبساطي ≥ 120). يلزم اتخاذ تدابير إسعافية وطلب المساعدة الطبية أو إبلاغ المشرف الصحي فوراً!\n\n(ملاحظة تدريبية: الاتصال عبر خط الطوارئ 123 أو المساعدين)"
  } else {
    "Blood pressure is dangerously high (Systolic ≥ 180 or Diastolic ≥ 120). Immediate medical attention or reaching your caregiver is required!\n\n(Training demo: Dial Help Line 123 or Helper Contacts)"
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier.testTag("emergency_alert_dialog"),
    icon = {
      Icon(
        imageVector = Icons.Default.Warning,
        contentDescription = "Crisis Warning",
        tint = HealthCriticalRed,
        modifier = Modifier.size(48.dp)
      )
    },
    title = {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = HealthCriticalRed
        )
      )
    },
    text = {
      Column {
        Text(
          text = message,
          style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(HealthCriticalContainer)
            .padding(10.dp)
        ) {
          Text(
            text = if (isArabic) "الإجراءات الموصى بها:" else "Immediate Next Steps:",
            fontWeight = FontWeight.Bold,
            color = HealthCriticalRed,
            fontSize = 13.sp
          )
          Text(
            text = if (isArabic) "1. الجلوس بهدوء وتجنب أي مجهود\n2. الاتصال بالمساعد أو خط الإسعاف 123\n3. عدم تناول أدوية إضافية بدون استشارة الطبيب"
            else "1. Sit calmly and avoid physical exertion\n2. Contact caregiver or Emergency line 123\n3. Do not take unprescribed medicine",
            fontSize = 12.sp,
            color = HealthCriticalRed
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { onCallHelp("123") },
        colors = ButtonDefaults.buttonColors(containerColor = HealthCriticalRed),
        modifier = Modifier.testTag("call_emergency_button")
      ) {
        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (isArabic) "طلب خط الطوارئ (123)" else "Dial Help Line (123)")
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("dismiss_alert_button")
      ) {
        Text(if (isArabic) "فهمت، إغلاق" else "Understood, Dismiss")
      }
    }
  )
}
