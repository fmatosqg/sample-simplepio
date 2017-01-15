/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.simplepio;

import android.app.Activity;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

/**
 * Sample usage of the Gpio API that blinks an LED at a fixed interval defined in
 * {@link #INTERVAL_BETWEEN_BLINKS_MS}.
 * <p>
 * Some boards, like Intel Edison, have onboard LEDs linked to specific GPIO pins.
 * The preferred GPIO pin to use on each board is in the {@link BoardDefaults} class.
 */
public class BlinkActivity extends Activity {
    private static final String TAG = BlinkActivity.class.getSimpleName();
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 1000;

    private Handler mHandler = new Handler();
    private Gpio mRedLedGpio;
    private Gpio mGreenLedGpio;
    private Gpio mBlueLedGpio;

    private Gpio mButtonGpio;
    private boolean isPressed = false;
    private BuzzerController buzzerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting BlinkActivity");

        PeripheralManagerService service = new PeripheralManagerService();
        try {

            mRedLedGpio = service.openGpio(BoardDefaults.getGPIOForRedLED());
            mRedLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            mGreenLedGpio = service.openGpio(BoardDefaults.getGPIOForGreenLED());
            mGreenLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            mBlueLedGpio = service.openGpio(BoardDefaults.getGPIOForBlueLED());
            mBlueLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            mButtonGpio = service.openGpio(BoardDefaults.getGPIOForButton());
            mButtonGpio.setDirection(Gpio.DIRECTION_IN);

            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_RISING);
            mButtonGpio.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    Log.i(TAG, "GPIO changed, button pressed");

                    try {
                        mGreenLedGpio.setValue(!mGreenLedGpio.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isPressed = true;
                    return true;
                }
            });

            buzzerController = new BuzzerController();
            buzzerController.init();
            buzzerController.buzz(200);

            Log.i(TAG, "Start blinking LED GPIO pin");
            // Post a Runnable that continuously switch the state of the GPIO, blinking the
            // corresponding LED
            mHandler.post(mBlinkRunnable);

        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove pending blink Runnable from the handler.
        mHandler.removeCallbacks(mBlinkRunnable);
        // Close the Gpio pin.
        Log.i(TAG, "Closing LED GPIO pin");
        try {
            mRedLedGpio.close();
            mGreenLedGpio.close();
            mBlueLedGpio.close();
            mButtonGpio.close();
            buzzerController.destroy();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mRedLedGpio = null;
        }
    }

    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            // Exit Runnable if the GPIO is already closed
            if (mRedLedGpio == null) {
                return;
            }
            try {
                // Toggle the GPIO state

//                mGreenLedGpio.setValue(!mGreenLedGpio.getValue());
                mBlueLedGpio.setValue(!mBlueLedGpio.getValue());


                if (isPressed) {
                    isPressed = false;
//                    mRedLedGpio.setValue(!mRedLedGpio.getValue());
                    buzzerController.buzz(3000);
                }

                Log.d(TAG, "State set to " + mBlueLedGpio.getValue() + "/" + mRedLedGpio.getValue());

                // Reschedule the same runnable in {#INTERVAL_BETWEEN_BLINKS_MS} milliseconds
                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };
}
