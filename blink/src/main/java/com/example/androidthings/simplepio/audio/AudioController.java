package com.example.androidthings.simplepio.audio;

import android.content.Context;
import android.util.Log;

/**
 * Created by fabio.goncalves on 1/02/2017.
 */
public class AudioController {

    private static final String TAG = AudioController.class.getSimpleName();

    private final SoundFacade soundFacade;

    public AudioController(Context context) {

        soundFacade = new SoundFacade(context);

        soundFacade.onResume();

        playLoop();
    }

    private void playLoop() {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    while(true) {
                    Thread.sleep(4000);
                    Log.i(TAG, "Play glass NOW");
                    soundFacade.playTest();

//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });

        th.start();

    }

    public void destroy() {

        soundFacade.onPause();
    }
}
