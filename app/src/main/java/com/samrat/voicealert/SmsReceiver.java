package com.samrat.voicealert;
import android.content.*; import android.provider.Telephony;
public class SmsReceiver extends BroadcastReceiver {
public void onReceive(Context c, Intent i){
for(var sms: Telephony.Sms.Intents.getMessagesFromIntent(i)){
String b=sms.getDisplayMessageBody(); double amt=MainActivity.extractAmount(b);
if(amt>0 && MainActivity.instance!=null){ boolean cr=MainActivity.isCredit(b);
MainActivity.instance.runOnUiThread(()->MainActivity.instance.showAlert(amt,cr,"bank")); }
}
}
}
