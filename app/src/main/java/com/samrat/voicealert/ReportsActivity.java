package com.samrat.voicealert;
import android.app.Activity; import android.os.Bundle; import android.widget.*; import android.graphics.Color;
public class ReportsActivity extends Activity {
@Override protected void onCreate(Bundle s){ super.onCreate(s);
LinearLayout l=new LinearLayout(this); l.setOrientation(1); l.setPadding(40,40,40,40);
TextView t=new TextView(this); t.setText("📊 Reports"); t.setTextSize(26);
TextView d=new TextView(this); d.setTextSize(18);
MainActivity m=MainActivity.instance;
if(m!=null) d.setText("\nTotal In: Rs."+m.totalIn+"\nTotal Out: Rs."+m.totalOut+"\nBalance: Rs."+m.balance+"\n\nThis week: "+(m.totalIn-m.totalOut)+" net");
else d.setText("No data yet");
l.addView(t); l.addView(d); setContentView(l);
}
}
