package com.doorbit.app;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends Activity {
 private WebView webView;
 private String pushToken="";
 @Override public void onCreate(Bundle state) {
  super.onCreate(state);
  createChannel();
  initFirebase();
  webView=new WebView(this);
  webView.setBackgroundColor(0xFF07192A);
  setContentView(webView);
  WebSettings s=webView.getSettings();
  s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setMediaPlaybackRequiresUserGesture(false);
  s.setAllowFileAccess(false); s.setAllowContentAccess(false);
  webView.addJavascriptInterface(new NativeBridge(),"DoorbitAndroid");
  webView.setWebViewClient(new WebViewClient(){
   @Override public void onPageFinished(WebView v,String url){ sendTokenToPage(); }
  });
  webView.setWebChromeClient(new WebChromeClient(){
   @Override public void onPermissionRequest(PermissionRequest r){runOnUiThread(()->r.grant(r.getResources()));}
  });
  requestAppPermissions();
  String room=getIntent().getStringExtra("room");
  webView.loadUrl("https://724doorbit.com/canli-test?mode=phone"+(room==null?"":"&room="+room));
 }
 private void requestAppPermissions(){
  java.util.ArrayList<String> p=new java.util.ArrayList<>();
  if(checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)p.add(Manifest.permission.CAMERA);
  if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)p.add(Manifest.permission.RECORD_AUDIO);
  if(Build.VERSION.SDK_INT>=33&&checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)p.add(Manifest.permission.POST_NOTIFICATIONS);
  if(!p.isEmpty())requestPermissions(p.toArray(new String[0]),101);
 }
 private void initFirebase(){
  if(FirebaseApp.getApps(this).isEmpty()){
   FirebaseOptions o=new FirebaseOptions.Builder().setApplicationId("1:64419013342:android:c3c3ee737d2684c3d1d84f").setProjectId("doorbit-87414").setApiKey("AIzaSyBPph66DsO48VWSFLoXJ_7DGSDRjzFIr0M").setGcmSenderId("64419013342").build();
   FirebaseApp.initializeApp(this,o);
  }
  FirebaseMessaging.getInstance().getToken().addOnCompleteListener((OnCompleteListener<String>)t->{if(t.isSuccessful()){pushToken=t.getResult();sendTokenToPage();}});
 }
 private void sendTokenToPage(){if(webView!=null&&!pushToken.isEmpty())runOnUiThread(()->webView.evaluateJavascript("window.dispatchEvent(new CustomEvent('doorbit-push-token',{detail:"+quote(pushToken)+"}));",null));}
 private String quote(String s){return "\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\"";}
 private void createChannel(){if(Build.VERSION.SDK_INT>=26){NotificationChannel c=new NotificationChannel("door_calls","Kapı çağrıları",NotificationManager.IMPORTANCE_HIGH);c.setDescription("724 Doorbit kapı çağrıları");c.enableVibration(true);getSystemService(NotificationManager.class).createNotificationChannel(c);}}
 public class NativeBridge {
  @JavascriptInterface public String getPushToken(){return pushToken;}
  @JavascriptInterface public void setDoNotDisturb(boolean value){getSharedPreferences("doorbit",MODE_PRIVATE).edit().putBoolean("dnd",value).apply();}
  @JavascriptInterface public boolean isDoNotDisturb(){return getSharedPreferences("doorbit",MODE_PRIVATE).getBoolean("dnd",false);}
 }
 @Override public void onBackPressed(){if(webView!=null&&webView.canGoBack())webView.goBack();else super.onBackPressed();}
 @Override protected void onDestroy(){if(webView!=null){webView.destroy();webView=null;}super.onDestroy();}
}
