package com.doorbit.app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
 private WebView webView;
 @Override public void onCreate(Bundle state) {
  super.onCreate(state);
  webView=new WebView(this);
  webView.setBackgroundColor(0xFF07192A);
  setContentView(webView);
  WebSettings s=webView.getSettings();
  s.setJavaScriptEnabled(true);
  s.setDomStorageEnabled(true);
  s.setMediaPlaybackRequiresUserGesture(false);
  webView.setWebViewClient(new WebViewClient());
  webView.setWebChromeClient(new WebChromeClient(){
   @Override public void onPermissionRequest(PermissionRequest r){ runOnUiThread(()->r.grant(r.getResources())); }
  });
  if(checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED ||
     checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){
   requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},101);
  }
  webView.loadUrl("https://724doorbit.com/canli-test?mode=phone");
 }
 @Override public void onBackPressed(){ if(webView!=null&&webView.canGoBack())webView.goBack();else super.onBackPressed(); }
}
