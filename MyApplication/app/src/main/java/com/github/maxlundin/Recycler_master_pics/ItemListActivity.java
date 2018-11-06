package com.github.maxlundin.Recycler_master_pics;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.maxlundin.Recycler_master_pics.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.INTERNET;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private static final String LOG_TAG = ItemListActivity.class.getSimpleName();

    private final static String[] Addreses = {
            "https://api.unsplash.com/search/photos?query=canada&client_id=eb024d8723958653f7bb48f7c8eef1bbb32eeff96776e83584ca49db33cc1eea"};
    private static ArrayList<DummyContent.DummyItem> LINKS_AND_DESCRIPTION = new ArrayList<>();
    final Context myActivity = this;
    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter adapterForListOfNamesOfPictures;
    private ServiceConnection serviceConnection1 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LoaderJson mMyService = ((LoaderJson.MyBinder) service).getService();
            mMyService.setOnLoad(data ->
            {
                LINKS_AND_DESCRIPTION = data;
                Log.d(LOG_TAG, LINKS_AND_DESCRIPTION.size() + " ");
                LoadPics();
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void LoadPics() {


        adapterForListOfNamesOfPictures = new SimpleItemRecyclerViewAdapter(this, new DummyContent(LINKS_AND_DESCRIPTION.size()), mTwoPane);

        RecyclerView listNames = findViewById(R.id.item_list);
        listNames.setAdapter(adapterForListOfNamesOfPictures);
        //if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        listNames.setLayoutManager(new LinearLayoutManager(myActivity));
        ContextCompat.checkSelfPermission(myActivity, INTERNET);
        for (int i = 0; i < LINKS_AND_DESCRIPTION.size(); i++) {
            Log.d(LOG_TAG, LINKS_AND_DESCRIPTION.get(i).toString());
            adapterForListOfNamesOfPictures.setElement(i, LINKS_AND_DESCRIPTION.get(i));
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (LINKS_AND_DESCRIPTION.size() == 0) {
            LINKS_AND_DESCRIPTION.add(new DummyContent.DummyItem("0", new Pair<>("Утя", "https://cs9.pikabu.ru/post_img/2017/01/18/7/1484734874175140604.jpg")));
            ContextCompat.checkSelfPermission(this, INTERNET);
            for (int i = 0; i < Addreses.length; i++) {
                LoaderJson.load(myActivity, i, Addreses[i]);
            }
            bindService(new Intent(this, LoaderJson.class), serviceConnection1, 0);

            Log.d(LOG_TAG, LINKS_AND_DESCRIPTION.size() + "Hello");
        } else {
            LoadPics();
        }


        //setupRecyclerView((RecyclerView) recyclerView);
    }

    //private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    //      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
//    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final DummyContent mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      DummyContent items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
//            holder.mIdView.setText(mValues.get(position).id);
//            holder.mContentView.setText(mValues.get(position).content);
//
            holder.itemView.setTag(mValues.get(i));
            holder.itemView.setOnClickListener(mOnClickListener);

            holder.bind(mValues.get(i), i);
        }

        public void setElement(int pos, DummyContent.DummyItem data) {
            this.mValues.set(pos, data);
            notifyItemChanged(pos);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ProgressBar progress;
            final TextView description;

            ViewHolder(@NonNull View view) {
                super(view);
                progress = itemView.findViewById(R.id.progresstext);
                description = itemView.findViewById(R.id.description);
            }

            void bind(DummyContent.DummyItem data, int i) {
                if (data == null) {
                    progress.setVisibility(View.VISIBLE);
                    description.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.GONE);
                    description.setVisibility(View.VISIBLE);
                    description.setText(data.content.first);
//                description.setOnClickListener(view ->
////                        Log.d(LOG_TAG, "clicked on -> " + data.first);
//                        Loader.load(myContext, i, data.second, size_adapter));

                }
            }
        }
    }
}
