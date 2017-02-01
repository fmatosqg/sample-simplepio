package com.example.androidthings.simplepio.audio;

/**
 * Created by fabio.goncalves on 1/02/2017.
 */

import android.content.Context;
import android.support.annotation.RawRes;

import com.example.androidthings.simplepio.R;

import java.lang.ref.WeakReference;

/**
 * Created by fdematos on 29/12/15.
 */
public class SoundFacade {

    private final SoundEngine soundEngine;
    private final WeakReference<Context> context;

    private Integer glassId;
    private Integer testId;

    public SoundFacade(Context context) {
        soundEngine = new SoundEngine(context);
        this.context = new WeakReference<Context>(context);

    }

    public void onResume() {
        soundEngine.onResume();
        loadSoundPool();
    }

    public void onPause() {

        unloadSoundPool();
        soundEngine.onPause();
    }

    private void unloadSoundPool() {
        if (soundEngine.getSoundPool() != null) {

            soundEngine.getSoundPool().unload(glassId);
            soundEngine.getSoundPool().unload(testId);
        }
    }

    private void loadSoundPool() {

        glassId = loadSoundResource(R.raw.breaking_a_glass_bottle_212698);
        testId =  loadSoundResource(R.raw.tone_level);

    }

    private Integer loadSoundResource(@RawRes int audioResourceId) {
        return soundEngine.getSoundPool().load(context.get(), audioResourceId, 1);
    }

    public void playGlass() {
        soundEngine.play(glassId);
    }
    public void playTest() {
        soundEngine.play(testId);
    }
}
