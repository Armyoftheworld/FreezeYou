package cf.playhi.freezeyou;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import net.grandcentrix.tray.AppPreferences;

public class ScreenLockOneKeyFreezeService extends Service {

    private ScreenLockListener screenLockListener;

    @Override
    public void onCreate() {
        super.onCreate();
        if (new AppPreferences(getApplicationContext()).getBoolean("useForegroundService", false) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder mBuilder = new Notification.Builder(this);
                mBuilder.setSmallIcon(R.drawable.ic_notification);
                mBuilder.setContentText(getString(R.string.backgroundService));
                NotificationChannel channel = new NotificationChannel("BackgroundService", getString(R.string.backgroundService), NotificationManager.IMPORTANCE_NONE);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (notificationManager != null)
                    notificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId("BackgroundService");
                Intent resultIntent = new Intent(getApplicationContext(), Main.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                startForeground(1, mBuilder.build());
            } else {
                startForeground(1, new Notification());
            }
        }
        if (screenLockListener == null) {
            screenLockListener = new ScreenLockListener(getApplicationContext());
            screenLockListener.registerListener();
        }
    }

    @Override
    public void onDestroy() {
        if (screenLockListener != null) {
            screenLockListener.unregisterListener();
            screenLockListener = null;
        }
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
