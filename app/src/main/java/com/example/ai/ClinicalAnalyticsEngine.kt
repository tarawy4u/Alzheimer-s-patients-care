package com.example.ai

import com.example.model.HealthAssessmentResult
import com.example.model.HealthStatusCode

object ClinicalAnalyticsEngine {

  /**
   * Implements the AI Expert System Clinical Analytics Engine from the graduation project:
   * Analyzes systolic (top), diastolic (bottom), and heart rate (pulse).
   */
  fun evaluateVitals(top: Int, bottom: Int, pulse: Int): HealthAssessmentResult {
    // 1. Emergency / Crisis check
    if (top >= 180 || bottom >= 120) {
      return HealthAssessmentResult(
        statusCode = HealthStatusCode.CRITICAL_HIGH,
        messageEn = "⚠️ CRITICAL ALERT: Blood pressure is dangerously high (Crisis). Immediate medical attention is required!",
        messageAr = "⚠️ تنبيه حرج للغاية: ضغط الدم مرتفع بمستوى خطير (نوبة ارتفاع حادة). يرجى طلب المساعدة الطبية فوراً!",
        isCritical = true
      )
    } else if (top <= 90 || bottom <= 60) {
      return HealthAssessmentResult(
        statusCode = HealthStatusCode.CRITICAL_LOW,
        messageEn = "⚠️ WARNING: Blood pressure is very low (Hypotension). Risk of dizziness or fainting.",
        messageAr = "⚠️ تحذير: ضغط الدم منخفض جداً. خطر الدوار أو الإغماء، يرجى الاستلقاء ورفع الساقين وشرب الماء.",
        isCritical = false
      )
    }

    // 2. Pulse check
    if (pulse < 50) {
      return HealthAssessmentResult(
        statusCode = HealthStatusCode.WARNING,
        messageEn = "⚠️ Pulse is quite low (Bradycardia). Please monitor closely and rest comfortably.",
        messageAr = "⚠️ نبض القلب منخفض (بطء القلب). يرجى المراقبة عن كثب والاسترخاء.",
        isCritical = false
      )
    } else if (pulse > 120) {
      return HealthAssessmentResult(
        statusCode = HealthStatusCode.WARNING,
        messageEn = "⚠️ Pulse is quite high (Tachycardia). Rest is strongly recommended.",
        messageAr = "⚠️ نبض القلب مرتفع جداً (تسارع القلب). يُنصح بأخذ قسط من الراحة وشرب ماء بارد.",
        isCritical = false
      )
    }

    // 3. Normal ranges with smart encouraging feedback
    if (top in 110..130 && bottom in 70..85 && pulse in 60..90) {
      return HealthAssessmentResult(
        statusCode = HealthStatusCode.OPTIMAL,
        messageEn = "✅ Excellent! Vital signs are within the optimal healthy range. Keep up the good work!",
        messageAr = "✅ ممتاز ما شاء الله! المؤشرات الحيوية في النطاق الصحي المثالي. استمر في العناية بنفسك!",
        isCritical = false
      )
    }

    // 4. Stable default
    return HealthAssessmentResult(
      statusCode = HealthStatusCode.STABLE,
      messageEn = "ℹ️ Readings are generally stable, but keep monitoring if any discomfort occurs.",
      messageAr = "ℹ️ القراءات مستقرة بشكل عام، ولكن استمر في المتابعة عند الشعور بأي تعب.",
      isCritical = false
    )
  }

  fun calculateMAP(top: Int, bottom: Int): Int {
    return bottom + (top - bottom) / 3
  }
}
