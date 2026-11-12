package com.example.model

enum class AppLanguage {
  ARABIC,
  ENGLISH
}

enum class TaskCategory {
  MEDICATION,
  HYDRATION,
  NUTRITION,
  WELLNESS,
  VITALS
}

data class CareTask(
  val id: String,
  val task: String,
  val taskAr: String = "",
  val amount: String,
  val amountAr: String = "",
  val schedule: String,
  val done: Boolean = false,
  val category: TaskCategory = TaskCategory.MEDICATION
)

enum class HealthStatusCode {
  OPTIMAL,
  STABLE,
  WARNING,
  CRITICAL_HIGH,
  CRITICAL_LOW
}

data class HealthAssessmentResult(
  val statusCode: HealthStatusCode,
  val messageEn: String,
  val messageAr: String,
  val isCritical: Boolean = false
)

data class VitalCheck(
  val id: String,
  val systolic: Int,
  val diastolic: Int,
  val pulse: Int,
  val timestamp: String,
  val statusCode: HealthStatusCode,
  val messageEn: String,
  val messageAr: String
)

data class HelperContact(
  val id: String,
  val nickname: String,
  val nicknameAr: String = "",
  val number: String,
  val relationship: String = "",
  val relationshipAr: String = "",
  val isEmergencyDemo: Boolean = false
)

data class ChatMessage(
  val id: String,
  val text: String,
  val isUser: Boolean,
  val timestamp: String,
  val emotionTag: String = ""
)
