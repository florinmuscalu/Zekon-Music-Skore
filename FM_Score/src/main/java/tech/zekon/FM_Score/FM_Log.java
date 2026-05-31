package tech.zekon.FM_Score;

import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Diagnostics seam for the FM_Score engine.
 *
 * <p>Writes to Logcat by default and keeps the library free of any crash-reporting
 * dependency. An application can install a {@link Handler} via {@link #setHandler}
 * to forward engine errors to its own reporter (e.g. Firebase Crashlytics), so that
 * caught-and-handled engine failures become visible in production instead of silent.
 */
public final class FM_Log {

    /** Receives engine error events; install one from the app to forward to crash reporting. */
    public interface Handler {
        void log(String tag, String message, @Nullable Throwable t);
    }

    @Nullable
    private static volatile Handler handler;

    private FM_Log() {
    }

    public static void setHandler(@Nullable Handler h) {
        handler = h;
    }

    /** Error: logged locally and forwarded to the installed handler (if any). */
    static void e(String tag, String message, @Nullable Throwable t) {
        if (t != null) {
            Log.e(tag, message, t);
        } else {
            Log.e(tag, message);
        }
        Handler h = handler;
        if (h != null) {
            try {
                h.log(tag, message, t);
            } catch (Exception ignored) {
                // Diagnostics must never crash the engine.
            }
        }
    }

    /** Warning: logged locally only (recoverable or high-frequency paths). */
    static void w(String tag, String message, @Nullable Throwable t) {
        if (t != null) {
            Log.w(tag, message, t);
        } else {
            Log.w(tag, message);
        }
    }
}
