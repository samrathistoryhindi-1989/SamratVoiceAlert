package com.samrat.voicealert;
import android.app.Activity; import android.os.Bundle; import android.widget.*;
public class SettingsActivity extends Activity {
public static boolean voiceOn=true, notifyOn=true;
public static String lang="te";
public static float speed=1.0f, pitch=1.0f;
public static String creditTpl="{amount} రూపాయలు వచ్చాయి";
public static String debitTpl="{amount} రూపాయలు పోయాయి";
@Override protected void onCreate(Bundle s){ super.onCreate(s);
LinearLayout l=new LinearLayout(this); l.setOrientation(1); l.setPadding(40,40,40,40);
ScrollView sc=new ScrollView(this); sc.addView(l);
TextView t=new TextView(this); t.setText("⚙ Settings"); t.setTextSize(26);
CheckBox cv=new CheckBox(this); cv.setText("Voice Alert ON"); cv.setChecked(voiceOn);
cv.setOnCheckedChangeListener((b,c)->voiceOn=c);
CheckBox cn=new CheckBox(this); cn.setText("Notification ON"); cn.setChecked(notifyOn);
cn.setOnCheckedChangeListener((b,c)->notifyOn=c);
TextView lt=new TextView(this); lt.setText("\nVoice Language:");
Spinner sp=new Spinner(this);
sp.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new String[]{"te - Telugu","en - English","hi - Hindi"}));
sp.setSelection(java.util.Arrays.asList(new String[]{"te","en","hi"}).indexOf(lang));
sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
public void onItemSelected(AdapterView a,android.view.View v,int p,long id){ lang=new String[]{"te","en","hi"}[p]; }
public void onNothingSelected(AdapterView a){} });
TextView st=new TextView(this); st.setText("\nVoice Speed: "+speed+"x");
SeekBar sb=new SeekBar(this); sb.setMax(150); sb.setProgress((int)(speed*100)-50);
sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
public void onProgressChanged(SeekBar b,int p,boolean u){ speed=(p+50)/100f; st.setText("\nVoice Speed: "+speed+"x"); }
public void onStartTrackingTouch(SeekBar b){} public void onStopTrackingTouch(SeekBar b){} });
TextView pt=new TextView(this); pt.setText("\nVoice Pitch: "+pitch+"x");
SeekBar pb=new SeekBar(this); pb.setMax(150); pb.setProgress((int)(pitch*100)-50);
pb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
public void onProgressChanged(SeekBar b,int p,boolean u){ pitch=(p+50)/100f; pt.setText("\nVoice Pitch: "+pitch+"x"); }
public void onStartTrackingTouch(SeekBar b){} public void onStopTrackingTouch(SeekBar b){} });
TextView ct=new TextView(this); ct.setText("\nCredit message ({amount}):");
EditText ce=new EditText(this); ce.setText(creditTpl);
TextView dt=new TextView(this); dt.setText("\nDebit message ({amount}):");
EditText de=new EditText(this); de.setText(debitTpl);
Button sv=new Button(this); sv.setText("💾 Save");
sv.setOnClickListener(v->{ creditTpl=ce.getText().toString(); debitTpl=de.getText().toString();
Toast.makeText(this,"Saved!",Toast.LENGTH_SHORT).show(); });
l.addView(t); l.addView(cv); l.addView(cn); l.addView(lt); l.addView(sp);
l.addView(st); l.addView(sb); l.addView(pt); l.addView(pb);
l.addView(ct); l.addView(ce); l.addView(dt); l.addView(de); l.addView(sv);
setContentView(sc);
}
}
