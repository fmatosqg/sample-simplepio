//
// Created by Fabio Goncalves on 25/01/2017.
//

#include <jni.h>

#include <stdio.h>

void aaa() {
    printf("aaa\n");
}

int sumMe(int a, int b) {
    return a + b;
}
//
//JNIEXPORT jstring JNICALL
//Java_com_example_androidthings_simplepio_ndktest_Ndktest_getMsgFromJni(JNIEnv *env,
//                                                                       jobject instance) {
//
//    // TODO
//
//
///*    return (*env)->NewStringUTF(env, returnValue);*/
//    return (*env)->NewStringUTF("Hello From Jni");
//}

JNIEXPORT jint JNICALL
Java_com_example_androidthings_simplepio_ndktest_Ndktest_sumMe(JNIEnv *env, jobject instance,
                                                               jint a, jint b) {

    return sumMe(a, b);

}

JNIEXPORT jstring JNICALL

Java_com_example_androidthings_simplepio_ndktest_Ndktest_getMsgFromJni(JNIEnv *env,
                                                                       jobject instance) {

    // TODO


    return env->NewStringUTF("Heeeeelllooooo");
}