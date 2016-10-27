package com.hfad.pedometer;

import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textViewX, texViewY, textViewZ;
    private TextView textViewSteps;
    private TextView textSensitive;
    private Button buttonReset;

    //senson manager
    private SensorManager sensorManager;
    private float acceleration;
    //values to calculate number of steps
    private float previousY;
    protected float currentY;
    private int numStep;
    //seekBar fields
    private SeekBar seekBar;
    private int threshold;//point at which we want to trigger 'steps'


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //attach objects
        textViewX = (TextView) findViewById(R.id.textViewX);
        texViewY = (TextView) findViewById(R.id.textViewY);
        textViewZ = (TextView) findViewById(R.id.textViewZ);

        textViewSteps = (TextView) findViewById(R.id.textSteps);
        textSensitive = (TextView) findViewById(R.id.textSensitive);

        buttonReset = (Button) findViewById(R.id.buttonReset);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        //set value of seekBar,threshold
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        threshold = 10;
        textSensitive.setText(String.valueOf(threshold));

        //initialize Values
        previousY = 0;
        currentY = 0;
        numStep = 0;

        //initialize acceleration Values
        acceleration = 0.00f;

        //enable the listener
        enableAccelerometerListening();

    }

    private void enableAccelerometerListening(){
        //initialize the senson manager
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL);
    }

    //event handler for accelerometer events
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        //listen for change in Acceleration, Display and compute Steps
        public void onSensorChanged(SensorEvent sensorEvent) {
            //gather the values from accelerometer
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            //Fetch the current y
            currentY = y;

            //Measure if a step is taken
            if(Math.abs(currentY - previousY)> threshold){
                numStep++;
                textViewSteps.setText(String.valueOf(numStep));
            }
            //end if

            //display the values
            textViewX.setText(String.valueOf(x));
            texViewY.setText(String.valueOf(y));
            textViewZ.setText(String.valueOf(z));

            //store the previous Y
            previousY = y;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public void resetSteps(View view){
        numStep = 0;
        textViewSteps.setText(String.valueOf(numStep));
    }


    private SeekBar.OnSeekBarChangeListener seekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    threshold = seekBar.getProgress();
                    textSensitive.setText(String.valueOf(threshold));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

}


