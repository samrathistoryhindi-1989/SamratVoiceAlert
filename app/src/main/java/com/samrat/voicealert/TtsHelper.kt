package com.samrat.voicealert
import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*
object TtsHelper {
    var tts: TextToSpeech? = null
    fun speakStatic(c: Context, amt: Double) {
        if (tts == null) tts = TextToSpeech(c) { s ->
            if (s == TextToSpeech.SUCCESS) {
                tts?.language = Locale("te","IN")
                speakNow(c, amt)
            }
        } else speakNow(c, amt)
    }
    private fun speakNow(c: Context, amt: Double) {
        val p = c.getSharedPreferences("samrat", Context.MODE_PRIVATE)
        val shop = p.getString("shop_name", "సామ్రాట్ స్టోర్స్")
        val rupees = amt.toInt()
        val text = if (rupees == 1) "ఒక్క రూపాయి మీ ఖాతాలో జమ చేయబడింది"
                   else "$rupees రూపాయలు మీ ఖాతాలో జమ చేయబడింది"
        tts?.speak("$text. $shop కి ధన్యవాదాలు", TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
