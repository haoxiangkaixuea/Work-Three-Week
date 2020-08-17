package cn.edu.scujcc.workthreeweek;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * @author Administrator
 */
public class MyIntentService extends IntentService {
    private LocalBroadcastManager mLocalBroadcastManager;

    public MyIntentService() {
        super("MyIntentService");
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int date = 0;
        boolean isRunning = true;
        while (isRunning) {
            try {
                date += 1;
                if (date >= 100) {
                    isRunning = false;
                }
                Thread.sleep(100);
                sendThreadStatus(date);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendThreadStatus(int progress) {
        Intent intent = new Intent(IntentServiceActivity.ACTION);
        intent.putExtra("progress", progress);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 复写onStartCommand()方法
     * 默认实现 = 将请求的Intent添加到工作队列里
     **/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
