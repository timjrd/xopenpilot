package xopenpilot.ubordeaux.fr.androidsensors;

import fr.ubordeaux.xopenpilot.libbus.client.*;

import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    private RemoteBus bus;
    private Sender    sender;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                sender = bus.registerSender("android_test", "android_test");

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
}
