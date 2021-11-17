package ir.code4life.willy.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.DownloadDao;
import ir.code4life.willy.database.models.Download;
import ir.code4life.willy.util.Downloader;

public class DownloadService extends Service {
    public static String START_DOWNLOAD = "start download";
    public static boolean DEBUG = false;
    public static Integer NOTIFICATION_ID = 0;
    private DownloadDao downloadDao;
    protected boolean pending = false;
    protected Download extra;

    private BroadcastReceiver receiver;
    private NotificationManager manager;

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        downloadDao = database.downloadDao();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(START_DOWNLOAD)){
                    download();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(START_DOWNLOAD);
        registerReceiver(receiver,filter);
    }


    private void download(){
        if(!pending){
            pending = true;
            extra = downloadDao.downloadExtra();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getPackageName());
            builder.setSmallIcon(android.R.drawable.stat_sys_download);
            builder.setContentTitle("Downloading ...");
            builder.setOnlyAlertOnce(true);
            TaskRunner task = new TaskRunner(downloadDao,extra);
            task.execute(new Listener() {
                @Override
                public void onComplete() {
                    pending = false;
                    builder.setContentTitle("Download completed");
                    builder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                    manager.notify(NOTIFICATION_ID,builder.build());
                }

                @Override
                public void onProgress(Integer progress, Integer total) {
                    builder.setProgress(total,progress,false);
                    builder.setContentText(String.format(Locale.ENGLISH,"Downloading %d of %d pending", progress,total));
                    manager.notify(NOTIFICATION_ID,builder.build());
                }
            });
        }
    }

    static class TaskRunner{
        private final Executor executor = Executors.newSingleThreadExecutor();
        private final Handler handler = new Handler(Looper.getMainLooper());
        private final Downloader downloader = Downloader.newInstance();
        private final DownloadDao downloadDao;
        private final Download extra;

        public TaskRunner(DownloadDao downloadDao,Download extra){
            this.downloadDao = downloadDao;
            this.extra = extra;
        }

        public void execute(Listener listener){
            executor.execute(() -> {
                Integer count = 0;
                List<Download> downloads = downloadDao.getPendingDownloads();
                while(!downloads.isEmpty()){
                    for(Download download: downloads){
                        count++;
                        if(DEBUG){
                            download.status = true;
                        }else{
                            download.status = downloader.download(download);
                            downloadDao.updatePin(download.pin_id,download.path);
                        }
                        downloadDao.update(download);
                        listener.onProgress(count,extra.total-extra.completed);
                    }
                    downloads.clear();
                    downloads.addAll(downloadDao.getPendingDownloads());
                }

                handler.post(listener::onComplete);
            });
        }
    }

    interface Listener{
        void onComplete();
        void onProgress(Integer progress,Integer total);
    }
}