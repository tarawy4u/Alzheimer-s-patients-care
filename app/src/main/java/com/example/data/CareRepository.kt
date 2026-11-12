package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.data.local.AppDatabase
import com.example.data.local.MedicationReminderDao
import com.example.data.local.MedicationReminderEntity
import com.example.model.AppLanguage
import com.example.model.ChatMessage
import com.example.model.HealthStatusCode
import com.example.model.HelperContact
import com.example.model.VitalCheck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CareRepository(private val context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("care_companion_prefs", Context.MODE_PRIVATE)

  private val database: AppDatabase = AppDatabase.getDatabase(context)
  private val reminderDao: MedicationReminderDao = database.medicationReminderDao()

  val medicationReminders: Flow<List<MedicationReminderEntity>> = reminderDao.getAllReminders()

  init {
    CoroutineScope(Dispatchers.IO).launch {
      AppDatabase.seedDatabase(reminderDao)
    }
  }

  // Helper Contacts State (Dedicated to Family Caregiver & Medical Supervisor)
  private val _contacts = MutableStateFlow<List<HelperContact>>(getInitialContacts())
  val contacts: StateFlow<List<HelperContact>> = _contacts.asStateFlow()

  // Vitals History State (BP & Glucose monitoring)
  private val _vitalsHistory = MutableStateFlow<List<VitalCheck>>(getInitialVitals())
  val vitalsHistory: StateFlow<List<VitalCheck>> = _vitalsHistory.asStateFlow()

  // Empathy & Memory Companion Chat
  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(getInitialChat())
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  // Preferences
  private val _language = MutableStateFlow(
    if (prefs.getString("pref_lang", "ARABIC") == "ENGLISH") AppLanguage.ENGLISH else AppLanguage.ARABIC
  )
  val language: StateFlow<AppLanguage> = _language.asStateFlow()

  private val _largeTextMode = MutableStateFlow(prefs.getBoolean("pref_large_text", true)) // Default true for Alzheimer's accessibility
  val largeTextMode: StateFlow<Boolean> = _largeTextMode.asStateFlow()

  private val _ttsEnabled = MutableStateFlow(prefs.getBoolean("pref_tts_enabled", true))
  val ttsEnabled: StateFlow<Boolean> = _ttsEnabled.asStateFlow()

  private fun getInitialContacts(): List<HelperContact> {
    return listOf(
      HelperContact(
        id = "contact-emergency",
        nickname = "Emergency Hotline (Demo)",
        nicknameAr = "خط الطوارئ السريع (123)",
        number = "123",
        relationship = "Emergency Hotline",
        relationshipAr = "خط المساعدة والإسعاف",
        isEmergencyDemo = true
      ),
      HelperContact(
        id = "contact-supervisor",
        nickname = "Dr. / Nurse Mona",
        nicknameAr = "منى (المشرفة الطبية)",
        number = "01100000000",
        relationship = "Primary Medical Supervisor",
        relationshipAr = "المشرفة الطبية لمتابعة أدوية الوالدة",
        isEmergencyDemo = false
      ),
      HelperContact(
        id = "contact-son",
        nickname = "Mohamed",
        nicknameAr = "محمد (ابنك ومقدم الرعاية)",
        number = "01000000000",
        relationship = "Son & Primary Caregiver",
        relationshipAr = "ابنك محمد ومرافقك الدائم",
        isEmergencyDemo = false
      )
    )
  }

  private fun getInitialVitals(): List<VitalCheck> {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val now = System.currentTimeMillis()
    return listOf(
      VitalCheck(
        id = "vital-1",
        systolic = 120,
        diastolic = 80,
        pulse = 72,
        timestamp = formatter.format(Date(now - 86400000L)),
        statusCode = HealthStatusCode.OPTIMAL,
        messageEn = "✅ Blood pressure & pulse in optimal range for mother.",
        messageAr = "✅ ضغط الدم ونبض القلب في النطاق الصحي المثالي للوالدة بفضل الله."
      )
    )
  }

  private fun getInitialChat(): List<ChatMessage> {
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    return listOf(
      ChatMessage(
        id = "msg-welcome",
        text = "صباح الخير والبركة يا أمي الغالية، يا ست الحبايب. أنا رفيقك للاطمئنان على مواعيد دوائك وصحتك وراحتك في كل لحظة. أسأل الله أن يبارك في عمرك وصحتك.",
        isUser = false,
        timestamp = time,
        emotionTag = "WELCOME"
      )
    )
  }

  // Room DAO Operations
  suspend fun markMedicationTaken(id: Long, isCompleted: Boolean, supervisorNotified: Boolean) {
    reminderDao.markAsTaken(
      id = id,
      isCompleted = isCompleted,
      timestamp = if (isCompleted) System.currentTimeMillis() else 0L,
      supervisorNotified = supervisorNotified
    )
  }

  suspend fun updateSupervisorStatus(id: Long, notified: Boolean) {
    reminderDao.updateSupervisorStatus(id, notified)
  }

  suspend fun addReminder(reminder: MedicationReminderEntity) {
    reminderDao.insertReminder(reminder)
  }

  suspend fun deleteReminder(id: Long) {
    reminderDao.deleteById(id)
  }

  suspend fun resetDailyReminders() {
    reminderDao.resetDailyReminders()
  }

  fun recordVitalCheck(vital: VitalCheck) {
    _vitalsHistory.value = listOf(vital) + _vitalsHistory.value
  }

  fun addContact(name: String, nameAr: String, number: String, relationship: String) {
    val newContact = HelperContact(
      id = UUID.randomUUID().toString(),
      nickname = name,
      nicknameAr = if (nameAr.isBlank()) name else nameAr,
      number = number,
      relationship = relationship,
      relationshipAr = relationship,
      isEmergencyDemo = false
    )
    _contacts.value = _contacts.value + newContact
  }

  fun deleteContact(contactId: String) {
    _contacts.value = _contacts.value.filterNot { it.id == contactId && !it.isEmergencyDemo }
  }

  fun sendChatMessage(userText: String, aiReply: String, emotionTag: String) {
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val userMsg = ChatMessage(
      id = UUID.randomUUID().toString(),
      text = userText,
      isUser = true,
      timestamp = time,
      emotionTag = "USER_INPUT"
    )
    val aiMsg = ChatMessage(
      id = UUID.randomUUID().toString(),
      text = aiReply,
      isUser = false,
      timestamp = time,
      emotionTag = emotionTag
    )
    _chatMessages.value = _chatMessages.value + userMsg + aiMsg
  }

  fun clearChat() {
    _chatMessages.value = getInitialChat()
  }

  fun setLanguage(lang: AppLanguage) {
    _language.value = lang
    prefs.edit().putString("pref_lang", lang.name).apply()
  }

  fun toggleLargeTextMode() {
    val newVal = !_largeTextMode.value
    _largeTextMode.value = newVal
    prefs.edit().putBoolean("pref_large_text", newVal).apply()
  }

  fun toggleTts() {
    val newVal = !_ttsEnabled.value
    _ttsEnabled.value = newVal
    prefs.edit().putBoolean("pref_tts_enabled", newVal).apply()
  }
}
