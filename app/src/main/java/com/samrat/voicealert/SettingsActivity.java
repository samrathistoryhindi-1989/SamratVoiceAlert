package com.samrat.voicealert;
import android.app.Activity; import android.os.Bundle; import android.widget.*;
public class SettingsActivity extends Activity {
public static boolean voiceOn=true, notifyOn=true; public static String lang="te";
@Override protected void onCreate(Bundle s){ super.onCreate(s);
LinearLayout l=new LinearLayout(this); l.setOrientation(1); l.setPadding(40,40,40,40);
TextView t=new TextView(this); t.setText("⚙️ Settings"); t.setTextSize(26);
CheckBox cv=new CheckBox(this); cv.setText("Voice Alert ON"); cv.setChecked(voiceOn); cv.setOnCheckedChangeListener((b,c)->voiceOn=c);
CheckBox cn=new CheckBox(this); cn.setText("Notification ON"); cn.setChecked(notifyOn); cn.setOnCheckedChangeListener((b,c)->notifyOn=c);
TextView lt=new TextView(this); lt.setText("\nVoice Language:");
Spinner sp=new Spinner(this); sp.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new String[]{"te - Telugu","en - English","hi - Hindi"}));
sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){ public void onItemSelected(AdapterView a,android.view.View v,int p,long id){ lang=new String[]{"te","en","hi"}[p]; } public void onNothingSelected(AdapterView a){} });
l.addView(t); l.addView(cv); l.addView(cn); l.addView(lt); l.addView(sp); setContentView(l);
}
}
