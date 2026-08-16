package com.doorbit.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class DoorbitMessagingService extends FirebaseMessagingService {
 @Override public void onMessageReceived(RemoteMessage message){
  if(getSharedPreferences("doorbit",MODE_PRIVATE).getBoolean("dnd",false))return;
  String room=message.getData().get("room");
  String entrance=message.getData().get("entrance");
  Intent i=new Intent(this,IncomingCallActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
  i.putExtra("room",room);i.putExtra("entrance",entrance);
  PendingIntent pi=PendingIntent.getActivity(this,100,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
  NotificationCompat.Builder b=new NotificationCompat.Builder(this,"door_calls")
   .setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Kapınızdan çağrı var")
   .setContentText(entrance==null?"724 Doorbit":entrance).setPriority(NotificationCompat.PRIORITY_MAX)
   .setCategory(NotificationCompat.CATEGORY_CALL).setAutoCancel(true).setContentIntent(pi).setFullScreenIntent(pi,true)
   .setDefaults(NotificationCompat.DEFAULT_ALL);
  ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(724,b.build());
 }
 @Override public void onNewToken(String token){
  getSharedPreferences("doorbit",MODE_PRIVATE).edit().putString("push_token",token).apply();
 }
}
