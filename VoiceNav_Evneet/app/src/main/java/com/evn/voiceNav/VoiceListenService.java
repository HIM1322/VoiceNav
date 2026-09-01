package com.evn.voiceNav;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceListenService extends Service implements RecognitionListener {
    private SpeechRecognizer recognizer;
    private Intent recognizerIntent;
    private boolean listening = false;

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(7, makeNotification());
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        begin();
    }

    private Notification makeNotification() {
        String text = "Listening for “Evneet”";
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            String channel = "voicenav";
            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new android.app.NotificationChannel(
                    channel, "VoiceNav", NotificationManager.IMPORTANCE_LOW));
            return new Notification.Builder(this, channel)
                    .setContentTitle("VoiceNav")
                    .setContentText(text)
                    .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                    .setOngoing(true).build();
        }
        return new Notification.Builder(this)
                .setContentTitle("VoiceNav")
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setOngoing(true).build();
    }

    private void begin() {
        if (recognizer != null) {
            try { recognizer.destroy(); } catch (Exception ignored) {}
        }
        if (!SpeechRecognizer.isRecognitionAvailable(this)) return;
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(this);
        try {
            listening = true;
            recognizer.startListening(recognizerIntent);
        } catch (Exception e) {
            listening = false;
        }
    }

    private void restartSoon() {
        new android.os.Handler().postDelayed(this::begin, 500);
    }

    private void process(String heard) {
        if (heard == null) return;
        String s = heard.toLowerCase(Locale.US).trim();
        int wake = s.indexOf("evneet");
        if (wake < 0) return;

        String cmd = s.substring(wake + "evneet".length()).trim();
        if (cmd.isEmpty()) return;

        if (cmd.contains("go back") || cmd.equals("back") || cmd.contains("back")) {
            VoiceNavAccessibilityService.back();
        } else if (cmd.contains("go home") || cmd.equals("home")) {
            VoiceNavAccessibilityService.home();
        } else if (cmd.contains("recent")) {
            VoiceNavAccessibilityService.recents();
        } else if (cmd.contains("notification")) {
            VoiceNavAccessibilityService.notifications();
        } else if (cmd.contains("quick setting")) {
            VoiceNavAccessibilityService.quickSettings();
        } else if (cmd.contains("scroll down") || cmd.contains("swipe down")) {
            VoiceNavAccessibilityService.scroll(true);
        } else if (cmd.contains("scroll up") || cmd.contains("swipe up")) {
            VoiceNavAccessibilityService.scroll(false);
        }
    }

    @Override public void onResults(Bundle results) {
        ArrayList<String> list = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (list != null) for (String s : list) process(s);
        listening = false;
        restartSoon();
    }

    @Override public void onPartialResults(Bundle results) {
        ArrayList<String> list = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (list != null) for (String s : list) process(s);
    }

    @Override public void onError(int error) {
        listening = false;
        restartSoon();
    }

    @Override public void onEndOfSpeech() {
        listening = false;
        restartSoon();
    }

    @Override public void onReadyForSpeech(Bundle params) {}
    @Override public void onBeginningOfSpeech() {}
    @Override public void onRmsChanged(float rmsdB) {}
    @Override public void onBufferReceived(byte[] buffer) {}
    @Override public void onEvent(int eventType, Bundle params) {}

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (!listening) begin();
        return START_STICKY;
    }

    @Override public void onDestroy() {
        if (recognizer != null) {
            try { recognizer.destroy(); } catch (Exception ignored) {}
        }
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
