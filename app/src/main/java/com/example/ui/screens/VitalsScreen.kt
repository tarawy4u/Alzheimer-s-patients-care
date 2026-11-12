package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.HealthAssessmentResult
import com.example.model.HealthStatusCode
import com.example.model.VitalCheck
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.HealthCriticalContainer
import com.example.ui.theme.HealthCriticalRed
import com.example.ui.theme.HealthOptimalContainer
import com.example.ui.theme.HealthOptimalGreen
import com.example.ui.theme.HealthStableBlue
import com.example.ui.theme.HealthStableContainer
import com.example.ui.theme.HealthWarningAmber
import com.example.ui.theme.HealthWarningContainer

@Composable
fun VitalsScreen(
  vitalsHistory: List<VitalCheck>,
  currentAssessment: HealthAssessmentResult?,
  language: AppLanguage,
  isLargeText: Boolean,
  onAnalyzeVitals: (systolic: Int, diastolic: Int, pulse: Int) -> Unit,
  onSpeakText: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val isArabic = language == AppLanguage.ARABIC

  var systolicInput by remember { mutableIntStateOf(120) }
  var diastolicInput by remember { mutableIntStateOf(80) }
  var pulseInput by remember { mutableIntStateOf(75) }

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

    // Hero Card with Generated Graphic
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(130.dp)
              .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
          ) {
            Image(
              painter = painterResource(id = R.drawable.care_hero_banner),
              contentDescription = "Care Banner",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
            )
            Text(
              text = if (isArabic) "محرّك التحليل السريري الذكي" else "AI Clinical Analytics Engine",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
            )
          }

          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (isArabic) "مراقبة العلامات الحيوية لكبار السن" else "Vitals Monitoring & Health Assessment",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = titleSize
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (isArabic)
                "أدخل قراءات ضغط الدم (الانقباضي/الانبساطي) ومعدل النبض للتحقق الآني بواسطة خوارزمية الذكاء الاصطناعي."
              else
                "Enter systolic & diastolic blood pressure plus pulse to run simulated AI expert clinical evaluation.",
              style = MaterialTheme.typography.bodyMedium.copy(fontSize = (bodySize.value - 2).sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Quick presets for professor/examiner testing
    item {
      Column {
        Text(
          text = if (isArabic) "نماذج اختبار سريعة (للمشروع والتقييم):" else "Quick Test Presets (For Demo & Grading):",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            AssistChip(
              onClick = {
                systolicInput = 120
                diastolicInput = 80
                pulseInput = 72
              },
              label = { Text("🩺 Optimal: 120/80") },
              modifier = Modifier.testTag("preset_optimal")
            )
          }
          item {
            AssistChip(
              onClick = {
                systolicInput = 185
                diastolicInput = 125
                pulseInput = 92
              },
              label = { Text("⚠️ Crisis: 185/125") },
              modifier = Modifier.testTag("preset_crisis")
            )
          }
          item {
            AssistChip(
              onClick = {
                systolicInput = 85
                diastolicInput = 55
                pulseInput = 60
              },
              label = { Text("⚠️ Low: 85/55") },
              modifier = Modifier.testTag("preset_low")
            )
          }
          item {
            AssistChip(
              onClick = {
                systolicInput = 118
                diastolicInput = 76
                pulseInput = 45
              },
              label = { Text("💓 Bradycardia: 45 bpm") }
            )
          }
          item {
            AssistChip(
              onClick = {
                systolicInput = 126
                diastolicInput = 82
                pulseInput = 128
              },
              label = { Text("⚡ Tachycardia: 128 bpm") }
            )
          }
        }
      }
    }

    // Input Controllers
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          // 1. Systolic (Top)
          VitalInputRow(
            label = if (isArabic) "الضغط الانقباضي (العلوي)" else "Systolic BP (Top)",
            unit = "mmHg",
            value = systolicInput,
            onValueChange = { systolicInput = it },
            testTagPrefix = "systolic"
          )

          Divider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.surfaceVariant)

          // 2. Diastolic (Bottom)
          VitalInputRow(
            label = if (isArabic) "الضغط الانبساطي (السفلي)" else "Diastolic BP (Bottom)",
            unit = "mmHg",
            value = diastolicInput,
            onValueChange = { diastolicInput = it },
            testTagPrefix = "diastolic"
          )

          Divider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.surfaceVariant)

          // 3. Pulse (Heart Rate)
          VitalInputRow(
            label = if (isArabic) "نبضات القلب (النبض)" else "Heart Rate / Pulse",
            unit = "bpm",
            value = pulseInput,
            onValueChange = { pulseInput = it },
            testTagPrefix = "pulse"
          )

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = {
              onAnalyzeVitals(systolicInput, diastolicInput, pulseInput)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(54.dp)
              .testTag("analyze_vitals_button"),
            shape = RoundedCornerShape(14.dp)
          ) {
            Icon(Icons.Default.Analytics, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isArabic) "تشغيل التحليل السريري بالذكاء الاصطناعي" else "Run AI Health Assessment",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // AI Analysis Result Display
    item {
      AnimatedVisibility(
        visible = currentAssessment != null,
        enter = fadeIn() + slideInVertically()
      ) {
        currentAssessment?.let { result ->
          val (badgeColor, containerColor) = when (result.statusCode) {
            HealthStatusCode.OPTIMAL -> Pair(HealthOptimalGreen, HealthOptimalContainer)
            HealthStatusCode.STABLE -> Pair(HealthStableBlue, HealthStableContainer)
            HealthStatusCode.WARNING -> Pair(HealthWarningAmber, HealthWarningContainer)
            HealthStatusCode.CRITICAL_HIGH, HealthStatusCode.CRITICAL_LOW -> Pair(HealthCriticalRed, HealthCriticalContainer)
          }

          val assessmentText = if (isArabic) result.messageAr else result.messageEn

          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("vital_assessment_result_card")
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = badgeColor
                ) {
                  Text(
                    text = result.statusCode.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                  )
                }

                IconButton(
                  onClick = { onSpeakText(assessmentText) },
                  modifier = Modifier
                    .size(40.dp)
                    .background(badgeColor.copy(alpha = 0.15f), CircleShape)
                ) {
                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Read out loud",
                    tint = badgeColor
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = assessmentText,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = (bodySize.value + 1).sp,
                  lineHeight = 24.sp
                ),
                color = badgeColor
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = if (isArabic) "💡 نصيحة: التسجيل المنتظم للعلامات الحيوية يساعد طبيبك المعالج في تقديم رعاية أفضل."
                else "💡 Tip: Regular tracking helps your doctor provide better, more accurate care.",
                style = MaterialTheme.typography.bodySmall,
                color = badgeColor.copy(alpha = 0.85f)
              )
            }
          }
        }
      }
    }

    // Historical Logs
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isArabic) "سجل القراءات السابقة" else "Recent Health Check Logs",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Text(
          text = "${vitalsHistory.size} ${if (isArabic) "قراءات" else "entries"}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    items(vitalsHistory, key = { it.id }) { vital ->
      val (badgeColor, containerColor) = when (vital.statusCode) {
        HealthStatusCode.OPTIMAL -> Pair(HealthOptimalGreen, HealthOptimalContainer)
        HealthStatusCode.STABLE -> Pair(HealthStableBlue, HealthStableContainer)
        HealthStatusCode.WARNING -> Pair(HealthWarningAmber, HealthWarningContainer)
        HealthStatusCode.CRITICAL_HIGH, HealthStatusCode.CRITICAL_LOW -> Pair(HealthCriticalRed, HealthCriticalContainer)
      }

      val logMsg = if (isArabic) vital.messageAr else vital.messageEn

      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(badgeColor)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "${vital.systolic}/${vital.diastolic} mmHg • ${vital.pulse} bpm",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = vital.timestamp,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = logMsg,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 2
            )
          }
        }
      }
    }
  }
}

@Composable
fun VitalInputRow(
  label: String,
  unit: String,
  value: Int,
  onValueChange: (Int) -> Unit,
  testTagPrefix: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "$value $unit",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
      )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      IconButton(
        onClick = { if (value > 30) onValueChange(value - 5) },
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .testTag("${testTagPrefix}_decrement")
      ) {
        Icon(Icons.Default.Remove, contentDescription = "Decrease")
      }

      Spacer(modifier = Modifier.width(12.dp))

      IconButton(
        onClick = { if (value < 260) onValueChange(value + 5) },
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary)
          .testTag("${testTagPrefix}_increment")
      ) {
        Icon(Icons.Default.Add, contentDescription = "Increase", tint = MaterialTheme.colorScheme.onPrimary)
      }
    }
  }
}
