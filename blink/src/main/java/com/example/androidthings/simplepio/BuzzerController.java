package com.example.androidthings.simplepio;

import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

/**
 * Created by fmatos on 13/01/2017.
 */
public class BuzzerController {

    private static final String TAG = BuzzerController.class.getCanonicalName();


    private static final long BUZZ_LENGHT_MS = 2000;
    private Gpio buzzer;
    private Pwm mPwm;

    public void init() throws IOException {
        initDigitalPin();
    }


    public void buzz(int buzzLenght) {

        buzzDigitalPin(buzzLenght);
    }

    public void initDigitalPin() throws IOException {
        PeripheralManagerService service = new PeripheralManagerService();

        buzzer = service.openGpio(BoardDefaults.getGpioForBuzzer());
        buzzer.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);


    }

    public void buzzDigitalPin(final int buzzLenght) {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Log.i(TAG, "Start");

                    long start = System.currentTimeMillis();
                    long end = start + buzzLenght;

                    int count = 0;
                    boolean flag = true;
                    while (end > System.currentTimeMillis()) {
                        Thread.sleep(0, 100);
                        buzzer.setValue(flag);
                        flag = !flag;
                        count++;
                    }

                    Log.i(TAG, "End, count = " + count);
                } catch (InterruptedException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        th.start();

    }

    private static final double MIN_ACTIVE_PULSE_DURATION_MS = 1;
    private static final double PULSE_PERIOD_MS = 5;  // Frequency of 50Hz (1000/20)

    private Handler mHandler = new Handler();
    private double mActivePulseDuration;

    private void initPwmPin() throws IOException {

        PeripheralManagerService service = new PeripheralManagerService();

        String pinName = BoardDefaults.getPwm0();
        mActivePulseDuration = MIN_ACTIVE_PULSE_DURATION_MS;

        mPwm = service.openPwm(pinName);

        // Always set frequency and initial duty cycle before enabling PWM
        mPwm.setPwmFrequencyHz(1000 / PULSE_PERIOD_MS);
        mPwm.setPwmDutyCycle(mActivePulseDuration);
        mPwm.setEnabled(true);

        // Post a Runnable that continuously change PWM pulse width, effectively changing the
        // servo position
        Log.d(TAG, "Start changing PWM pulse");
//        mHandler.post(mChangePWMRunnable);
    }

    public void destroy() throws IOException {
        if (mPwm != null) {
            mPwm.close();
            mPwm = null;
        }

        if (buzzer != null) {
            buzzer.close();
            buzzer = null;
        }
    }
}
