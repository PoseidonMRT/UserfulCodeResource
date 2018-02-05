package com.mqunar.core.basectx.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.speech.tts.TextToSpeechService;

@TargetApi(14)
public abstract class QTextToSpeechService extends TextToSpeechService {
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{this});
        } catch (Throwable th) {
        }
    }
}
