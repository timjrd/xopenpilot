package xopenpilot.ubordeaux.fr.androidsensors;

import fr.ubordeaux.xopenpilot.libbus.client.*;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private RemoteBus bus;
    private Sender    sender;

    private SensorManager sensorManager;
    private Sensor        sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onConnect(View v)
    {
        Button   connectButton = (Button) v;
        TextView hostnameText  = (TextView) findViewById(R.id.hostname);
        String   hostname      = hostnameText.getText().toString();

        if (bus == null)
        {
            try
            {
                bus    = new RemoteBus(hostname);
                sender = bus.registerSender("Accelerometer", "android_accelerometer");

                connectButton.setText("Disconnect");
            }
            catch (IOException e)
            {
                e.printStackTrace();

                if (bus != null)
                    try { bus.close(); } catch (IOException e1) { e1.printStackTrace(); }

                bus    = null;
                sender = null;
                connectButton.setText("Connect");

                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setPositiveButton("OK", null)
                        .setTitle("Unable to connect")
                        .setMessage("Please make sure the bus is running and check the hostname/IP.")
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        else
        {
            try { sender.deregister(); } catch (IOException e) { e.printStackTrace(); }
            try { bus.close();         } catch (IOException e) { e.printStackTrace(); }

            bus    = null;
            sender = null;
            connectButton.setText("Connect");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        JsonObject contents = Json.createObjectBuilder()
                .add("x", x)
                .add("y", y)
                .add("z", z)
                .build();

        if (sender != null)
            try { sender.sendMessage(contents); } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {}
}
