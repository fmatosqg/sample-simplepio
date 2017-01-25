package com.example.androidthings.simplepio.ndktest;

import android.util.Log;

/**
 * Created by fabio.goncalves on 25/01/2017.
 */

public class Ndktest {

    public Ndktest() {

        try {
            Log.i("Static loader test", "Test workedff??");
            System.loadLibrary("native-lib-test");

            Log.i("Call it", "String " + getMsgFromJni());
            Log.i("Call it", "2+3 = " + sumMe(2, 3));
        } catch (UnsatisfiedLinkError error) {
            Log.i("Static loader FAIIIILL", "FAIL");
        }

    }

    public void test() {

    }


    public native String getMsgFromJni();
    public native int sumMe(int a, int b);

}
