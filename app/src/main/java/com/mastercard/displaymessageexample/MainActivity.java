package com.mastercard.displaymessageexample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import co.poynt.os.services.v1.IPoyntSecondScreenService;


public class MainActivity extends Activity {


    private Button testDisplay;
    private IPoyntSecondScreenService secondScreenService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService(new Intent(co.poynt.os.services.v1.IPoyntSecondScreenService.class.getName()),
                secondScreenServiceConnection, Context.BIND_AUTO_CREATE);


        testDisplay = (Button) findViewById(R.id.button);

        testDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try
                {
                    showConfirmation();
                }
                catch(RemoteException ex)
                {
                    Log.i("Remote Exception", "Remote Exception!");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(co.poynt.os.services.v1.IPoyntSecondScreenService.class.getName()),
                secondScreenServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unbindService(secondScreenServiceConnection);
    }

    private ServiceConnection secondScreenServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            secondScreenService = IPoyntSecondScreenService.Stub.asInterface(iBinder);
            Log.d("Test Example", "SecondScreenService Connected");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            secondScreenService = null;
            Log.d("Test Example", "SecondScreenService Disconnected");
        }
    };

    public void showConfirmation() throws RemoteException {

        Bitmap image = Bitmap.createBitmap(800, 480, Bitmap.Config.ARGB_8888);
        image.eraseColor(Color.WHITE);
        Bitmap checkin = BitmapFactory.decodeResource(getResources(),R.drawable.demoqr);
        //secondScreenService.displayWelcome(null, null, null);
        secondScreenService.displayMessage("", image);
        showToast("Button Clicked");
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}


