package com.example.androidthings.simplepio;

import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

/**
 * Created by fmatos on 20/01/2017.
 */

public class ServoController {

    private static final String TAG = ServoController.class.getSimpleName();
    private Pwm servoPin;

    public ServoController(PeripheralManagerService service, String pinName) {

        try {
            servoPin = service.openPwm(pinName);
            servoPin.setPwmFrequencyHz(1000.0f);
            servoPin.setPwmDutyCycle(50.0f);
            servoPin.setEnabled(true);

            Log.i(TAG, "Servo pin set :" + pinName);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
