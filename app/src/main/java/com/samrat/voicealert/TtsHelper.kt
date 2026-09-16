package com.samrat.voicealert
import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*
object TtsHelper {
    var tts: TextToSpeech? = null
    fun speakStatic(c: Context, amt: Double) {
        try { if (!SettingsActivity.voiceOn) return } catch(e: Exception) {}
        if (tts == null) tts = TextToSpeech(c) { s ->
            if (s == TextToSpeech.SUCCESS) { applySettings(); speakNow(c, amt) }
        } else speakNow(c, amt)
    }
    fun speakText(c: Context, text: String) {
        try { if (!SettingsActivity.voiceOn) return } catch(e: Exception) {}
        if (tts == null) tts = TextToSpeech(c) { s ->
            if (s == TextToSpeech.SUCCESS) { applySettings(); tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) }
        } else { applySettings(); tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) }
    }
    private fun applySettings() {
        val lang = try { SettingsActivity.lang } catch(e: Exception) { "te" }
        tts?.language = Locale(lang, "IN")
        try { tts?.setSpeechRate(SettingsActivity.speed) } catch(e: Exception) {}
        try { tts?.setPitch(SettingsActivity.pitch) } catch(e: Exception) {}
    }
    private fun speakNow(c: Context, amt: Double) {
        applySettings()
        val p = c.getSharedPreferences("samrat", Context.MODE_PRIVATE)
        val shop = p.getString("shop_name", "సమ్రాట్ స్టోర్స్")
        val rupees = amt.toInt()
        val tpl = try { SettingsActivity.creditTpl } catch(e: Exception) { "{amount} రూపాయలు వచ్చాయి" }
        val text = tpl.replace("{amount}", rupees.toString())
        tts?.speak("$text. $shop కి ధన్యవాదాలు", TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
