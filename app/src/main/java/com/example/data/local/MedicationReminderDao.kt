package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationReminderDao {

  @Query("SELECT * FROM medication_reminders ORDER BY scheduledTime ASC, id ASC")
  fun getAllReminders(): Flow<List<MedicationReminderEntity>>

  @Query("SELECT * FROM medication_reminders WHERE id = :id LIMIT 1")
  fun getReminderById(id: Long): Flow<MedicationReminderEntity?>

  @Query("SELECT * FROM medication_reminders WHERE scheduledTime = :time")
  fun getRemindersByTime(time: String): Flow<List<MedicationReminderEntity>>

  @Query("SELECT COUNT(*) FROM medication_reminders")
  suspend fun getCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReminder(reminder: MedicationReminderEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(reminders: List<MedicationReminderEntity>)

  @Update
  suspend fun updateReminder(reminder: MedicationReminderEntity)

  @Delete
  suspend fun deleteReminder(reminder: MedicationReminderEntity)

  @Query("DELETE FROM medication_reminders WHERE id = :id")
  suspend fun deleteById(id: Long)

  @Query("""
    UPDATE medication_reminders 
    SET isCompleted = :isCompleted, 
        lastTakenTimestamp = :timestamp, 
        supervisorNotified = :supervisorNotified 
    WHERE id = :id
  """)
  suspend fun markAsTaken(
    id: Long,
    isCompleted: Boolean,
    timestamp: Long,
    supervisorNotified: Boolean
  )

  @Query("UPDATE medication_reminders SET supervisorNotified = :notified WHERE id = :id")
  suspend fun updateSupervisorStatus(id: Long, notified: Boolean)

  @Query("""
    UPDATE medication_reminders 
    SET isCompleted = 0, 
        supervisorNotified = 0 
    WHERE isRecurringDaily = 1
  """)
  suspend fun resetDailyReminders()
}
