package com.example.androidthings.simplepio;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Created by fmatos on 13/01/2017.
 */
public class BuzzerController {

    private static final String TAG = BuzzerController.class.getSimpleName();
    private static final long BUZZ_LENGHT_MS = 2000;
    private Gpio buzzer;

    public void init() throws IOException {
        PeripheralManagerService service = new PeripheralManagerService();

        buzzer = service.openGpio(BoardDefaults.getGpioForBuzzer());
        buzzer.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

    }

    public void buzz() {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Log.i(TAG,"Start");
                    
                    long start = System.currentTimeMillis();
                    long end = start + BUZZ_LENGHT_MS;
                    
                    while (end > System.currentTimeMillis()) {
                        Thread.sleep(0,200);
                        buzzer.setValue(!buzzer.getValue());
                    }

                    Log.i(TAG,"End");
                } catch (InterruptedException e) {} catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        th.start();

    }
}
