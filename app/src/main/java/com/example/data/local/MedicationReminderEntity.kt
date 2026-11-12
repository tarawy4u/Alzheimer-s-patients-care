package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_reminders")
data class MedicationReminderEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val name: String,
  val nameEn: String = "",
  val dosage: String,
  val dosageEn: String = "",
  val scheduledTime: String = "09:00",
  val category: String = "GENERAL", // BLOOD_PRESSURE, DIABETES, HYDRATION, GENERAL
  val isRecurringDaily: Boolean = true,
  val isCompleted: Boolean = false,
  val lastTakenTimestamp: Long = 0L,
  val audioPromptText: String = "",
  val supervisorNotified: Boolean = false,
  val supervisorPhoneNumber: String = "01000000000",
  val supervisorName: String = "المشرف الطبي"
)
