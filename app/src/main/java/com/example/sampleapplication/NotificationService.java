package com.example.sampleapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import static android.widget.Toast.LENGTH_LONG;

public class NotificationService extends Service {
    int MailCount=0;
    int MailVer=0;
    int GrievanceCount=0;
    int GrievanceVer=0;
    int[] Increment=new int[]{};
    String msg;

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 5;
    Connection con;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }


    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();


        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {

                        con = new ConnectionClass().CONN();
                        if (con != null) {

                            System.out.println("error in DB connection");
                        }
                        else{
                            //Toast.makeText(NotificationService.this, "Connection invalid", Toast.LENGTH_SHORT).show();
                        }
                        CheckTables checkTables=new CheckTables();
                        checkTables.execute("");
                        /**
                        System.out.println("MV---------------"+MailVer+"--------------------");
                        System.out.println("MG---------------"+MailCount+"--------------------");
                        System.out.println("GC---------------"+GrievanceCount+"--------------------");
                        System.out.println("GV---------------"+GrievanceVer+"--------------------");
                         */
                        msg="-";
                        if ((MailCount==MailVer)&&(GrievanceVer==GrievanceCount)){
                            System.out.println("no apparent update");
                        }else{
                            if ((MailCount!=MailVer))
                            {
                                MailVer=MailCount;
                                msg="New Mail ";
                            }if ((GrievanceVer!=GrievanceCount)){
                                GrievanceVer=GrievanceCount;
                                msg=msg+"  New Grievance ";
                            }
                            addNotification();
                            String st=MainActivity.sd;
                            Toast.makeText(NotificationService.this,st , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
    }
    private void addNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }
    public class CheckTables extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            System.out.println(z);
            if (isSuccess) {
                Toast.makeText(NotificationService.this, "Login Successful", LENGTH_LONG).show();

            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected String doInBackground(String... params) {


            try {
                // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    // add the extension of employee pfNumber in the query...IMPORTANT
                    // use the shared preference to get the pfNumber of user
                    String query = "select count(*) from mailTable ;";
                    String query1 = "select count(*) from GTable ;";
                    String q[]=new String[]{query,query1};

                    Statement stmt = con.createStatement();
                    ResultSet rs = null;
                    for (int i=0;i<q.length;i++){
                         rs = stmt.executeQuery(q[i]);
                         if (i==0){
                             rs.next();
                             MailCount=rs.getInt(1);
                             System.out.println("mailTable contains "+rs.getInt(1)+" rows");
                         }else{
                             rs.next();
                             GrievanceCount=rs.getInt(1);
                             System.out.println("GTable contains "+rs.getInt(1)+" rows");
                         }
                    }
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();
            }
            return z;

        }

    }
}
