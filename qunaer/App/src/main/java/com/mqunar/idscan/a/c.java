package com.mqunar.idscan.a;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Vibrator;
import com.mqunar.idscan.R;
import com.mqunar.tools.log.QLog;

public final class c implements OnCompletionListener, OnErrorListener {
    private static final String a = c.class.getSimpleName();
    private final Activity b;
    private MediaPlayer c = null;
    private boolean d;
    private boolean e;

    public c(Activity activity) {
        this.b = activity;
        a();
    }

    private MediaPlayer a(Context context) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(3);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        try {
            AssetFileDescriptor openRawResourceFd = context.getResources().openRawResourceFd(R.raw.idscan_beep);
            mediaPlayer.setDataSource(openRawResourceFd.getFileDescriptor(), openRawResourceFd.getStartOffset(), openRawResourceFd.getLength());
            openRawResourceFd.close();
            mediaPlayer.setVolume(0.1f, 0.1f);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (Exception e) {
            QLog.e(a, e);
            return null;
        }
    }

    public final synchronized void a() {
        this.d = ((AudioManager) this.b.getSystemService("audio")).getRingerMode() == 2;
        this.e = true;
        if (this.d && this.c == null) {
            this.b.setVolumeControlStream(3);
            this.c = a(this.b);
        }
    }

    public final synchronized void b() {
        if (this.d && this.c != null) {
            this.c.start();
        }
        if (this.e) {
            ((Vibrator) this.b.getSystemService("vibrator")).vibrate(200);
        }
    }

    public final void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.seekTo(0);
    }

    public final synchronized boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        if (i == 100) {
            this.b.finish();
        } else {
            mediaPlayer.release();
            this.c = null;
            a();
        }
        return true;
    }
}
