package ir.code4life.willy.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;

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

    private String board_name;
    private AppDatabase database;
    private DownloadDao downloadDao;
    protected boolean pending = false;
    protected Download extra;

    private BroadcastReceiver receiver;
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDatabase.getInstance(getApplicationContext());
        downloadDao = database.downloadDao();
        extra = downloadDao.downloadExtra();

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
            DownloadTask task = new DownloadTask(downloadDao,extra);
            task.setListener(() -> pending = false);
            task.execute();
        }
    }

    static class DownloadTask extends AsyncTask<Void,Integer,Boolean>{

        private Downloader downloader;
        private List<Download> downloads;
        private Integer count = 0;
        private DownloadDao downloadDao;
        private Download extra;
        Listener listener;

        public DownloadTask(DownloadDao downloadDao,Download extra){
            downloader = Downloader.newInstance();
            this.downloadDao=downloadDao;
            this.extra = extra;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            downloads = downloadDao.getPendingDownloads();
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
        public void onComplete();
    }
}