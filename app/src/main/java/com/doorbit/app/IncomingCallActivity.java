package com.doorbit.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IncomingCallActivity extends Activity {
 @Override public void onCreate(Bundle state){
  super.onCreate(state);
  getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setGravity(Gravity.CENTER);box.setPadding(42,42,42,42);box.setBackgroundColor(0xFF07192A);
  TextView title=new TextView(this);title.setText("Kapınızdan çağrı var");title.setTextColor(0xFFFFFFFF);title.setTextSize(30);title.setGravity(Gravity.CENTER);
  TextView sub=new TextView(this);String e=getIntent().getStringExtra("entrance");sub.setText(e==null?"724 Doorbit":e);sub.setTextColor(0xFFB9D7F2);sub.setTextSize(18);sub.setGravity(Gravity.CENTER);sub.setPadding(0,24,0,48);
  Button answer=new Button(this);answer.setText("Cevapla");answer.setOnClickListener(v->{Intent i=new Intent(this,MainActivity.class);i.putExtra("room",getIntent().getStringExtra("room"));startActivity(i);finish();});
  Button reject=new Button(this);reject.setText("Sessiz reddet");reject.setOnClickListener(v->finish());
  box.addView(title);box.addView(sub);box.addView(answer,new LinearLayout.LayoutParams(-1,-2));box.addView(reject,new LinearLayout.LayoutParams(-1,-2));setContentView(box);
 }
}
