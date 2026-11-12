(Environment Setup)

يعتمد التطبيق على الذكاء الاصطناعي (Gemini API) لتشغيل بعض الميزات الذكية لمساعدة المرضى ومقدمي الرعاية. لتشغيل المشروع محلياً، اتبع الخطوات التالية:

1. قم بإنشاء ملف باسم `.env` في المجلد الرئيسي للمشروع.
2. استخدم النموذج الموجود في ملف `.env.example` كمرجع:

```env
# GEMINI_API_KEY: Required for Gemini AI API calls.
# قم بإضافة مفتاح الـ API الخاص بك هنا
GEMINI_API_KEY=YOUR_ACTUAL_API_KEY_HERE
