package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    TextView textView1;
    TextView textView2;
    TextView textView3;

    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }


        if(sensor != null) {
            sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float x = sensorEvent.values[0];
                    float y = sensorEvent.values[1];
                    float z = sensorEvent.values[2];
                    textView1.setText("x: " + String.valueOf(x));
                    textView2.setText("y: " + String.valueOf(y));
                    textView3.setText("z: " + String.valueOf(z));

                    if(x < -4 && cont == 0){
                        cont++;
                    }
                    if(x > 4 && cont == 1){
                        cont++;
                    }
                    if(cont == 2){
                        latigo();
                        cont = 0;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
        }
        start();
    }

    private void start(){
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void latigo(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.latigo);
        mediaPlayer.start();
    }

    public void ubicacion(View view) {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,this);

        }catch (Exception e){
            e.printStackTrace();
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null){
            textView1.setText("Latidud: " + String.valueOf(location.getAltitude()));
            textView2.setText("Longitud: " + String.valueOf(location.getLongitude()));
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
}