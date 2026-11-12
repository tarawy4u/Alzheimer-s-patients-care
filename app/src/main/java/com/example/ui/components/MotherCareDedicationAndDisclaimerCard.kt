package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage

@Composable
fun MotherCareDedicationAndDisclaimerCard(
  language: AppLanguage,
  isEndPosition: Boolean = false,
  isLetterAudioPlaying: Boolean = false,
  onToggleLetterAudio: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val isArabic = language == AppLanguage.ARABIC

  val headerTitle = if (isArabic) {
    if (isEndPosition) "خاتمة ورسالة وفاء إلى أمي الغالية ❤️" else "رسالتي إلى أمي التي قمت بإنشاء التطبيق من أجلها ❤️"
  } else {
    if (isEndPosition) "Closing Words & Devotion to My Mother ❤️" else "A Message to My Mother for Whom I Built This App ❤️"
  }

  // Exact letter from the user to his mother
  val sonMessage = if (isArabic) {
    "«والدتي الغالية، يعلم الله ألمي من رؤيتك هكذا، ولكنها الحياة التي دائماً لا تترك الأشياء الجميلة كما هي، وأنا الآن أعطي لكِ ما أستطيع حيث أنني جئت إلى هذه الحياة أضعف ما يكون، وبفضلك أنتِ فقط أصبحت ما أنا عليه الآن، وحينما تدير الحياة ظهرها لنا يجب أن نجد من يحنو علينا وأنا أجدكِ باب الجنة، وأتمنى أن أراكِ تلك السيدة الشابة مرة أخرى لأزيل ضيق صدري وحزني عليكِ.»"
  } else {
    "\"My dearest mother, God knows my deep pain seeing you like this, but such is life, which never leaves beautiful things unchanged. I am now giving you all that I can, for I came into this world in the weakest state, and by your grace alone I became who I am today. When life turns its back on us, we must find someone who embraces us with tenderness, and I see you as my gateway to Paradise. How I wish I could see you as that vibrant, youthful lady once more, to lift the heaviness and grief from my heart for you.\""
  }

  val scientificNotice = if (isArabic) {
    "هذا ليس تطبيقاً طبياً ولكنه تطبيق استخدمت فيه الذكاء الاصطناعي لرعاية والدتي المريضة والتي تعيش بمفردها ولا أستطيع التواجد الدائم بجانبها مثلما كانت تفعل معي دائماً، ولكن كل ما ذُكر من معلومات طبية تم مراجعته علمياً، ولكن لا ينصح أبداً باستخدام مصادر غير موثقة علمياً وتطبيقها على البشر. وشكراً."
  } else {
    "This is NOT a medical application; rather, it is an application where I utilized AI to care for my ill mother who lives alone, as I cannot always be physically beside her as she always was for me. All medical information mentioned has been scientifically reviewed, but it is strictly advised never to use unverified sources on human beings. Thank you."
  }

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isEndPosition) Color(0xFFF8FAFC) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag(if (isEndPosition) "mother_dedication_end_card" else "mother_dedication_start_card")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // 1. Header with heart icon
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = Color(0xFFE11D48).copy(alpha = 0.15f),
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFE11D48),
                modifier = Modifier.size(20.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = headerTitle,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Color(0xFF9F1239)
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // The Son's Exact Letter to His Mother
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color.White.copy(alpha = 0.8f))
          .padding(14.dp)
      ) {
        Text(
          text = sonMessage,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium
          ),
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      // Audio Playback Button for Son's Letter with Calm Voice & Soft Music
      if (onToggleLetterAudio != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Button(
          onClick = onToggleLetterAudio,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isLetterAudioPlaying) Color(0xFF9F1239) else Color(0xFFE11D48)
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag(if (isEndPosition) "play_letter_end_btn" else "play_letter_start_btn")
        ) {
          Icon(
            imageVector = if (isLetterAudioPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isLetterAudioPlaying) {
              if (isArabic) "⏸️ إيقاف تشغيل الرسالة والموسيقى" else "⏸️ Pause Letter Audio & Music"
            } else {
              if (isArabic) "🎵 استمعي لرسالة ابنكِ محمد بصوت هادئ وموسيقى ناعمة" else "🎵 Listen with Calm Voice & Soft Music"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 2. Scientific Review & Disclaimer Notice
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFFFEF3C7))
          .padding(14.dp)
      ) {
        Row(verticalAlignment = Alignment.Top) {
          Icon(
            imageVector = Icons.Default.VerifiedUser,
            contentDescription = "Scientific Review Notice",
            tint = Color(0xFFB45309),
            modifier = Modifier
              .size(22.dp)
              .padding(top = 2.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = if (isArabic) "ملحوظة هامة وتنبيه علمي:" else "Important Scientific Notice:",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF92400E)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = scientificNotice,
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                lineHeight = 19.sp
              ),
              color = Color(0xFF78350F)
            )
          }
        }
      }
    }
  }
}
