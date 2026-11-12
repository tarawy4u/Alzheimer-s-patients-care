package com.example

import com.example.ai.ClinicalAnalyticsEngine
import com.example.ai.EmpathyCompanionEngine
import com.example.data.local.MedicationReminderEntity
import com.example.model.AppLanguage
import com.example.model.HealthStatusCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testClinicalAnalytics_CriticalHigh() {
    val result = ClinicalAnalyticsEngine.evaluateVitals(185, 122, 80)
    assertEquals(HealthStatusCode.CRITICAL_HIGH, result.statusCode)
    assertTrue(result.isCritical)
  }

  @Test
  fun testClinicalAnalytics_Optimal() {
    val result = ClinicalAnalyticsEngine.evaluateVitals(120, 80, 75)
    assertEquals(HealthStatusCode.OPTIMAL, result.statusCode)
  }

  @Test
  fun testEmpathyCompanion_MotherAlzheimerCare() {
    val response = EmpathyCompanionEngine.generateResponse("تطبيق رعاية أمي مريضة الزهايمر", AppLanguage.ARABIC)
    assertNotNull(response.replyAr)
    assertEquals("MOTHER_LOVE", response.emotionTag)
    assertTrue(response.replyAr.contains("أمي الحبيبة"))
  }

  @Test
  fun testMedicationReminderEntity_9AmDefaults() {
    val bpMed = MedicationReminderEntity(
      name = "دواء الضغط",
      nameEn = "Blood Pressure Medicine",
      dosage = "قرص واحد بعد الإفطار",
      scheduledTime = "09:00",
      category = "BLOOD_PRESSURE",
      isRecurringDaily = true
    )
    assertEquals("09:00", bpMed.scheduledTime)
    assertEquals("BLOOD_PRESSURE", bpMed.category)
    assertTrue(bpMed.isRecurringDaily)

    val sugarMed = MedicationReminderEntity(
      name = "دواء السكر",
      nameEn = "Diabetes Medicine",
      dosage = "قرص واحد مع الإفطار",
      scheduledTime = "09:00",
      category = "DIABETES",
      isRecurringDaily = true
    )
    assertEquals("09:00", sugarMed.scheduledTime)
    assertEquals("DIABETES", sugarMed.category)
  }
}
