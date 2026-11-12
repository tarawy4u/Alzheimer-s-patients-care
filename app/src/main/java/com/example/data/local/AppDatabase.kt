package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [MedicationReminderEntity::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun medicationReminderDao(): MedicationReminderDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "alzheimer_care_companion.db"
        )
          .addCallback(DatabaseCallback())
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          CoroutineScope(Dispatchers.IO).launch {
            seedDatabase(database.medicationReminderDao())
          }
        }
      }
    }

    suspend fun seedDatabase(dao: MedicationReminderDao) {
      if (dao.getCount() == 0) {
        dao.insertAll(
          listOf(
            MedicationReminderEntity(
              name = "دواء الضغط",
              nameEn = "Blood Pressure Medicine",
              dosage = "قرص واحد بعد الإفطار",
              dosageEn = "1 tablet after breakfast",
              scheduledTime = "09:00",
              category = "BLOOD_PRESSURE",
              isRecurringDaily = true,
              isCompleted = false,
              audioPromptText = "يا أمي الغالية، صباح الخير والبركة. حان الآن موعد أخذ دواء الضغط في تمام الساعة التاسعة صباحاً مع كأس ماء، بالصحة والعافية يا ست الكل.",
              supervisorPhoneNumber = "01000000000",
              supervisorName = "منى (المشرفة الطبية)"
            ),
            MedicationReminderEntity(
              name = "دواء السكر",
              nameEn = "Diabetes Medicine",
              dosage = "قرص واحد مع وجبة الإفطار",
              dosageEn = "1 tablet with breakfast",
              scheduledTime = "09:00",
              category = "DIABETES",
              isRecurringDaily = true,
              isCompleted = false,
              audioPromptText = "يا أمي الحبيبة، حان موعد دواء السكر في تمام الساعة التاسعة صباحاً. يرجى تناوله مع طعام الإفطار بالهناء والعافية.",
              supervisorPhoneNumber = "01000000000",
              supervisorName = "منى (المشرفة الطبية)"
            ),
            MedicationReminderEntity(
              name = "شرب ماء وترطيب",
              nameEn = "Hydration Check",
              dosage = "كأس ماء نقي",
              dosageEn = "1 glass of fresh water",
              scheduledTime = "14:00",
              category = "HYDRATION",
              isRecurringDaily = true,
              isCompleted = false,
              audioPromptText = "تذكير بشرب كأس ماء نقي لترطيب جسمك وتنشيطك يا أمي.",
              supervisorPhoneNumber = "01000000000",
              supervisorName = "منى (المشرفة الطبية)"
            )
          )
        )
      }
    }
  }
}
