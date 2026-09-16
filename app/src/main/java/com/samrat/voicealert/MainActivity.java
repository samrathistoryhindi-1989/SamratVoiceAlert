package com.samrat.voicealert;
import android.app.*; import android.os.*; import android.widget.*; import android.view.*; import android.graphics.Color; import android.speech.tts.TextToSpeech; import java.util.*; import java.util.regex.*;
public class MainActivity extends Activity {
TextToSpeech tts; LinearLayout txnList; TextView balTv, inTv, outTv; double balance=50000;
public static MainActivity instance;
@Override protected void onCreate(Bundle s){ super.onCreate(s); instance=this;
LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setPadding(30,30,30,30);
TextView title=new TextView(this); title.setText("Samrat Alert"); title.setTextSize(28); title.setTextColor(Color.BLACK);
balTv=new TextView(this); balTv.setTextSize(22); updateBal();
LinearLayout row=new LinearLayout(this); row.setOrientation(0);
inTv=new TextView(this); outTv=new TextView(this); inTv.setTextSize(18); outTv.setTextSize(18);
row.addView(inTv); TextView sp=new TextView(this); sp.setText("   "); row.addView(sp); row.addView(outTv);
updateInOut(0,0);
txnList=new LinearLayout(this); txnList.setOrientation(1);
Button tc=new Button(this); tc.setText("Test Credit ₹500"); tc.setOnClickListener(v->showAlert(500,true,"ravi@upi"));
Button td=new Button(this); td.setText("Test Debit ₹1000"); td.setOnClickListener(v->showAlert(1000,false,"swiggy@upi"));
root.addView(title); root.addView(balTv); root.addView(row);
TextView h=new TextView(this); h.setText("\nTransactions:"); h.setTextSize(20); root.addView(h);
ScrollView sc=new ScrollView(this); sc.addView(txnList); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,600); sc.setLayoutParams(lp); root.addView(sc);
root.addView(tc); root.addView(td);
setContentView(root);
tts=new TextToSpeech(this,st->{ if(st==TextToSpeech.SUCCESS){ tts.setLanguage(new Locale("te","IN")); }});
requestPermissions(new String[]{"android.permission.RECEIVE_SMS","android.permission.READ_SMS"},1);
}
void updateBal(){ if(balTv!=null) balTv.setText("Balance: Rs."+String.format("%,.0f",balance)); }
void updateInOut(double i,double o){ if(inTv!=null){ inTv.setText("In: Rs."+i); inTv.setTextColor(Color.parseColor("#008000")); } if(outTv!=null){ outTv.setText("Out: Rs."+o); outTv.setTextColor(Color.RED); } }
public void showAlert(double amt, boolean isCredit, String vpa){
if(isCredit) balance+=amt; else balance-=amt; updateBal();
String msg = isCredit ? amt+" రూపాయలు క్రెడిట్ అయ్యాయి" : amt+" రూపాయలు డెబిట్ అయ్యాయి";
if(tts!=null) tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
AlertDialog d=new AlertDialog.Builder(this).create();
LinearLayout l=new LinearLayout(this); l.setOrientation(1); l.setPadding(50,50,50,50);
l.setBackgroundColor(isCredit?Color.parseColor("#E8F5E9"):Color.parseColor("#FFEBEE"));
TextView t1=new TextView(this); t1.setText(isCredit?"💰 డబ్బులు వచ్చాయి!":"💸 డబ్బులు పోయాయి!"); t1.setTextSize(24);
TextView t2=new TextView(this); t2.setText("Rs."+amt); t2.setTextSize(36); t2.setTextColor(isCredit?Color.parseColor("#008000"):Color.RED);
TextView t3=new TextView(this); t3.setText((isCredit?"From: ":"To: ")+vpa); t3.setTextSize(16);
l.addView(t1); l.addView(t2); l.addView(t3); d.setView(l);
d.show();
TextView tx=new TextView(this); tx.setText((isCredit?"+ Rs.":"- Rs.")+amt+" "+vpa); tx.setTextSize(16);
tx.setTextColor(isCredit?Color.parseColor("#008000"):Color.RED);
txnList.addView(tx,0);
if(!isCredit && amt>=50000){ Toast.makeText(this,"⚠️ Fraud Alert! పెద్ద debit!",Toast.LENGTH_LONG).show(); }
}
public static double extractAmount(String body){
Matcher m=Pattern.compile("(?:Rs\\.?|INR|₹)\\s?([0-9,]+(?:\\.[0-9]+)?)",Pattern.CASE_INSENSITIVE).matcher(body);
if(m.find()) return Double.parseDouble(m.group(1).replace(",","")); return 0;
}
public static boolean isCredit(String body){ body=body.toLowerCase(); return body.contains("credit")||body.contains("received")||body.contains("deposited"); }
@Override protected void onDestroy(){ if(tts!=null) tts.shutdown(); super.onDestroy(); }
}
