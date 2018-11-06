package com.github.maxlundin.Recycler_master_pics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AdapterForListOfNamesOfPictures extends RecyclerView.Adapter<AdapterForListOfNamesOfPictures.VH> {
    private final Pair<String, String>[] description;
    private final Context myContext;
    private int size_adapter;
    private static final String LOG_TAG = AdapterForListOfNamesOfPictures.class.getSimpleName();

    public AdapterForListOfNamesOfPictures(int size, Context context) {
        this.description = new Pair[size];
        myContext = context;
    }

    @NonNull
    @Override
    public AdapterForListOfNamesOfPictures.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AdapterForListOfNamesOfPictures.VH(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_item_list,
                viewGroup, false));
    }

    public void setElement(int pos, Pair<String, String> data, int size_adapter) {
        this.description[pos] = data;
        this.size_adapter = size_adapter;
        notifyItemChanged(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForListOfNamesOfPictures.VH vh, int i) {
        vh.bind(description[i], i);
    }


    @Override
    public int getItemCount() {
        return description.length;
    }


    class VH extends RecyclerView.ViewHolder {
        final ProgressBar progress;
        final TextView description;

        VH(@NonNull View view) {
            super(view);
            progress = itemView.findViewById(R.id.progresstext);
            description = itemView.findViewById(R.id.description);
        }

        void bind(Pair<String, String> data, int i) {
            if (data == null) {
                progress.setVisibility(View.VISIBLE);
                description.setVisibility(View.GONE);
            } else {
                progress.setVisibility(View.GONE);
                description.setVisibility(View.VISIBLE);
                description.setText(data.first);
//                description.setOnClickListener(view ->
////                        Log.d(LOG_TAG, "clicked on -> " + data.first);
//                        Loader.load(myContext, i, data.second, size_adapter));

            }
        }
    }


}
