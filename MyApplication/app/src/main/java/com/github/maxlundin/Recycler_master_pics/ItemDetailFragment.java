package com.github.maxlundin.Recycler_master_pics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
//                main.post(() -> deliver(id, result));
                return result;
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        rootView.findViewById(R.id.progresspic).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.item_detail).setVisibility(View.GONE);
        if (mItem != null) {
            //com.example.max.myapplication.Loader.load(, 1, mItem.content.second);
            byte[] mas;
            while (true) {
                val res = new DownloadFilesTask().execute(mItem.content.second);
                try {
                    mas = res.get();
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Download failed");
                    continue;
                }
                break;
            }
            val bitmap = new Bitmap[mas.length];
            rootView.findViewById(R.id.progresspic).setVisibility(View.GONE);
            rootView.findViewById(R.id.item_detail).setVisibility(View.VISIBLE);
            ((ImageView) rootView.findViewById(R.id.item_detail)).setImageBitmap(BitmapFactory.decodeByteArray(mas, 0, mas.length));
        }

        return rootView;
    }
}
