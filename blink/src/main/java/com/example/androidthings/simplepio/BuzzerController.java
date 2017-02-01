package com.example.androidthings.simplepio;

import android.os.Handler;
import android.util.Log;
import android.util.TimingLogger;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

/**
 * Created by fmatos on 13/01/2017.
 */
public class BuzzerController {

    private static final String TAG = BuzzerController.class.getSimpleName();


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

                    long start = System.nanoTime();
//                    long end = start + 100 * 1000 * 1000;//buzzLenght;

                    int count = 0;
//                    boolean flag = true;
//                    TimingLogger timings = new TimingLogger(TAG, "sleep");
//                    while (end > System.nanoTime()) {
//
//                        for (int i = 0; i < 10; i++) {
//                            busySleep(0.02f);
//                            buzzer.setValue(flag);
//                            flag = !flag;
//                            count++;
//                        }
//                    }
                    count = buzzLenght;
                    for (int i = count; i > 0; i--) {
                        buzzer.setValue(true);
                        buzzer.setValue(false);
                    }

                    float tElapsed = System.nanoTime() - start;
//                    timings.dumpToLog();

                    count = count*2;
                    TimingLogger timings2 = new TimingLogger(TAG, "log");


                    tElapsed = tElapsed / 1000.0f / 1000.0f / 1000.0f;
                    float f = (float) count / tElapsed;
                    Log.i(TAG, "End, count = " + count + " time = " + tElapsed + " s " + " freq  = " + f + " Hz");
                    timings2.dumpToLog();
//                } catch (InterruptedException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        th.start();

    }

    private void busySleep(float sleepMs) {
        long endNano = System.nanoTime() + (int) (1000.0f * 1000.0f * sleepMs);

        // Note: System.nanoTime is approx 10x faster than SystemClock equivalent ( like 4 us per call )
        int c = 0;
        while (System.nanoTime() < endNano)
            c++;

        Log.i(TAG, "Secondary count  = " + c);

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
