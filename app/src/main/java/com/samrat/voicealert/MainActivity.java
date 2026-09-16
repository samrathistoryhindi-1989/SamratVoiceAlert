package com.samrat.voicealert;
import android.app.*; import android.os.*; import android.widget.*; import android.graphics.Color; import android.speech.tts.TextToSpeech; import android.content.pm.*; import java.util.*; import java.util.regex.*;
public class MainActivity extends Activity {
TextToSpeech tts; LinearLayout txnList; TextView balTv,inTv,outTv; double balance=50000,totalIn=0,totalOut=0; public static MainActivity instance; NotificationManager nm;
@Override protected void onCreate(Bundle s){ super.onCreate(s); instance=this; nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
if(Build.VERSION.SDK_INT>=26){ nm.createNotificationChannel(new NotificationChannel("alert","Alerts",3)); }
LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setPadding(30,30,30,30);
TextView title=new TextView(this); title.setText("Samrat Alert"); title.setTextSize(28);
balTv=new TextView(this); balTv.setTextSize(22); inTv=new TextView(this); outTv=new TextView(this);
LinearLayout row=new LinearLayout(this); row.addView(inTv); TextView sp=new TextView(this); sp.setText("   "); row.addView(sp); row.addView(outTv);
txnList=new LinearLayout(this); txnList.setOrientation(1);
Button tc=new Button(this); tc.setText("Test Credit ₹500"); tc.setOnClickListener(v->showAlert(500,true,"ravi@upi"));
Button td=new Button(this); td.setText("Test Debit ₹1000"); td.setOnClickListener(v->showAlert(1000,false,"swiggy@upi"));
Button tf=new Button(this); tf.setText("Test Fraud ₹60000 debit"); tf.setOnClickListener(v->showAlert(60000,false,"unknown@upi"));
updateAll();
root.addView(title); root.addView(balTv); root.addView(row);
TextView h=new TextView(this); h.setText("\nTransactions:"); h.setTextSize(20); root.addView(h);
ScrollView sc=new ScrollView(this); sc.addView(txnList); sc.setLayoutParams(new LinearLayout.LayoutParams(-1,500)); root.addView(sc);
root.addView(tc); root.addView(td); root.addView(tf); setContentView(root);
tts=new TextToSpeech(this,st->{ if(st==0) tts.setLanguage(new Locale("te","IN")); });
requestPermissions(new String[]{"android.permission.RECEIVE_SMS","android.permission.READ_SMS","android.permission.POST_NOTIFICATIONS"},1);
}
void updateAll(){ balTv.setText("Balance: Rs."+String.format("%,.0f",balance)); inTv.setText("In: Rs."+totalIn); inTv.setTextColor(Color.parseColor("#008000")); outTv.setText("Out: Rs."+totalOut); outTv.setTextColor(Color.RED); }
void notify(String t,String b,boolean cr){ Notification.Builder nb; if(Build.VERSION.SDK_INT>=26) nb=new Notification.Builder(this,"alert"); else nb=new Notification.Builder(this); nb.setContentTitle(t).setContentText(b).setSmallIcon(android.R.drawable.ic_dialog_info); nm.notify((int)System.currentTimeMillis(),nb.build()); }
public void showAlert(double amt,boolean isCredit,String vpa){
if(isCredit){balance+=amt; totalIn+=amt;} else {balance-=amt; totalOut+=amt;} updateAll();
String voice = isCredit ? amt+" రూపాయలు క్రెడిట్ అయ్యాయి" : amt+" రూపాయలు డెబిట్ అయ్యాయి";
if(balance<1000) voice+=". తక్కువ బ్యాలెన్స్ హెచ్చరిక";
if(!isCredit && amt>=50000) voice+=". ఫ్రాడ్ హెచ్చరిక! పెద్ద మొత్తం డెబిట్ అయింది";
if(tts!=null) tts.speak(voice,0,null,null);
notify(isCredit?"💰 డబ్బులు వచ్చాయి":"💸 డబ్బులు పోయాయి","Rs."+amt+" "+vpa,isCredit);
AlertDialog d=new AlertDialog.Builder(this).create(); LinearLayout l=new LinearLayout(this); l.setOrientation(1); l.setPadding(50,50,50,50); l.setBackgroundColor(isCredit?Color.parseColor("#E8F5E9"):Color.parseColor("#FFEBEE"));
TextView t1=new TextView(this); t1.setText(isCredit?"💰 డబ్బులు వచ్చాయి!":"💸 డబ్బులు పోయాయి!"); t1.setTextSize(24);
TextView t2=new TextView(this); t2.setText("Rs."+amt); t2.setTextSize(36); t2.setTextColor(isCredit?Color.parseColor("#008000"):Color.RED);
TextView t3=new TextView(this); t3.setText((isCredit?"From: ":"To: ")+vpa);
if(balance<1000){ TextView w=new TextView(this); w.setText("⚠️ Low Balance!"); w.setTextColor(Color.RED); l.addView(w); }
if(!isCredit && amt>=50000){ TextView f=new TextView(this); f.setText("🛡️ Fraud Alert!"); f.setTextColor(Color.RED); f.setTextSize(20); l.addView(f); }
l.addView(t1); l.addView(t2); l.addView(t3); d.setView(l); d.show();
TextView tx=new TextView(this); tx.setText((isCredit?"+ Rs.":"- Rs.")+amt+" "+vpa); tx.setTextColor(isCredit?Color.parseColor("#008000"):Color.RED); txnList.addView(tx,0);
}
public static double extractAmount(String b){ var m=Pattern.compile("(?:Rs\\.?|INR|₹)\\s?([0-9,]+)",Pattern.CASE_INSENSITIVE).matcher(b); if(m.find()) return Double.parseDouble(m.group(1).replace(",","")); return 0; }
public static boolean isCredit(String b){ b=b.toLowerCase(); return b.contains("credit")||b.contains("received"); }
@Override protected void onDestroy(){ if(tts!=null) tts.shutdown(); super.onDestroy(); }
}
