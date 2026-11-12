package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.HealthCriticalRed
import com.example.ui.theme.HealthOptimalContainer
import com.example.ui.theme.HealthOptimalGreen
import com.example.viewmodel.MotherResponseType
import java.util.Calendar

enum class DayTimeSlot(
  val labelAr: String,
  val labelEn: String,
  val icon: ImageVector,
  val timeDesc: String
) {
  MORNING("الصباح والفطور", "Morning & Breakfast", Icons.Default.WbSunny, "06:00 - 11:59"),
  AFTERNOON("الظهيرة والغداء", "Afternoon & Lunch", Icons.Default.LightMode, "12:00 - 16:59"),
  EVENING("المساء والعشاء", "Evening & Dinner", Icons.Default.NightsStay, "17:00 - 20:59"),
  NIGHT("الليل وقبل النوم", "Night & Sleep", Icons.Default.Bedtime, "21:00 - 05:59")
}

data class TimeBasedVoicePrompt(
  val id: String,
  val slot: DayTimeSlot,
  val textAr: String,
  val textEn: String,
  val iconColor: Color,
  val topicTag: String // "FOOD", "WATER", "HEALTH", "EMOTION"
)

@Composable
fun VoiceAndComfortScreen(
  isMusicPlaying: Boolean,
  musicVolume: Float,
  language: AppLanguage,
  isLargeText: Boolean,
  motherFeedbackStatus: String?,
  isLetterAudioPlaying: Boolean = false,
  onToggleMusic: () -> Unit,
  onSetMusicVolume: (Float) -> Unit,
  onSpeakText: (String) -> Unit,
  onSpeakLetter: () -> Unit,
  onMotherRespond: (MotherResponseType, Context) -> Unit,
  onDismissFeedbackStatus: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isArabic = language == AppLanguage.ARABIC

  val titleSize = if (isLargeText) 26.sp else 22.sp
  val bodySize = if (isLargeText) 18.sp else 15.sp

  // Calculate the current active time slot based on device hour
  val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
  val initialSlot = remember(currentHour) {
    when (currentHour) {
      in 6..11 -> DayTimeSlot.MORNING
      in 12..16 -> DayTimeSlot.AFTERNOON
      in 17..20 -> DayTimeSlot.EVENING
      else -> DayTimeSlot.NIGHT
    }
  }

  var selectedSlot by remember { mutableStateOf(initialSlot) }

  // Dynamic voice prompts throughout the day (Never repeating static prompts, asking about food & water)
  val allPrompts = remember {
    listOf(
      // 1. MORNING (الصباح والفطور)
      TimeBasedVoicePrompt(
        id = "m1",
        slot = DayTimeSlot.MORNING,
        textAr = "إزيك يا ست الكل؟ أنا ابنك محمد، صباحك ومساؤك رضا وعافية. هل فطرتي وشربتي ماء كويس النهاردة؟",
        textEn = "How are you doing, lady of everyone? It's your son Mohamed. Did you have breakfast and fresh water today?",
        iconColor = Color(0xFF0284C7),
        topicTag = "FOOD"
      ),
      TimeBasedVoicePrompt(
        id = "m2",
        slot = DayTimeSlot.MORNING,
        textAr = "يا أمي الغالية، بفكرك بمعاد دواء الضغط ودواء السكر في تمام الساعة 9:00 صباحاً مع وجبة الإفطار وكوب ماء.",
        textEn = "Dear mother, reminding you of your 9:00 AM Blood Pressure and Diabetes medications with breakfast and water.",
        iconColor = Color(0xFF059669),
        topicTag = "HEALTH"
      ),
      TimeBasedVoicePrompt(
        id = "m3",
        slot = DayTimeSlot.MORNING,
        textAr = "مش محتاجة مني أي حاجة يا أمي؟ ابنك محمد في خدمتك وتحت أمرك في أي وقت.",
        textEn = "Don't you need anything from me, mother? Your son Mohamed is always here in your service.",
        iconColor = Color(0xFF0D9488),
        topicTag = "EMOTION"
      ),
      TimeBasedVoicePrompt(
        id = "m4",
        slot = DayTimeSlot.MORNING,
        textAr = "لو حسيتي بأي دوخة أو تعب في أول اليوم عرفيني فوراً عشان أطمن عليكي وأتصل بالدكتورة منى.",
        textEn = "If you feel any dizziness or fatigue, let me know right away so I can check on you and call Dr. Mona.",
        iconColor = Color(0xFFDC2626),
        topicTag = "HEALTH"
      ),

      // 2. AFTERNOON (الظهيرة والغداء)
      TimeBasedVoicePrompt(
        id = "a1",
        slot = DayTimeSlot.AFTERNOON,
        textAr = "مساء الخير يا أمي الحبيبة، ابنك محمد بيطمن عليكي: هل اتغديتي كويس وشربتي كأس ماء نقي يرطب جسمك؟",
        textEn = "Good afternoon, beloved mother! Your son Mohamed checking in: did you have lunch and drink a fresh glass of water?",
        iconColor = Color(0xFFD97706),
        topicTag = "FOOD"
      ),
      TimeBasedVoicePrompt(
        id = "a2",
        slot = DayTimeSlot.AFTERNOON,
        textAr = "شرب الماء في وقت الظهيرة نعمة وصحة يا ست الحبايب، بينشط الدورة الدموية وبيحافظ على توازن ضغطك.",
        textEn = "Drinking water midday revitalizes your circulation and keeps your blood pressure balanced, dearest mother.",
        iconColor = Color(0xFF0284C7),
        topicTag = "WATER"
      ),
      TimeBasedVoicePrompt(
        id = "a3",
        slot = DayTimeSlot.AFTERNOON,
        textAr = "فيه أي حاجة مضايقاكي يا أمي الغالية؟ احكي لابنك محمد وفضفضيلي، أنا سامعك بكل حب واهتمام.",
        textEn = "Is there anything upsetting you, dear mother? Talk to your son Mohamed, I am listening with deep love.",
        iconColor = Color(0xFF7C3AED),
        topicTag = "EMOTION"
      ),
      TimeBasedVoicePrompt(
        id = "a4",
        slot = DayTimeSlot.AFTERNOON,
        textAr = "صحتك عاملة إيه دلوقت؟ لو حاسة بأي ألم أو ثقل في جسمك عرفيني عشان أطمن عليكي فوراً.",
        textEn = "How is your health right now? If you feel any pain or discomfort, tell me immediately.",
        iconColor = Color(0xFFDC2626),
        topicTag = "HEALTH"
      ),

      // 3. EVENING (المساء والعشاء)
      TimeBasedVoicePrompt(
        id = "e1",
        slot = DayTimeSlot.EVENING,
        textAr = "مساء الورد والسكينة يا أغلى الناس، هل اتعشيتي وجبة خفيفة وشربتي شوية ماء؟",
        textEn = "Good evening and peace, dearest mother! Did you enjoy a light dinner and sip water?",
        iconColor = Color(0xFFEA580C),
        topicTag = "FOOD"
      ),
      TimeBasedVoicePrompt(
        id = "e2",
        slot = DayTimeSlot.EVENING,
        textAr = "طمنيني عليكي يا ست الكل، يومك كان عامل إيه؟ ابنك محمد دايماً معاكي وبيفكر فيكي وبيدعيلك في كل لحظة.",
        textEn = "Reassure me how you are doing! Your son Mohamed is always beside you, thinking of you and praying for you.",
        iconColor = Color(0xFF0D9488),
        topicTag = "EMOTION"
      ),
      TimeBasedVoicePrompt(
        id = "e3",
        slot = DayTimeSlot.EVENING,
        textAr = "ريحي جسمك يا أمي واقعدي في مكان هادئ ومريح، ومش محتاجة حاجة أجهزهالك بنفسي؟",
        textEn = "Rest comfortably in a calm space, mother. Don't you need anything I can prepare for you?",
        iconColor = Color(0xFF0284C7),
        topicTag = "EMOTION"
      ),
      TimeBasedVoicePrompt(
        id = "e4",
        slot = DayTimeSlot.EVENING,
        textAr = "لو حسيتي بأي ألم أو صداع في المساء اضغطي على زر التنبيه وابنك محمد والمشرفة الطبية هيكونوا معاكي.",
        textEn = "If you feel any pain or headache this evening, tap the alert button and we will assist you immediately.",
        iconColor = Color(0xFFDC2626),
        topicTag = "HEALTH"
      ),

      // 4. NIGHT (الليل وقبل النوم)
      TimeBasedVoicePrompt(
        id = "n1",
        slot = DayTimeSlot.NIGHT,
        textAr = "تصبحي على ألف خير وصحة وسكينة يا أمي الحبيبة. هل شربتي شوية ماء قبل النوم لراحة جسمك؟",
        textEn = "Good night and sweet dreams, beloved mother! Did you drink some water before sleep?",
        iconColor = Color(0xFF38BDF8),
        topicTag = "WATER"
      ),
      TimeBasedVoicePrompt(
        id = "n2",
        slot = DayTimeSlot.NIGHT,
        textAr = "نامي وارتاحي يا ست الحبايب وباب الجنة، ابنك محمد بيدعيلك في كل صلاة ربنا يحفظك ويبارك في عمرك وصحتك.",
        textEn = "Rest peacefully, gateway to Paradise. Your son Mohamed prays for your continuous health and peace.",
        iconColor = Color(0xFFA855F7),
        topicTag = "EMOTION"
      ),
      TimeBasedVoicePrompt(
        id = "n3",
        slot = DayTimeSlot.NIGHT,
        textAr = "شغلي موسيقى السكينة الهادئة بالأسفل عشان تنامي نوم عميق ومريح بدون أي قلق أو توتر.",
        textEn = "Turn on the calming ambient music below to enjoy deep, peaceful sleep free from anxiety.",
        iconColor = Color(0xFF06B6D4),
        topicTag = "HEALTH"
      ),
      TimeBasedVoicePrompt(
        id = "n4",
        slot = DayTimeSlot.NIGHT,
        textAr = "لو قلقتي في أي وقت بالليل أو حسيتي بدوخة، التليفون والتطبيق جنبك وهتلاقيني معاكي في ثواني.",
        textEn = "If you wake up or feel dizzy at night, the app is beside you and your son Mohamed is reachable in seconds.",
        iconColor = Color(0xFFDC2626),
        topicTag = "HEALTH"
      )
    )
  }

  val activePrompts = allPrompts.filter { it.slot == selectedSlot }

  // Pulsing animation when calming music plays
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

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

    // 1. Time-of-Day Filter Chips Row
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = selectedSlot.icon,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = if (isArabic) "فترة اليوم: ${selectedSlot.labelAr}" else "Time Slot: ${selectedSlot.labelEn}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isArabic) "تتغير الأسئلة الصوتية حسب الوقت الحالي (${selectedSlot.timeDesc})"
                else "Prompts dynamically change by time of day (${selectedSlot.timeDesc})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(DayTimeSlot.values()) { slot ->
            val isCurrentActive = slot == initialSlot
            val isSelected = slot == selectedSlot
            FilterChip(
              selected = isSelected,
              onClick = { selectedSlot = slot },
              label = {
                Text(
                  text = if (isArabic) {
                    if (isCurrentActive) "${slot.labelAr} (الآن)" else slot.labelAr
                  } else {
                    if (isCurrentActive) "${slot.labelEn} (Now)" else slot.labelEn
                  },
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 12.sp
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = slot.icon,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
              )
            )
          }
        }
      }
    }

    // 2. Interactive Food & Hydration Tracker (سؤال الوالدة عن الطعام والمياه)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color(0xFFFEF3C7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("food_and_water_tracker_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = CircleShape,
              color = Color(0xFFD97706),
              modifier = Modifier.size(38.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (isArabic) "سؤال الوالدة عن الطعام والمياه 🍞 🥛" else "Food & Hydration Check for Mother",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = Color(0xFF92400E)
                )
              )
              Text(
                text = if (isArabic) "طمني ابنكِ محمد على وجباتكِ وشربكِ للماء:" else "Reassure Mohamed about your meals & water:",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB45309)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Food & Water Quick Action Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { onMotherRespond(MotherResponseType.ATE_FOOD, context) },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("mother_ate_food_button")
            ) {
              Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isArabic) "أكلت والحمد لله" else "Ate my meal",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }

            Button(
              onClick = { onMotherRespond(MotherResponseType.DRANK_WATER, context) },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("mother_drank_water_button")
            ) {
              Icon(Icons.Default.LocalDrink, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isArabic) "شربت ماء كفاية" else "Drank water",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = { onMotherRespond(MotherResponseType.NEEDS_FOOD_OR_WATER, context) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("mother_needs_food_water_button")
          ) {
            Text(
              text = if (isArabic) "⚠️ محتاجة مساعدة في إحضار الطعام أو الماء (تنبيه محمد)" else "⚠️ Need assistance with food/water (Alert Mohamed)",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFF92400E)
            )
          }
        }
      }
    }

    // 3. Mother's Feedback Status Toast
    item {
      AnimatedVisibility(visible = motherFeedbackStatus != null) {
        motherFeedbackStatus?.let { status ->
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
              Text(
                text = status,
                color = HealthOptimalGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
              )
              IconButton(onClick = onDismissFeedbackStatus) {
                Text("✕", color = HealthOptimalGreen, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }

    // 4. Calming Music Player Card (Therapeutic Audio for Alzheimer's)
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color(0xFF0F172A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("calming_music_card")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .scale(if (isMusicPlaying) pulseScale else 1f)
                  .clip(CircleShape)
                  .background(if (isMusicPlaying) Color(0xFF38BDF8) else Color(0xFF334155)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (isMusicPlaying) Icons.Default.GraphicEq else Icons.Default.MusicNote,
                  contentDescription = null,
                  tint = if (isMusicPlaying) Color(0xFF0F172A) else Color.White,
                  modifier = Modifier.size(26.dp)
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column {
                Text(
                  text = if (isArabic) "موسيقى السكينة والاسترخاء للوالدة" else "Mother's Calming Ambient Music",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 17.sp
                  )
                )
                Text(
                  text = if (isArabic) "نغمات هادئة لتهدئة القلق والتوتر (432Hz)" else "Peaceful harmonic chords to ease agitation",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color(0xFF94A3B8)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Big Play/Pause Action Button
          Button(
            onClick = onToggleMusic,
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isMusicPlaying) Color(0xFFE11D48) else Color(0xFF0284C7)
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("toggle_music_button")
          ) {
            Icon(
              imageVector = if (isMusicPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = null,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = if (isMusicPlaying) {
                if (isArabic) "⏸️ إيقاف الموسيقى الهادئة مؤقتاً" else "⏸️ Pause Calming Music"
              } else {
                if (isArabic) "▶️ تشغيل موسيقى السكينة لراحة الوالدة" else "▶️ Play Calming Music for Mother"
              },
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Volume control slider
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              Icons.Default.Headphones,
              contentDescription = null,
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isArabic) "مستوى الصوت:" else "Volume:",
              color = Color(0xFF94A3B8),
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
              value = musicVolume,
              onValueChange = onSetMusicVolume,
              valueRange = 0.1f..1.0f,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }

    // 5. Mother's One-Tap Quick Response Section
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = if (isArabic) "إجابات الوالدة السريعة لطمأنة ابنها محمد ❤️" else "Mother's Quick Reassurance to Mohamed ❤️",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = (titleSize.value - 4).sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isArabic) "اضغطي على الزر المناسب لطمأنة ابنكِ محمد أو طلب مساعدته فوراً:" else "Tap to reassure Mohamed or alert him:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Response 1: Feeling Well
          Button(
            onClick = { onMotherRespond(MotherResponseType.FEELING_WELL, context) },
            colors = ButtonDefaults.buttonColors(containerColor = HealthOptimalGreen),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("mother_response_fine")
          ) {
            Icon(Icons.Default.SentimentSatisfiedAlt, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isArabic) "💚 الحمد لله أنا بخير وصحتي كويسة" else "💚 I am fine and feeling well, Alhamdulillah",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Response 2: Need to talk
          Button(
            onClick = { onMotherRespond(MotherResponseType.NEED_TO_TALK, context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("mother_response_talk")
          ) {
            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isArabic) "💬 محتاجة أكلمك وأسمع صوتك يا محمد" else "💬 I want to speak with you, Mohamed",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Response 3: Pain or Dizziness
          Button(
            onClick = { onMotherRespond(MotherResponseType.PAIN_OR_DIZZY, context) },
            colors = ButtonDefaults.buttonColors(containerColor = HealthCriticalRed),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("mother_response_pain")
          ) {
            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isArabic) "⚠️ حاسة بألم أو دوخة (إرسال تنبيه عاجل لمحمد)" else "⚠️ Feeling pain or dizziness (Urgent Alert to Mohamed)",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    }

    // 6. Time-Specific Voice Check-in Prompts List
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isArabic) "رسائل ابنكِ محمد الصوتية في فترة (${selectedSlot.labelAr})"
          else "Mohamed's Voice Prompts for (${selectedSlot.labelEn})",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = (titleSize.value - 4).sp
          ),
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    items(activePrompts, key = { it.id }) { q ->
      val text = if (isArabic) q.textAr else q.textEn

      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onSpeakText(text) }
          .testTag("voice_question_${q.id}")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = CircleShape,
            color = q.iconColor.copy(alpha = 0.15f),
            modifier = Modifier.size(46.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.Hearing,
                contentDescription = null,
                tint = q.iconColor,
                modifier = Modifier.size(24.dp)
              )
            }
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = text,
              style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = bodySize,
                lineHeight = 22.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (isArabic) "اضغطي للاستماع لصوت محمد" else "Tap to play voice message",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary
            )
          }

          IconButton(
            onClick = { onSpeakText(text) },
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(q.iconColor.copy(alpha = 0.12f))
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "Play voice",
              tint = q.iconColor,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }
    }

    // 7. Audio Reader Card for Son's Letter with Calm Voice & Soft Music
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color(0xFFFFF1F2)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFE11D48),
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = if (isArabic) "مقطع رسالة ابنكِ محمد (صوت هادئ وموسيقى ناعمة) 🎵" else "Mohamed's Letter (Calm Voice & Soft Music) 🎵",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF9F1239),
                  fontSize = 15.sp
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = if (isArabic)
              "مقطع صوتي دافئ ومريح، يقرأ فيه ابنكِ محمد رسالته بكلمات هادئة مع موسيقى السكينة الناعمة لراحة بالك وطمأنينة قلبكِ:"
            else
              "A soothing, comforting audio clip: Mohamed reads his letter with a calm voice accompanied by soft ambient music for peace of mind:",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF881337)
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = onSpeakLetter,
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isLetterAudioPlaying) Color(0xFF9F1239) else Color(0xFFE11D48)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("play_son_letter_audio_button")
          ) {
            Icon(
              imageVector = if (isLetterAudioPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
              contentDescription = null,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isLetterAudioPlaying) {
                if (isArabic) "⏸️ إيقاف تشغيل الرسالة والموسيقى" else "⏸️ Pause Audio Letter & Music"
              } else {
                if (isArabic) "🎵 استمعي لرسالة ابنكِ محمد بصوت هادئ وموسيقى ناعمة" else "🎵 Play Mohamed's Letter with Soft Music"
              },
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    }
  }
}
