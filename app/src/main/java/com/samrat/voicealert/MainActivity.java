package com.samrat.voicealert;
import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    public static MainActivity instance;
    double balance = 50000, totalIn = 0, totalOut = 0;
    ArrayList<String[]> txns = new ArrayList<>();
    LinearLayout txnBox;
    TextView balanceTv, inTv, outTv, shopTv;
    int notifId = 1;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        instance = this;
        askPerms();
        buildUI();
        refreshUI();
    }

    void askPerms() {
        ArrayList<String> ps = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
            ps.add(Manifest.permission.RECEIVE_SMS);
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED)
            ps.add(Manifest.permission.POST_NOTIFICATIONS);
        if (!ps.isEmpty()) requestPermissions(ps.toArray(new String[0]), 1);
    }

    GradientDrawable round(int color, int r) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(r); return d;
    }

    LinearLayout.LayoutParams lp(int top) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, top, 0, 0); return p;
    }

    void buildUI() {
        SharedPreferences p = getSharedPreferences("samrat", MODE_PRIVATE);
        String shop = p.getString("shop_name", "సమ్రాట్ స్టోర్స్");

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 28, 28, 28);
        root.setBackgroundColor(Color.parseColor("#121212"));
        ScrollView sc = new ScrollView(this); sc.addView(root);

        TextView head = new TextView(this);
        head.setText("🔊 Samrat Voice Alert");
        head.setTextSize(24); head.setTextColor(Color.WHITE);
        head.setTypeface(null, Typeface.BOLD); head.setGravity(Gravity.CENTER);
        root.addView(head, lp(8));

        LinearLayout shopCard = new LinearLayout(this);
        shopCard.setOrientation(LinearLayout.VERTICAL);
        shopCard.setBackground(round(Color.parseColor("#1E1E1E"), 24));
        shopCard.setPadding(28, 24, 28, 24);
        shopTv = new TextView(this);
        shopTv.setText("🏪 " + shop); shopTv.setTextSize(18);
        shopTv.setTextColor(Color.WHITE); shopTv.setTypeface(null, Typeface.BOLD);
        shopTv.setGravity(Gravity.CENTER);
        TextView st = new TextView(this);
        st.setText("● Listening for UPI SMS..."); st.setTextSize(14);
        st.setTextColor(Color.parseColor("#4CAF50")); st.setGravity(Gravity.CENTER);
        shopCard.addView(shopTv); shopCard.addView(st);
        root.addView(shopCard, lp(24));

        LinearLayout balCard = new LinearLayout(this);
        balCard.setOrientation(LinearLayout.VERTICAL);
        balCard.setBackground(round(Color.parseColor("#1E1E1E"), 24));
        balCard.setPadding(28, 24, 28, 24);
        TextView bl = new TextView(this);
        bl.setText("Balance"); bl.setTextSize(14); bl.setTextColor(Color.parseColor("#AAAAAA"));
        balanceTv = new TextView(this);
        balanceTv.setTextSize(34); balanceTv.setTextColor(Color.WHITE);
        balanceTv.setTypeface(null, Typeface.BOLD);
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        inTv = new TextView(this); inTv.setTextSize(16);
        inTv.setTextColor(Color.parseColor("#4CAF50")); inTv.setTypeface(null, Typeface.BOLD);
        outTv = new TextView(this); outTv.setTextSize(16);
        outTv.setTextColor(Color.parseColor("#F44336")); outTv.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams w = new LinearLayout.LayoutParams(0, -2, 1);
        row.addView(inTv, w); outTv.setGravity(Gravity.END); row.addView(outTv, w);
        balCard.addView(bl); balCard.addView(balanceTv); balCard.addView(row, lp(8));
        root.addView(balCard, lp(20));

        TextView tl = new TextView(this);
        tl.setText("Transactions:"); tl.setTextSize(16); tl.setTextColor(Color.parseColor("#AAAAAA"));
        root.addView(tl, lp(24));
        txnBox = new LinearLayout(this);
        txnBox.setOrientation(LinearLayout.VERTICAL);
        root.addView(txnBox, lp(12));

        TextView testL = new TextView(this);
        testL.setText("Test:"); testL.setTextSize(14); testL.setTextColor(Color.parseColor("#AAAAAA"));
        root.addView(testL, lp(24));
        root.addView(mkBtn("TEST CREDIT ₹500", "#1B8C3A", () -> showAlert(500, true, "PhonePe Test")), lp(12));
        root.addView(mkBtn("TEST DEBIT ₹1000", "#C62828", () -> showAlert(1000, false, "GPay Test")), lp(12));
        root.addView(mkBtn("TEST FRAUD ₹60000 DEBIT", "#E65100", () -> showAlert(60000, false, "Unknown VPA")), lp(12));

        LinearLayout menu = new LinearLayout(this);
        menu.setOrientation(LinearLayout.HORIZONTAL);
        Button rep = mkBtn("📊 Reports", "#1565C0", () -> startActivity(new Intent(this, ReportsActivity.class)));
        Button set = mkBtn("⚙ Settings", "#424242", () -> startActivity(new Intent(this, SettingsActivity.class)));
        LinearLayout.LayoutParams w2 = new LinearLayout.LayoutParams(0, -2, 1);
        w2.setMargins(0, 0, 12, 0);
        menu.addView(rep, w2);
        LinearLayout.LayoutParams w3 = new LinearLayout.LayoutParams(0, -2, 1);
        w3.setMargins(12, 0, 0, 0);
        menu.addView(set, w3);
        root.addView(menu, lp(20));

        setContentView(sc);
    }

    Button mkBtn(String t, String color, Runnable r) {
        Button b = new Button(this);
        b.setText(t); b.setTextColor(Color.WHITE);
        b.setTextSize(16); b.setTypeface(null, Typeface.BOLD);
        b.setBackground(round(Color.parseColor(color), 24));
        b.setPadding(20, 28, 20, 28);
        b.setOnClickListener(v -> r.run());
        return b;
    }

    public void showAlert(double amt, boolean cr, String vpa) {
        runOnUiThread(() -> {
            boolean fraud =!cr && amt >= 50000;
            if (cr) { balance += amt; totalIn += amt; }
            else { balance -= amt; totalOut += amt; }
            String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            String label = (vpa == null || vpa.isEmpty()? "UPI" : vpa) + " • " + time;
            txns.add(0, new String[]{label, String.valueOf((int) amt), cr? "c" : "d", fraud? "1" : "0"});
            refreshUI();
            try {
                Intent it = new Intent(this, AlertActivity.class);
                it.putExtra("amt", String.valueOf((int) amt));
                it.putExtra("credit", cr);
                startActivity(it);
            } catch (Exception e) {}
            try {
                if (fraud) TtsHelper.INSTANCE.speakText(this, "హెచ్చరిక! " + (int) amt + " రూపాయలు మీ ఖాతా నుండి పోయాయి. ఇది మీరు చేశారా?");
                else TtsHelper.INSTANCE.speakStatic(this, amt);
            } catch (Exception e) {}
            try { if (SettingsActivity.notifyOn) notifyUser(amt, cr, fraud); } catch (Exception e) {}
        });
    }

    void notifyUser(double amt, boolean cr, boolean fraud) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String ch = "alerts";
        if (Build.VERSION.SDK_INT >= 26)
            nm.createNotificationChannel(new NotificationChannel(ch, "Alerts", NotificationManager.IMPORTANCE_HIGH));
        Notification.Builder b = new Notification.Builder(this)
               .setContentTitle(fraud? "⚠️ Fraud Alert!" : (cr? "💰 Money Received" : "💸 Money Sent"))
               .setContentText("Rs." + (int) amt)
               .setSmallIcon(android.R.drawable.ic_dialog_info);
        if (Build.VERSION.SDK_INT >= 26) b.setChannelId(ch);
        nm.notify(notifId++, b.build());
    }

    void refreshUI() {
        balanceTv.setText("Rs." + String.format(Locale.US, "%,.0f", balance));
        inTv.setText("In: Rs." + String.format(Locale.US, "%,.0f", totalIn));
        outTv.setText("Out: Rs." + String.format(Locale.US, "%,.0f", totalOut));
        txnBox.removeAllViews();
        if (txns.isEmpty()) {
            TextView e = new TextView(this);
            e.setText("No transactions yet"); e.setTextColor(Color.parseColor("#777777"));
            e.setTextSize(14); txnBox.addView(e);
            return;
        }
        for (int i = 0; i < Math.min(txns.size(), 30); i++) {
            String[] t = txns.get(i);
            LinearLayout r = new LinearLayout(this);
            r.setOrientation(LinearLayout.HORIZONTAL);
            r.setBackground(round(Color.parseColor("#1E1E1E"), 20));
            r.setPadding(24, 20, 24, 20);
            TextView l = new TextView(this);
            l.setText(t[0]); l.setTextSize(14); l.setTextColor(Color.WHITE);
            TextView a = new TextView(this);
            boolean isC = t[2].equals("c");
            a.setText((isC? "+ ₹" : "- ₹") + t[1]);
            a.setTextSize(15); a.setTypeface(null, Typeface.BOLD);
            a.setTextColor(Color.parseColor(t[3].equals("1")? "#FF9800" : (isC? "#4CAF50" : "#F44336")));
            LinearLayout.LayoutParams w = new LinearLayout.LayoutParams(0, -2, 1);
            r.addView(l, w); a.setGravity(Gravity.END); r.addView(a, w);
            txnBox.addView(r, lp(10));
        }
    }
}
