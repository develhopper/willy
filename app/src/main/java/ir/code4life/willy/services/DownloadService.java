package ir.code4life.willy.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.dao.DownloadDao;
import ir.code4life.willy.database.dao.PinDao;
import ir.code4life.willy.database.models.BoardWithPins;
import ir.code4life.willy.database.models.Download;
import ir.code4life.willy.database.models.PinWithBoard;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.Downloader;
import ir.code4life.willy.util.G;

public class DownloadService extends Service {
    public static String START_DOWNLOAD = "start download";
    public static String DOWNLOAD_PROGRESS = "download progress";
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
            DownloadTask task = new DownloadTask(downloadDao,extra);
            task.setListener(new Listener() {
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
                    builder.setContentText(String.format("Downloading %d of %d pending", progress,total));
                    manager.notify(NOTIFICATION_ID,builder.build());
                }
            });
            task.execute();
        }
    }

    static class DownloadTask extends AsyncTask<Void,Integer,Boolean>{

        private final Downloader downloader;
        private final DownloadDao downloadDao;
        private final Download extra;
        Listener listener;

        public DownloadTask(DownloadDao downloadDao,Download extra){
            downloader = Downloader.newInstance();
            this.downloadDao=downloadDao;
            this.extra = extra;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Integer count = 0;
            List<Download> downloads = downloadDao.getPendingDownloads();
            while(!downloads.isEmpty()){
                for(Download download: downloads){
                    count++;
                    if(DEBUG){
                        download.status = true;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        download.status = downloader.download(download);
                        downloadDao.updatePin(download.pin_id,download.path);
                    }
                    downloadDao.update(download);
                    publishProgress(count,extra.total);
                }
                downloads.clear();
                downloads.addAll(downloadDao.getPendingDownloads());
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            listener.onProgress(values[0],values[1]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(listener!=null)
                listener.onComplete();
        }

        protected void setListener(Listener listener){
            this.listener = listener;
        }
    }


    interface Listener{
        void onComplete();
        void onProgress(Integer progress,Integer total);
    }
}