package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.ClinicalAnalyticsEngine
import com.example.ai.EmpathyCompanionEngine
import com.example.audio.CalmingAudioSynthesizer
import com.example.data.CareRepository
import com.example.data.local.MedicationReminderEntity
import com.example.model.AppLanguage
import com.example.model.ChatMessage
import com.example.model.HealthAssessmentResult
import com.example.model.HelperContact
import com.example.model.VitalCheck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class MotherResponseType {
  FEELING_WELL,
  NEED_TO_TALK,
  PAIN_OR_DIZZY,
  ATE_FOOD,
  DRANK_WATER,
  NEEDS_FOOD_OR_WATER
}

class CareViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = CareRepository(application.applicationContext)
  private val calmingSynthesizer = CalmingAudioSynthesizer()

  // Reactive Room Flow for daily medication reminders
  val medicationReminders: StateFlow<List<MedicationReminderEntity>> =
    repository.medicationReminders
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
      )

  val contacts: StateFlow<List<HelperContact>> = repository.contacts
  val vitalsHistory: StateFlow<List<VitalCheck>> = repository.vitalsHistory
  val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
  val language: StateFlow<AppLanguage> = repository.language
  val largeTextMode: StateFlow<Boolean> = repository.largeTextMode
  val ttsEnabled: StateFlow<Boolean> = repository.ttsEnabled

  // Calming ambient music state
  val isMusicPlaying: StateFlow<Boolean> = calmingSynthesizer.isPlaying
  val musicVolume: StateFlow<Float> = calmingSynthesizer.volume

  // Son's letter with calm voice and soft music playing state
  private val _isLetterAudioPlaying = MutableStateFlow(false)
  val isLetterAudioPlaying: StateFlow<Boolean> = _isLetterAudioPlaying.asStateFlow()

  // Active vital analysis result state
  private val _currentAssessment = MutableStateFlow<HealthAssessmentResult?>(null)
  val currentAssessment: StateFlow<HealthAssessmentResult?> = _currentAssessment.asStateFlow()

  // Emergency dialog visibility
  private val _showEmergencyAlert = MutableStateFlow(false)
  val showEmergencyAlert: StateFlow<Boolean> = _showEmergencyAlert.asStateFlow()

  // Supervisor dispatch alert
  private val _lastNotifiedMedication = MutableStateFlow<String?>(null)
  val lastNotifiedMedication: StateFlow<String?> = _lastNotifiedMedication.asStateFlow()

  // Mother's feedback status
  private val _motherFeedbackStatus = MutableStateFlow<String?>(null)
  val motherFeedbackStatus: StateFlow<String?> = _motherFeedbackStatus.asStateFlow()

  // Text-To-Speech engine
  private var textToSpeech: TextToSpeech? = null
  private var isTtsReady = false

  init {
    initTts()
  }

  private fun initTts() {
    try {
      textToSpeech = TextToSpeech(getApplication()) { status ->
        if (status == TextToSpeech.SUCCESS) {
          isTtsReady = true
        }
      }
    } catch (_: Exception) {
      isTtsReady = false
    }
  }

  fun speak(text: String) {
    if (!ttsEnabled.value || !isTtsReady) return
    try {
      val isArabic = language.value == AppLanguage.ARABIC
      val targetLocale = if (isArabic) Locale("ar") else Locale.ENGLISH
      textToSpeech?.language = targetLocale
      textToSpeech?.setPitch(1.0f)
      textToSpeech?.setSpeechRate(1.0f)
      textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "care_tts_${System.currentTimeMillis()}")
    } catch (_: Exception) {
      // Graceful fallback
    }
  }

  fun stopSpeaking() {
    try {
      textToSpeech?.stop()
    } catch (_: Exception) {}
  }

  fun toggleCalmingMusic() {
    calmingSynthesizer.togglePlayback()
  }

  fun setMusicVolume(vol: Float) {
    calmingSynthesizer.setVolume(vol)
  }

  /**
   * Plays the son's letter with a calm voice combined with soft gentle background music
   */
  fun toggleLetterWithSoftMusic() {
    if (_isLetterAudioPlaying.value) {
      stopLetterWithMusic()
    } else {
      playLetterWithSoftMusic()
    }
  }

  fun playLetterWithSoftMusic() {
    _isLetterAudioPlaying.value = true

    // 1. Play soft gentle background music at low therapeutic volume
    calmingSynthesizer.setVolume(0.22f)
    if (!calmingSynthesizer.isPlaying.value) {
      calmingSynthesizer.play()
    }

    val isArabic = language.value == AppLanguage.ARABIC
    val text = if (isArabic) {
      "والدتي الغالية، يعلم الله ألمي من رؤيتك هكذا، ولكنها الحياة التي دائماً لا تترك الأشياء الجميلة كما هي، وأنا الآن أعطي لكِ ما أستطيع حيث أنني جئت إلى هذه الحياة أضعف ما يكون، وبفضلك أنتِ فقط أصبحت ما أنا عليه الآن، وحينما تدير الحياة ظهرها لنا يجب أن نجد من يحنو علينا وأنا أجدكِ باب الجنة، وأتمنى أن أراكِ تلك السيدة الشابة مرة أخرى لأزيل ضيق صدري وحزني عليكِ."
    } else {
      "My dearest mother, God knows my deep pain seeing you like this, but such is life, which never leaves beautiful things unchanged. I am now giving you all that I can, for I came into this world in the weakest state, and by your grace alone I became who I am today. When life turns its back on us, we must find someone who embraces us with tenderness, and I see you as my gateway to Paradise. How I wish I could see you as that vibrant, youthful lady once more, to lift the heaviness and grief from my heart for you."
    }

    if (!ttsEnabled.value || !isTtsReady) return

    try {
      val targetLocale = if (isArabic) Locale("ar") else Locale.ENGLISH
      textToSpeech?.language = targetLocale
      // Gentle, calm, soothing pitch and slow deliberate speech rate for Alzheimer's clarity
      textToSpeech?.setPitch(0.92f)
      textToSpeech?.setSpeechRate(0.82f)

      val utteranceId = "son_letter_music_${System.currentTimeMillis()}"
      textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
          _isLetterAudioPlaying.value = true
        }

        override fun onDone(utteranceId: String?) {
          _isLetterAudioPlaying.value = false
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
          _isLetterAudioPlaying.value = false
        }
      })

      textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    } catch (_: Exception) {
      _isLetterAudioPlaying.value = false
    }
  }

  fun stopLetterWithMusic() {
    _isLetterAudioPlaying.value = false
    stopSpeaking()
    if (calmingSynthesizer.isPlaying.value) {
      calmingSynthesizer.pause()
    }
  }

  /**
   * Speaks the son's exact emotional letter to his mother aloud
   */
  fun speakLetterToMother() {
    playLetterWithSoftMusic()
  }

  /**
   * Spoken audio reminder for 9:00 AM Blood Pressure & Diabetes medicines
   */
  fun speak9AmMedicationReminder() {
    val isArabic = language.value == AppLanguage.ARABIC
    val text = if (isArabic) {
      "يا أمي الغالية، صباح الخير والبركة. حان الآن موعد أخذ دواء الضغط ودواء السكر في تمام الساعة التاسعة صباحاً مع كأس ماء نقي وطعام الإفطار، بالصحة والعافية يا ست الكل."
    } else {
      "Good morning, dear mother! It is 9:00 AM, time to take your Blood Pressure and Diabetes medications with a glass of water and breakfast."
    }
    speak(text)
  }

  fun speakReminder(reminder: MedicationReminderEntity) {
    val isArabic = language.value == AppLanguage.ARABIC
    val prompt = if (reminder.audioPromptText.isNotBlank()) {
      reminder.audioPromptText
    } else {
      if (isArabic) "حان موعد: ${reminder.name}، الجرعة: ${reminder.dosage}"
      else "Time for ${reminder.nameEn.ifBlank { reminder.name }}, dosage: ${reminder.dosageEn.ifBlank { reminder.dosage }}"
    }
    speak(prompt)
  }

  /**
   * Mother's one-tap responses to son's daily check-in questions
   */
  fun handleMotherResponse(responseType: MotherResponseType, context: Context) {
    val isArabic = language.value == AppLanguage.ARABIC
    val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

    when (responseType) {
      MotherResponseType.FEELING_WELL -> {
        val reply = if (isArabic) {
          "الحمد لله يا أمي الغالية، ربنا يديم عليكي نعمة الصحة والعافية وراحة البال دائماً، ابنك محمد دايماً بيدعيلك."
        } else {
          "Alhamdulillah, dearest mother. May health, peace and blessings be with you always. Your son Mohamed prays for you always."
        }
        speak(reply)
        _motherFeedbackStatus.value = if (isArabic) "💚 طمأنتِ ابنكِ محمد أنكِ بخير وبصحة جيدة والحمد لله." else "💚 Mother confirmed she is feeling well to Mohamed."
      }

      MotherResponseType.NEED_TO_TALK -> {
        val reply = if (isArabic) {
          "حاضر يا أمي يا ست الكل، جارٍ الاتصال بابنكِ محمد والتنبيه فوراً ليتحدث معكِ."
        } else {
          "Right away, mother. Notifying your son Mohamed to call you now."
        }
        speak(reply)
        _motherFeedbackStatus.value = if (isArabic) "💬 تم إشعار ابنكِ محمد أنكِ ترغبين في الحديث معه." else "💬 Notified Mohamed that mother wants to speak."

        // Send SMS to son Mohamed
        val sonContact = contacts.value.find { !it.isEmergencyDemo && (it.nickname.contains("Mohamed", ignoreCase = true) || it.relationship.contains("Son", ignoreCase = true)) }
        val sonPhone = sonContact?.number ?: "01000000000"
        val msg = if (isArabic) {
          "رسالة من تطبيق محمد ($timeNow): والدتك الغالية تطلب التحدث معك وسماع صوتك، يرجى الاتصال بها."
        } else {
          "Mohamed app alert ($timeNow): Your mother wishes to speak with you. Please call her."
        }
        sendSmsMessage(context, sonPhone, msg)
      }

      MotherResponseType.PAIN_OR_DIZZY -> {
        val reply = if (isArabic) {
          "ألف سلامة عليكي يا أمي يا غالية! اجلسي بهدوء واشربي رشفات من الماء، تم إرسال تنبيه عاجل لابنكِ محمد وللمشرفة الطبية منى فوراً."
        } else {
          "Please rest comfortably and sip water, mother. An urgent alert has been dispatched to your son Mohamed and Dr. Mona."
        }
        speak(reply)
        _motherFeedbackStatus.value = if (isArabic) "⚠️ تم إرسال تنبيه عاجل لابنكِ محمد وللمشرفة الطبية لوجود دوخة أو ألم." else "⚠️ Urgent alert dispatched to Mohamed & supervisor."

        // Send Urgent SMS to Son Mohamed and Medical Supervisor
        val supervisorContact = contacts.value.find { !it.isEmergencyDemo && (it.relationship.contains("Supervisor", ignoreCase = true) || it.nickname.contains("Mona", ignoreCase = true)) }
        val supervisorPhone = supervisorContact?.number ?: "01100000000"
        val sonContact = contacts.value.find { !it.isEmergencyDemo && it.relationship.contains("Son", ignoreCase = true) }
        val sonPhone = sonContact?.number ?: "01000000000"

        val urgentMsg = if (isArabic) {
          "⚠️ تنبيه عاجل من تطبيق رعاية الوالدة ($timeNow): الوالدة تشعر بألم أو دوخة وتحتاج متابعة فورية للاطمئنان على ضغطها وصحتها."
        } else {
          "⚠️ URGENT ALERT from Mother's Care ($timeNow): Mother is reporting pain or dizziness. Immediate follow-up recommended."
        }

        sendSmsMessage(context, supervisorPhone, urgentMsg)
        sendSmsMessage(context, sonPhone, urgentMsg)
      }

      MotherResponseType.ATE_FOOD -> {
        val reply = if (isArabic) {
          "ألف صحة وهنا على قلبك يا أمي الغالية، ربنا يجعله بالشفاء والعافية ودايماً مبسوطة وبصحة."
        } else {
          "Blessings and good appetite, dearest mother. May it bring health and strength."
        }
        speak(reply)
        _motherFeedbackStatus.value = if (isArabic) "🍞 طمأنتِ ابنكِ محمد أنكِ تناولتِ طعامكِ بالهناء والشفاء." else "🍞 Mother confirmed she had her meal."
      }

      MotherResponseType.DRANK_WATER -> {
        val reply = if (isArabic) {
          "بالهناء والشفاء وريّ العافية يا ست الحبايب، الماء صحة ونشاط لجسمك ودايماً ريانة وبخير."
        } else {
          "Good health and hydration, dear mother. Water keeps your body energized."
        }
        speak(reply)
        _motherFeedbackStatus.value = if (isArabic) "🥛 طمأنتِ ابنكِ محمد أنكِ شربتِ الماء وارتويتِ." else "🥛 Mother confirmed she drank fresh water."
      }

      MotherResponseType.NEEDS_FOOD_OR_WATER -> {
        val reply = if (isArabic) {
          "حاضر يا أمي يا غالية، تم تنبيه ابنكِ محمد حالاً لتجهيز ومتابعة طعامكِ وشرابكِ فوراً."
        } else {
          "Right away, mother. Alerting your son Mohamed to assist with food and hydration."
        }
        speak(reply)
        _motherFeedbackStatus.value = if (isArabic) "⚠️ تم إشعار ابنكِ محمد بمساعدتكِ في وجبة الطعام وشرب الماء." else "⚠️ Alerted Mohamed to assist with food and water."

        val sonContact = contacts.value.find { !it.isEmergencyDemo && (it.nickname.contains("Mohamed", ignoreCase = true) || it.relationship.contains("Son", ignoreCase = true)) }
        val sonPhone = sonContact?.number ?: "01000000000"
        val msg = if (isArabic) {
          "تنبيه من تطبيق محمد ($timeNow): والدتك الغالية لم تتناول طعامها بعد أو تحتاج مساعدة في إحضار الطعام والماء."
        } else {
          "Mohamed app alert ($timeNow): Mother needs assistance with her meal or drinking water."
        }
        sendSmsMessage(context, sonPhone, msg)
      }
    }
  }

  fun dismissMotherFeedbackStatus() {
    _motherFeedbackStatus.value = null
  }

  /**
   * Confirms dose taken in Room, plays spoken audio blessing, and dispatches SMS notification to supervisor
   */
  fun confirmMedicationTaken(context: Context, reminder: MedicationReminderEntity) {
    viewModelScope.launch {
      val isArabic = language.value == AppLanguage.ARABIC
      val newStatus = !reminder.isCompleted
      val supervisorNotified = newStatus

      repository.markMedicationTaken(
        id = reminder.id,
        isCompleted = newStatus,
        supervisorNotified = supervisorNotified
      )

      if (newStatus) {
        val blessing = if (isArabic) {
          "ألف صحة وعافية يا أمي الغالية. تم تأكيد أخذ ${reminder.name} بنجاح، وتم إرسال رسالة للمشرفة الطبية للاطمئنان."
        } else {
          "Blessings and good health, mother. Confirmation of taking ${reminder.nameEn.ifBlank { reminder.name }} has been logged and supervisor notified."
        }
        speak(blessing)
        _lastNotifiedMedication.value = reminder.name

        // Prepare SMS to medical supervisor
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val supervisorMessage = if (isArabic) {
          "السلام عليكم، تم بحمد الله تأكيد أخذ ${reminder.name} (${reminder.dosage}) للوالدة في تمام الساعة $timeNow. الحالة مستقرة ومطمئنة."
        } else {
          "Hello, confirmation that mother has taken ${reminder.nameEn.ifBlank { reminder.name }} (${reminder.dosage}) at $timeNow. Condition is stable."
        }

        sendSmsMessage(context, reminder.supervisorPhoneNumber, supervisorMessage)
      }
    }
  }

  fun dismissSupervisorNotification() {
    _lastNotifiedMedication.value = null
  }

  fun addMedication(
    name: String,
    nameEn: String,
    dosage: String,
    dosageEn: String,
    scheduledTime: String,
    category: String,
    audioPrompt: String,
    supervisorPhone: String = "01000000000",
    supervisorName: String = "منى (المشرفة الطبية)"
  ) {
    viewModelScope.launch {
      val entity = MedicationReminderEntity(
        name = name,
        nameEn = nameEn,
        dosage = dosage,
        dosageEn = dosageEn,
        scheduledTime = scheduledTime,
        category = category,
        isRecurringDaily = true,
        isCompleted = false,
        audioPromptText = audioPrompt,
        supervisorPhoneNumber = supervisorPhone,
        supervisorName = supervisorName
      )
      repository.addReminder(entity)
    }
  }

  fun deleteMedication(id: Long) {
    viewModelScope.launch {
      repository.deleteReminder(id)
    }
  }

  fun resetDailyMedications() {
    viewModelScope.launch {
      repository.resetDailyReminders()
      val isArabic = language.value == AppLanguage.ARABIC
      val msg = if (isArabic) "تمت إعادة تعيين جدول الأدوية لليوم الجديد." else "Medication schedule reset for the new day."
      speak(msg)
    }
  }

  fun analyzeAndRecordVitals(systolic: Int, diastolic: Int, pulse: Int) {
    val assessment = ClinicalAnalyticsEngine.evaluateVitals(systolic, diastolic, pulse)
    _currentAssessment.value = assessment

    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val vital = VitalCheck(
      id = UUID.randomUUID().toString(),
      systolic = systolic,
      diastolic = diastolic,
      pulse = pulse,
      timestamp = formatter.format(Date()),
      statusCode = assessment.statusCode,
      messageEn = assessment.messageEn,
      messageAr = assessment.messageAr
    )
    repository.recordVitalCheck(vital)

    if (assessment.isCritical) {
      _showEmergencyAlert.value = true
    }

    val textToRead = if (language.value == AppLanguage.ARABIC) assessment.messageAr else assessment.messageEn
    speak(textToRead)
  }

  fun dismissEmergencyAlert() {
    _showEmergencyAlert.value = false
  }

  fun sendEmpathyMessage(userText: String) {
    if (userText.isBlank()) return
    viewModelScope.launch {
      val response = EmpathyCompanionEngine.generateResponse(userText, language.value)
      val reply = if (language.value == AppLanguage.ARABIC) response.replyAr else response.replyEn
      repository.sendChatMessage(userText, reply, response.emotionTag)
      speak(reply)
    }
  }

  fun clearChat() {
    repository.clearChat()
  }

  fun addContact(name: String, nameAr: String, number: String, relationship: String) {
    repository.addContact(name, nameAr, number, relationship)
  }

  fun deleteContact(contactId: String) {
    repository.deleteContact(contactId)
  }

  fun setLanguage(lang: AppLanguage) {
    repository.setLanguage(lang)
  }

  fun toggleLanguage() {
    val next = if (language.value == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
    repository.setLanguage(next)
  }

  fun toggleLargeText() {
    repository.toggleLargeTextMode()
  }

  fun toggleTts() {
    repository.toggleTts()
    if (!ttsEnabled.value) {
      stopSpeaking()
    }
  }

  fun dialPhoneNumber(context: Context, number: String) {
    try {
      val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$number")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "Could not open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
    }
  }

  fun sendSmsMessage(context: Context, number: String, messageText: String) {
    try {
      val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("sms:$number")
        putExtra("sms_body", messageText)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "Could not open SMS: ${e.message}", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onCleared() {
    super.onCleared()
    try {
      calmingSynthesizer.release()
      textToSpeech?.stop()
      textToSpeech?.shutdown()
    } catch (_: Exception) {}
  }
}
