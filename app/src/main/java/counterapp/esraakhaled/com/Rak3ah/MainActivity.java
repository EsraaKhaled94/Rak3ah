package counterapp.esraakhaled.com.Rak3ah;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    final static int NORMARL_WAIT=5000,HALF_T7YAT_WAIT=20000,FULL_T7YAT_WAIT=40000;
    int rak3ahNumber=1, sensorAccuracyChangedCounter=0, currentPrayerPosition;
    boolean  changed=true;

    String currentPrayer;
    TextView  rak3ahNumberTextView, prayerNameTextView;

    SensorManager manager;
    Sensor sensor;

    NotificationManager notifMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            currentPrayer = getIntent().getExtras().getString("PRAYER_NAME");
            currentPrayerPosition = getIntent().getExtras().getInt("PRAYER_POSITION");
        }
        catch (Exception e){
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }
        rak3ahNumberTextView = (TextView) findViewById(R.id.counter);
        prayerNameTextView = (TextView) findViewById(R.id.prayer_name);

        prayerNameTextView.setText("Prayer : "+ currentPrayer);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        notifMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        redFlashLight();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()== Sensor.TYPE_PROXIMITY){
            if (event.values[0]==0&&changed){
                changed=false;
                sensorAccuracyChangedCounter++;

                Toast.makeText(this, "Sagda "+sensorAccuracyChangedCounter, Toast.LENGTH_SHORT).show();

                if (sensorAccuracyChangedCounter==2){
                    rak3ahNumber++;
                    sensorAccuracyChangedCounter=0;
                    Handler handler=new Handler();
                    if (rak3ahNumber==2){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                greenFlashLight();
                                rak3ahNumberTextView.setText(String.valueOf(rak3ahNumber));
                            }
                        },NORMARL_WAIT);
                    }
                    else if (rak3ahNumber==3){
                        if (currentPrayerPosition==1){
                            manager.unregisterListener(this);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, FULL_T7YAT_WAIT);
                        }
                        else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    redFlashLight();
                                    rak3ahNumberTextView.setText(String.valueOf(rak3ahNumber));
                                }
                            }, HALF_T7YAT_WAIT);
                        }
                    }
                    else if (rak3ahNumber==4){
                        if (currentPrayerPosition==4){
                            manager.unregisterListener(this);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, FULL_T7YAT_WAIT);
                        }
                        else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    greenFlashLight();
                                    rak3ahNumberTextView.setText(String.valueOf(rak3ahNumber));
                                }
                            }, NORMARL_WAIT);
                        }
                    }
                    else {
                        manager.unregisterListener(this);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },FULL_T7YAT_WAIT);
                    }
                }
            }
            else if (event.values[0]!=0){
                changed=true;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void greenFlashLight() {
        Notification notif = new Notification();
        notif.ledARGB = Color.argb(255, 0, 255, 0);
        notif.flags |= Notification.FLAG_SHOW_LIGHTS;
        notif.ledOnMS = 200;
        notif.ledOffMS = 100;
        notifMgr.notify(1234, notif);
    }

    private void redFlashLight() {
        Notification notif = new Notification();
        notif.ledARGB = Color.argb(255, 255, 0, 0);
        notif.flags |= Notification.FLAG_SHOW_LIGHTS;
        notif.ledOnMS = 200;
        notif.ledOffMS = 100;
        notifMgr.notify(1234, notif);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }
}
