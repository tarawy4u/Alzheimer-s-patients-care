package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.HealthWarningAmber
import com.example.ui.theme.HealthWarningContainer

@Composable
fun MedicalDisclaimerBanner(
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val isArabic = language == AppLanguage.ARABIC
  val text = if (isArabic) {
    "هذا ليس تطبيقاً طبياً ولكنه تطبيق استخدمت فيه الذكاء الاصطناعي لرعاية والدتي المريضة التي تعيش بمفردها ولا أستطيع التواجد الدائم بجانبها، وكل ما ذُكر من معلومات طبية تم مراجعته علمياً ولكن لا ينصح أبداً باستخدام مصادر غير موثقة وتطبيقها على البشر."
  } else {
    "This is NOT a medical device but an AI companion built to care for my ill mother who lives alone. All medical info has been scientifically reviewed, but unverified sources must never be applied to humans."
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(HealthWarningContainer)
      .padding(horizontal = 12.dp, vertical = 8.dp)
      .testTag("medical_disclaimer_banner")
  ) {
    Row(
      verticalAlignment = Alignment.Top
    ) {
      Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Medical Disclaimer",
        tint = HealthWarningAmber,
        modifier = Modifier
          .size(18.dp)
          .padding(top = 2.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = 11.sp,
          lineHeight = 16.sp
        ),
        color = HealthWarningAmber
      )
    }
  }
}
