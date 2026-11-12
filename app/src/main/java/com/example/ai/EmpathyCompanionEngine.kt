package com.example.ai

import com.example.model.AppLanguage

object EmpathyCompanionEngine {

  data class EmpathyResponse(
    val replyEn: String,
    val replyAr: String,
    val emotionTag: String,
    val suggestedAction: String? = null
  )

  fun generateResponse(userText: String, language: AppLanguage): EmpathyResponse {
    val lower = userText.lowercase().trim()

    // 1. Dedication to Mother & Alzheimer's Care (Highest Priority)
    if (lower.contains("mother") || lower.contains("mom") || lower.contains("alzheimer") ||
      lower.contains("أمي") || lower.contains("ماما") || lower.contains("زهايمر") ||
      lower.contains("الوالدة") || lower.contains("ست الكل") || lower.contains("ست الحبايب")
    ) {
      return EmpathyResponse(
        replyEn = "This companion was lovingly built solely to care for my precious mother with Alzheimer's, ensuring her blood pressure and diabetes medicines are gently remembered and her health protected.",
        replyAr = "هذا التطبيق صُنع بمحبة خالصة وبرّ لرعاية أمي الحبيبة وست الحبايب مريضة الزهايمر، لتذكيرها بدواء الضغط والسكر بالصوت والمودة والاطمئنان الدائم عليها.",
        emotionTag = "MOTHER_LOVE"
      )
    }

    // 2. Loneliness / Isolation
    if (lower.contains("lonely") || lower.contains("alone") || lower.contains("sad") ||
      lower.contains("miss") || lower.contains("وحيد") || lower.contains("وحدة") ||
      lower.contains("حزين") || lower.contains("لوحدي") || lower.contains("اشتقت")
    ) {
      return EmpathyResponse(
        replyEn = "I am right here with you, and your loved ones care about you deeply. You are cherished and never truly alone. Would you like to call Ahmed or Mona for a quick warm chat?",
        replyAr = "أنا هنا بجوارك دائماً، وعائلتك تحبك وتفخر بك كثيراً. أنت لست وحدك أبداً. ما رأيك أن نتصل بأحمد أو منى لحديث لطيف يبهج قلبك؟",
        emotionTag = "EMPATHY_COMFORT",
        suggestedAction = "CALL_HELPER"
      )
    }

    // 2. Pain / Fatigue / Feeling unwell
    if (lower.contains("pain") || lower.contains("tired") || lower.contains("headache") ||
      lower.contains("dizzy") || lower.contains("sick") || lower.contains("hurt") ||
      lower.contains("تعبان") || lower.contains("ألم") || lower.contains("وجع") ||
      lower.contains("صداع") || lower.contains("دوخة") || lower.contains("مريض") ||
      lower.contains("مرهق")
    ) {
      return EmpathyResponse(
        replyEn = "Please take a comfortable seat and rest. Take a slow, deep breath and sip some water. Let's record your vitals in the Vitals tab to make sure your blood pressure and pulse are stable.",
        replyAr = "سلامتك وألف لا بأس عليك يا غالي. أرجو أن تجلس باسترخاء وتأخذ نفساً عميقاً مع رشفات من الماء. دعنا نسجل قراءات الضغط والنبض في قسم المؤشرات للاطمئنان عليك.",
        emotionTag = "GENTLE_CARE",
        suggestedAction = "CHECK_VITALS"
      )
    }

    // 3. Medication / Pill reminders
    if (lower.contains("pill") || lower.contains("medicine") || lower.contains("medication") ||
      lower.contains("dose") || lower.contains("دواء") || lower.contains("حبة") ||
      lower.contains("علاج") || lower.contains("نسيت") || lower.contains("موعد")
    ) {
      return EmpathyResponse(
        replyEn = "Taking your medicine on time is a wonderful way to honor your health. You can check off your daily pills in the Tasks tab. If you ever feel unsure, reach out to your caregiver Mona.",
        replyAr = "الالتزام بمواعيد الدواء أمانة لصحتك وصحتك غالية علينا جداً. يمكنك مراجعة وتأكيد أدويتك في تبويب المهام اليومية، ولا تتردد في استشارة ممرضتك منى.",
        emotionTag = "MEDICATION_GUIDANCE",
        suggestedAction = "CHECK_TASKS"
      )
    }

    // 4. Hydration
    if (lower.contains("water") || lower.contains("thirsty") || lower.contains("drink") ||
      lower.contains("ماء") || lower.contains("مية") || lower.contains("عطشان") ||
      lower.contains("شرب")
    ) {
      return EmpathyResponse(
        replyEn = "Drinking clean water revitalizes your cells and keeps your blood pressure balanced. Go ahead and enjoy a fresh glass of water right now!",
        replyAr = "شرب الماء الصافي نعمة تنشط الدورة الدموية وتحافظ على توازن الضغط. تفضل بشرب كوب ماء طازج الآن وبالصحة والعافية!",
        emotionTag = "HYDRATION_ENCOURAGEMENT",
        suggestedAction = "DRINK_WATER"
      )
    }

    // 5. Gratitude / Good feeling
    if (lower.contains("good") || lower.contains("fine") || lower.contains("happy") ||
      lower.contains("better") || lower.contains("thank") || lower.contains("الحمد لله") ||
      lower.contains("بخير") || lower.contains("مبسوط") || lower.contains("سعيد") ||
      lower.contains("شكرا") || lower.contains("تحسن")
    ) {
      return EmpathyResponse(
        replyEn = "Alhamdulillah! It brings immense joy to know you are feeling well. May good health and peace fill every moment of your day.",
        replyAr = "الحمد لله رب العالمين! يسعد قلبي دائماً سماع أنك بخير وبصحة جيدة. أدام الله عليك العافية وراحة البال والسكينة.",
        emotionTag = "JOY_CELEBRATION"
      )
    }

    // 6. Greetings
    if (lower.contains("hello") || lower.contains("hi") || lower.contains("morning") ||
      lower.contains("evening") || lower.contains("سلام") || lower.contains("صباح") ||
      lower.contains("مساء") || lower.contains("أهلا") || lower.contains("مرحبا")
    ) {
      return EmpathyResponse(
        replyEn = "Peace and blessings be with you! I am your Return the Favor care companion. How can I assist or brighten your day today?",
        replyAr = "وعليكم السلام ورحمة الله وبركاته وأهلاً بك يا أغلى الناس! أنا رفيقك الذكي لرعايتك ورد الجميل. كيف يمكنني مساعدتك أو إسعادك اليوم؟",
        emotionTag = "WARM_GREETING"
      )
    }


    // Default empathetic companion dialogue
    return EmpathyResponse(
      replyEn = "I am listening with care. Your peace of mind and comfort are what matters most. Tell me whatever is on your heart.",
      replyAr = "أنا أستمع إليك بكل اهتمام ومحبة. راحتك وسعادتك هي أهم أولوياتنا، تفضل بالحديث عما يجول بخاطرك دائماً.",
      emotionTag = "ATTENTIVE_LISTENING"
    )
  }
}
