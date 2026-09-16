package com.samrat.voicealert;
import android.content.Context; import android.graphics.*;
import android.os.Handler; import android.view.View;
import java.util.*;
public class ConfettiView extends View {
    ArrayList<float[]> ps = new ArrayList<>(); ArrayList<Integer> cs = new ArrayList<>();
    Random r = new Random(); Paint p = new Paint(); Handler h = new Handler();
    int[] cols = {Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.rgb(255,165,0)};
    public ConfettiView(Context c) { super(c);
        for (int i = 0; i < 120; i++) { ps.add(new float[]{r.nextInt(1080), r.nextInt(400) - 400, 6 + r.nextInt(10), 4 + r.nextInt(8)}); cs.add(cols[r.nextInt(cols.length)]); }
        h.postDelayed(new Runnable(){ public void run(){ for(float[] q:ps){ q[1]+=q[3]; if(q[1]>2200) q[1]=-50; } invalidate(); h.postDelayed(this,30);} },30);
    }
    @Override protected void onDraw(Canvas c) { for(int i=0;i<ps.size();i++){ p.setColor(cs.get(i)); float[] q=ps.get(i); c.drawCircle(q[0],q[1],q[2],p);} }
}
