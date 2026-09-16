package com.samrat.voicealert
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        TtsHelper.speakStatic(this, 1.0)
    }
}
