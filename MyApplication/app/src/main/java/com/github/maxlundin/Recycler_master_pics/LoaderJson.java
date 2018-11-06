package com.github.maxlundin.Recycler_master_pics;


import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.github.maxlundin.Recycler_master_pics.Gson.MyGson;
import com.github.maxlundin.Recycler_master_pics.dummy.DummyContent;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;

public class LoaderJson extends IntentService {
    private static final String ACTION_LOAD = "unsplash.action.loadJson";
    private static final String EXTRA_URL = "unsplash.extra.url.json";
    private static final String EXTRA_ID = "unsplash.extra.id.json";
    private static final String LOG_TAG = LoaderJson.class.getSimpleName();

    private final Queue<Pair<Integer, byte[]>> responses1 = new LinkedList<>();
    private final Handler main1 = new Handler(Looper.getMainLooper());
    private OnLoad callback1;
    private static ArrayList<DummyContent.DummyItem> LINKS1 = new ArrayList<>();

    public LoaderJson() {
        super("LoaderJson");
    }


    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void load(Context context, int id, String url) {
        Log.d(LOG_TAG, "first");
        Intent intent = new Intent(context, LoaderJson.class);
        intent.setAction(ACTION_LOAD);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_ID, id);
        // intent.putStringArrayListExtra(EXTRA_LINKS, Links);
//        LINKS1 = Links;
        Log.d(LOG_TAG, "Json load start");

        context.startService(intent);
        Log.d(LOG_TAG, "Json load finish" + LINKS1.size());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "On handle");
        if (intent != null) {
            Log.d(LOG_TAG, "If1");
            val action = intent.getAction();
            val url = intent.getStringExtra(EXTRA_URL);
            val id = intent.getIntExtra(EXTRA_ID, -1);
            // ArrayList<String> Links = intent.getStringArrayListExtra(EXTRA_LINKS);
            String st = null;
            if (ACTION_LOAD.equals(action) && url != null && id != -1) {
                Log.d(LOG_TAG, "If2");
                st = loadJson(id, url);
            }
            MyGson r;
            if (st != null) {
                r = parse(st);
                for (int i = 0; i < r.getResults().size(); i++) {
                    LINKS1.add(new DummyContent.DummyItem(i + "", new Pair<String, String>(r.getResults().get(i).getDescription(), r.getResults().get(i).getUrls().getRegular())));
                }
            }
            Log.d(LOG_TAG, "koroche mi sdelali" + LINKS1.size() + LINKS1.get(1));
            main1.post(() -> deliver(LINKS1));
        }
    }

    MyGson parse(String st) {
        Gson gson = new Gson();
        return gson.fromJson(st, MyGson.class);
    }

    public static String loadJson(int id, @NonNull final String Link) {
        Log.d(LOG_TAG, "Intenet Json load start");
        StringBuilder st = new StringBuilder("");
        try {
            URL url = new URL(Link);
            Log.d(LOG_TAG, "1");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.d(LOG_TAG, "2");
            try {
                Log.d(LOG_TAG, "3");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d(LOG_TAG, "4");
                int a = in.read();
                while (a != -1) {
                    st.append((char) a);
                    a = in.read();
                }
                Log.d(LOG_TAG, "5");

            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "\n" + "string output Json -> " + st.toString() + "\n");
        return st.toString();
    }


    private void deliver(ArrayList<DummyContent.DummyItem> st) {
        callback1.onLoad(st);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind: ");
        return new MyBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "onUnbind: ");
        callback1 = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(LOG_TAG, "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate: ");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(LOG_TAG, "onStart: ");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy: ");
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public class MyBinder extends Binder {
        private final LoaderJson service;

        public LoaderJson getService() {
            return service;
        }
    }

    public void setOnLoad(OnLoad onLoad) {
        this.callback1 = onLoad;
    }

    public interface OnLoad {
        public void onLoad(ArrayList<DummyContent.DummyItem> data);
    }
}   
