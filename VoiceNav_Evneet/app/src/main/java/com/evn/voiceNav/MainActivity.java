package com.evn.voiceNav;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity {
    private static final int MIC_REQ = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText("VoiceNav — Evneet");
        title.setTextSize(28);
        title.setTextColor(Color.BLACK);

        TextView info = new TextView(this);
        info.setText("\n1. Enable VoiceNav in Accessibility.\n2. Allow microphone permission.\n3. Keep the service enabled.\n\nWake word: “Evneet”\nCommands: back, home, recent apps, notifications, quick settings, scroll up, scroll down.\n\nThis first build is intentionally small and uses no Kotlin libraries.");
        info.setTextSize(17);

        Button accessibility = new Button(this);
        accessibility.setText("Open Accessibility Settings");
        accessibility.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));

        Button mic = new Button(this);
        mic.setText("Allow Microphone");
        mic.setOnClickListener(v -> requestMic());

        Button start = new Button(this);
        start.setText("Start Voice Listening");
        start.setOnClickListener(v -> startListeningService());

        box.addView(title);
        box.addView(info);
        box.addView(accessibility);
        box.addView(mic);
        box.addView(start);
        setContentView(box);

        if (android.os.Build.VERSION.SDK_INT >= 23 &&
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestMic();
        }
    }

    private void requestMic() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, MIC_REQ);
        }
    }

    private void startListeningService() {
        Intent i = new Intent(this, VoiceListenService.class);
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            startForegroundService(i);
        } else {
            startService(i);
        }
    }
}
