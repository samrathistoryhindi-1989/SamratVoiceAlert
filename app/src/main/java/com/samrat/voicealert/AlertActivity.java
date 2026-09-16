package com.samrat.voicealert;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.*;
import android.graphics.Color;
public class AlertActivity extends Activity {
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        String amt = getIntent().getStringExtra("amt");
        boolean credit = getIntent().getBooleanExtra("credit", true);
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(credit? Color.parseColor("#1B8C3A") : Color.parseColor("#C62828"));
        ConfettiView cv = new ConfettiView(this);
        TextView t = new TextView(this);
        t.setText((credit? "🎉 +" : "⚠️ -") + " Rs." + amt);
        t.setTextSize(44); t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        root.addView(t, new FrameLayout.LayoutParams(-1, -1));
        root.addView(cv, new FrameLayout.LayoutParams(-1, -1));
        setContentView(root);
        int snd=getResources().getIdentifier(credit?"success":"alert","raw",getPackageName()); if(snd!=0){ try{ android.media.MediaPlayer mp=android.media.MediaPlayer.create(this,snd); mp.setOnCompletionListener(m->m.release()); mp.start(); }catch(Exception e){} } new Handler().postDelayed(() -> finish(), 4000);
    }
}
