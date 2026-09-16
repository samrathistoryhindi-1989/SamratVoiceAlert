package com.samrat.voicealert
import android.content.*
import android.provider.Telephony
class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) {
        val msgs = Telephony.Sms.Intents.getMessagesFromIntent(i)
        val body = msgs.joinToString("") { it.messageBody }
        if (!body.contains("credited", true) && !body.contains("UPI", true)) return
        if (body.contains("debited", true)) return
        val amt = Regex("""Rs\.?\s?([\d,]+\.?\d*)""").find(body)?.groupValues?.get(1)?.replace(",","")?.toDoubleOrNull() ?: return
        TtsHelper.speakStatic(c, amt)
    }
}
