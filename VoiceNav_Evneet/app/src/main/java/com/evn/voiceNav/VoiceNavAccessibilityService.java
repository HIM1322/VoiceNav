package com.evn.voiceNav;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;
import android.os.Build;

public class VoiceNavAccessibilityService extends AccessibilityService {
    public static VoiceNavAccessibilityService instance;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {}

    @Override
    public void onDestroy() {
        if (instance == this) instance = null;
        super.onDestroy();
    }

    public static boolean back() {
        return instance != null && instance.performGlobalAction(GLOBAL_ACTION_BACK);
    }

    public static boolean home() {
        return instance != null && instance.performGlobalAction(GLOBAL_ACTION_HOME);
    }

    public static boolean recents() {
        return instance != null && instance.performGlobalAction(GLOBAL_ACTION_RECENTS);
    }

    public static boolean notifications() {
        return instance != null && instance.performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS);
    }

    public static boolean quickSettings() {
        return instance != null && instance.performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS);
    }

    public static boolean scroll(boolean down) {
        if (instance == null || Build.VERSION.SDK_INT < 24) return false;
        Path p = new Path();
        float x = 540f;
        if (down) {
            p.moveTo(x, 500f);
            p.lineTo(x, 1400f);
        } else {
            p.moveTo(x, 1400f);
            p.lineTo(x, 500f);
        }
        GestureDescription.StrokeDescription stroke =
                new GestureDescription.StrokeDescription(p, 0, 500);
        return instance.dispatchGesture(new GestureDescription.Builder()
                .addStroke(stroke).build(), null, null);
    }
}
