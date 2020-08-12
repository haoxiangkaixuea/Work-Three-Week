package cn.edu.scujcc.workthreeweek;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
public class HandlerActivity extends AppCompatActivity {
    public static final int PROGRRESBAR_STAR = 1;
    public static final int PROGRRESBAR_END = -1;
    public int data = 0;
    Boolean isRunning = true;
    private Button btnStart;
    private ProgressBar pb;
    private TextView tvState;
    public Handler updateBarHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRRESBAR_STAR:
                    pb.setProgress(msg.arg1);
                    break;
                case PROGRRESBAR_END:
                    tvState.setText(getResources().getString(R.string.progress_end));
                    pb.setVisibility(View.GONE);
                    pb.setProgress(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };
    private volatile int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        btnStart = findViewById(R.id.bt_start);
        pb = findViewById(R.id.pb);
        tvState = findViewById(R.id.tv_state);
        btnStart.setOnClickListener(v -> {
            tvState.setText(getResources().getString(R.string.progress_star));
            pb.setVisibility(View.VISIBLE);
            doWork();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateBarHandler.removeCallbacksAndMessages(null);
    }

    public void doWork() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(this::timerTaskThread);
        singleThreadPool.shutdown();
    }

    private void timerTaskThread() {
        Message msg = updateBarHandler.obtainMessage();
        //可以避免重复创建Message对象，所以建议用updateBarHandler.obtainMessage()创建Message对象。
        msg.arg1 = i;
        while (isRunning) {
            i++;
            try {
                Thread.sleep(100);
                msg.what = PROGRRESBAR_STAR;
                if (i > 100) {
                    isRunning = false;
                    msg.what = PROGRRESBAR_END;
                    onDestroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //将msg加入到消息队列中
        updateBarHandler.sendMessage(msg);
    }

}

