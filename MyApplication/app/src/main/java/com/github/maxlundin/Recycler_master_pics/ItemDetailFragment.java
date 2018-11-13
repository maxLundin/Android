package com.github.maxlundin.Recycler_master_pics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maxlundin.Recycler_master_pics.dummy.DummyContent;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import lombok.experimental.var;
import lombok.val;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private static final String LOG_TAG = ItemDetailFragment.class.getSimpleName();


    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content.first);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadFilesTask extends AsyncTask<String, Integer, byte[]> {
        @Override
        protected byte[] doInBackground(String... url1) {
            File directory = getContext().getFilesDir();
            File file = new File(directory, url1[1]);
            byte[] mas;
            if (file.exists()) {
                try {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    val reader = new FileInputStream(file);
                    int a;
                    mas = new byte[2048];
                    while ((a = reader.read(mas)) != -1) {
                        buffer.write(mas, 0, a);
                    }
                    mas = buffer.toByteArray();
                    Log.d(LOG_TAG, "Image " + mItem.content.first + " Loaded from cache");
                } catch (IOException e) {
                    mas = null;
                }
                return mas;
            } else {
                while (true) {
                    byte[] res;
                    try {
                        String urlString = url1[0];
                        Log.d(LOG_TAG, urlString + "\n");
                        val url = new URL(urlString);
                        val connection = url.openConnection();
                        connection.connect();
                        int leng = connection.getContentLength();
                        if (leng == -1) {
                            Log.d(LOG_TAG, "Error loading : \n " + urlString + "\n");
                            continue;
                        }
                        res = new byte[leng];
                        try (val is = connection.getInputStream()) {
                            int p = 0;
                            int r;
                            while ((r = is.read(res, p, res.length - p)) > 0) p += r;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        res = null;
                    }
                    val result = res;
                    Log.d(LOG_TAG, "loadPicture: got: id: " + ", data.length = " + (res == null ? null : res.length));

                    FileOutputStream outputStream;
                    try {
                        String urlString = url1[1];
                        Log.d(LOG_TAG, urlString + "\n");
                        //File file = new File(getContext().getFilesDir(), urlString);

                        outputStream = getContext().openFileOutput(urlString, Context.MODE_PRIVATE);
                        outputStream.write(result);
                        outputStream.close();


                    } catch (IOException e) {
                        e.printStackTrace();
                        res = null;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "Mama eto medved'");
                    }
                    Log.d(LOG_TAG, "CachedPicture: got: id: " + ", data.length = " + (res == null ? null : res.length));


                    return result;
                }
            }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        rootView.findViewById(R.id.progresspic).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.item_detail).setVisibility(View.GONE);
        if (mItem != null) {
            byte[] mas;
            ArrayList<Byte> download = new ArrayList<>();

            while (true) {

                val res = new DownloadFilesTask().execute(mItem.content.second, mItem.content.first);
                try {
                    mas = res.get();
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Download failed");
                    continue;
                }
                break;
            }

            val bitmap = mas != null ? new Bitmap[mas.length] : new Bitmap[0];
            rootView.findViewById(R.id.progresspic).setVisibility(View.GONE);
            rootView.findViewById(R.id.item_detail).setVisibility(View.VISIBLE);
            ((ImageView) rootView.findViewById(R.id.item_detail)).setImageBitmap(BitmapFactory.decodeByteArray(mas, 0, mas != null ? mas.length : 0));


        }
        return rootView;
    }
}
