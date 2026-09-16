package com.samrat.voicealert;
import android.service.notification.*; import android.os.Bundle;
import android.content.Intent;
public class UpiListener extends NotificationListenerService {
@Override public void onNotificationPosted(StatusBarNotification sbn){
String pkg=sbn.getPackageName();
if(pkg.contains("phonepe")||pkg.contains("tez")||pkg.contains("paytm")||pkg.contains("upi")||pkg.contains("paisa")){
try{
Bundle e=sbn.getNotification().extras;
String txt=(e.getString("android.title","")+" "+e.getString("android.text","")).toLowerCase();
double amt=MainActivity.extractAmount(txt);
if(amt>0 && MainActivity.instance!=null){
boolean cr = txt.contains("received")||txt.contains("credit")||txt.contains("credited")||txt.contains("vachayi")||txt.contains("received money");
String vpa="UPI";
 try{ Intent it=new Intent(this, AlertActivity.class); it.putExtra("amt", String.valueOf(amt)); it.putExtra("credit", cr); it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(it);}catch(Exception e2){}
MainActivity.instance.runOnUiThread(()->MainActivity.instance.showAlert(amt,cr,vpa));
}
}catch(Exception ex){}
}
}
}
