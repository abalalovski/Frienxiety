package com.twodwarfs.frienxiety.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.model.RelaxMethodsData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */

public class RelaxationMethodsAdapter extends RecyclerView.Adapter<RelaxationMethodsAdapter.ViewHolder> {

    private List<RelaxMethodsData> mDataset = new ArrayList<>();

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
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public RelaxationMethodsAdapter() {
    }

    public void setData(List<RelaxMethodsData> users) {
        mDataset = users;
        notifyDataSetChanged();
    }

    public RelaxMethodsData getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public RelaxationMethodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RelaxationMethodsAdapter.ViewHolder viewHolder, int i) {

        RelaxMethodsData data = mDataset.get(i);

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

}
