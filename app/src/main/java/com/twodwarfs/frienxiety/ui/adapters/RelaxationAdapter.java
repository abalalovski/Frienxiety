package com.twodwarfs.frienxiety.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twodwarfs.frienxiety.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */

public class RelaxationAdapter extends RecyclerView.Adapter<RelaxationAdapter.ViewHolder> {

    private List<RelaxData> mDataset = new ArrayList<>();

    public interface IOnItemClick {
        void onItemClick(View v, int position);
    }

    static IOnItemClick mOnItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mMainView;

        public ViewHolder(View v) {
            super(v);
            mMainView = v;

            mMainView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public RelaxationAdapter(Context context) {

        List<String> titles = Arrays.asList(context.getResources().getStringArray(R.array.relaxation_items));
        List<String> descs = Arrays.asList(context.getResources().getStringArray(R.array.relaxation_description));

        TypedArray icons = context.getResources().obtainTypedArray(R.array.relaxation_icons);

        for (int i = 0; i < titles.size(); i++) {
            RelaxData data = new RelaxData();
            data.title = titles.get(i);
            data.description = descs.get(i);
            data.resId = icons.getResourceId(i, 0);

            mDataset.add(data);
        }

        icons.recycle();
        setData(mDataset);
    }


    public void setData(List<RelaxData> users) {
        mDataset = users;

        notifyDataSetChanged();
    }

    public RelaxData getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public RelaxationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RelaxationAdapter.ViewHolder viewHolder, int i) {

        RelaxData data = mDataset.get(i);

        ((TextView) viewHolder.mMainView.findViewById(R.id.textView_relax_title)).setText(data.title);
        ((TextView) viewHolder.mMainView.findViewById(R.id.textView_relax_desc)).setText(data.description);
        viewHolder.mMainView.findViewById(R.id.imageView_avatar).setBackgroundResource(mDataset.get(i).resId);
    }

    public void setOnItemClickListener(IOnItemClick listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class RelaxData {
        public String title;
        public String description;
        public int resId;
    }
}
