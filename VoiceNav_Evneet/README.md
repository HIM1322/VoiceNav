# VoiceNav — Evneet

First native Android prototype for Redmi Note 4 / old Android.

Wake phrase:
    Evneet

Commands:
    Evneet, back
    Evneet, go home
    Evneet, recent apps
    Evneet, notifications
    Evneet, quick settings
    Evneet, scroll down
    Evneet, scroll up

Important:
- This build uses Android AccessibilityService for system navigation.
- It uses Android SpeechRecognizer in a foreground service and restarts recognition sessions.
- Actual always-on wake-word reliability depends on the speech-recognition service available on the phone. This is why this is V1: first prove navigation works, then improve the wake-word engine.
- No Kotlin libraries are used.
- minSdk 23 and targetSdk 25 are intentional for old-device compatibility.

Cloud build with Codemagic:
1. Create a GitHub repository named VoiceNav.
2. Upload the entire contents of this folder to the repository root.
3. In Codemagic, Add application -> connect GitHub -> select VoiceNav.
4. Select Native Android.
5. Start the `voice-nav-debug` workflow.
6. Download `app-debug.apk` from Artifacts.
7. Install it on the Redmi Note 4.
8. Open VoiceNav, allow microphone, open Accessibility settings, enable VoiceNav.
9. Return to VoiceNav and tap Start Voice Listening.

Codemagic build command is:
    gradle assembleDebug --no-daemon

The APK artifact is:
    app/build/outputs/apk/debug/app-debug.apk
