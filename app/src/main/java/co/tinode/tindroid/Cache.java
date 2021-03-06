package co.tinode.tindroid;

import android.os.Build;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

import co.tinode.tindroid.db.BaseDb;
import co.tinode.tindroid.media.VxCard;
import co.tinode.tinodesdk.Tinode;
import co.tinode.tinodesdk.model.PrivateType;

/**
 * Shared resources.
 */
public class Cache {
    private static final String TAG = "Cache";

    // public static String sHost = "api.tinode.co"; // remote host
    public static final String HOST_NAME = "10.0.2.2:6060"; // local host

    private static final String API_KEY = "AQEAAAABAAD_rAp4DJh05a1HAwFT3A6K";

    private static Tinode sTinode;

    private static int sUniqueCounter = 100;

    public static Tinode getTinode() {
        if (sTinode == null) {
            Log.d(TAG, "Tinode instantiated");

            sTinode = new Tinode("Tindroid/" + TindroidApp.getAppVersion(), API_KEY,
                    BaseDb.getInstance().getStore(), null);
            sTinode.setOsString(Build.VERSION.RELEASE);

            // Default types for parsing Public, Private fields of messages
            sTinode.setDefaultTypeOfMetaPacket(VxCard.class, PrivateType.class);
            sTinode.setMeTypeOfMetaPacket(VxCard.class);
            sTinode.setFndTypeOfMetaPacket(VxCard.class);

            // Set device language
            sTinode.setLanguage(Locale.getDefault().getLanguage());
            sTinode.setAutologin(true);

            // Keep in app to prevent garbage collection.
            TindroidApp.retainTinodeCache(sTinode);
        }

        sTinode.setDeviceToken(FirebaseInstanceId.getInstance().getToken());
        return sTinode;
    }

    // Invalidate existing cache.
    public static void invalidate() {
        if (sTinode != null) {
            sTinode.logout();
            sTinode = null;
        }
    }

    public static int getUniqueCounter() {
        return ++sUniqueCounter;
    }
}
