package com.twodwarfs.frienxiety.ui.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.model.RelaxMethodsData;
import com.twodwarfs.frienxiety.ui.DividerItemDecoration;
import com.twodwarfs.frienxiety.ui.adapters.RelaxationAdapter;
import com.twodwarfs.frienxiety.ui.adapters.RelaxationMethodsAdapter;
import com.twodwarfs.frienxiety.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RelaxationMethodFragment extends BaseFragment {

    @InjectView(R.id.recyclerView_relax_methods)
    RecyclerView mRecyclerView;

    private RelaxationMethodsAdapter mAdapter = new RelaxationMethodsAdapter();
    private RecyclerView.LayoutManager mLayoutManager;

    public static RelaxationMethodFragment newInstance(int type) {
        RelaxationMethodFragment fragment = new RelaxationMethodFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.Fields.RELAX_METOD, type);
        fragment.setArguments(args);
        return fragment;
    }

    public RelaxationMethodFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int type = getArguments().getInt(Constants.Fields.RELAX_METOD);
            mAdapter.setData(getDatasetForType(type));
        }

        setHasOptionsMenu(true);
        setHasBackButton(true);
    }

    private List<RelaxMethodsData> getDatasetForType(int type) {
        int titlesResId = 0;
        int descsResId = 0;
        int resResId = 0;

        List<RelaxMethodsData> dataset = new ArrayList<>();

        if (type == 0) {
            titlesResId = R.array.binaural_titles;
            descsResId = R.array.binaural_descriptions;
            resResId = R.array.binaural_res_ids;
        } else if (type == 1) {
            titlesResId = R.array.chromo_titles;
            descsResId = R.array.chromo_descs;
            resResId = R.array.chromo_res_ids;
        }

        List<String> titles = Arrays.asList(getResources().getStringArray(titlesResId));
        List<String> descs = Arrays.asList(getResources().getStringArray(descsResId));
        TypedArray icons = getResources().obtainTypedArray(resResId);

        for (int i = 0; i < titles.size(); i++) {
            RelaxMethodsData data = new RelaxMethodsData();
            data.title = titles.get(i);
            data.description = descs.get(i);
            data.resId = icons.getResourceId(i, 0);
            data.type = type;

            dataset.add(data);
        }

        icons.recycle();
        return dataset;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getString(R.string.relaxation_title));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.relaxation_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_relax_methods, container, false);
        ButterKnife.inject(this, rootView);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        initData();
    }

    @Override
    protected void initData() {
        mAdapter.setOnItemClickListener(new RelaxationMethodsAdapter.IOnItemClick() {
            @Override
            public void onItemClick(View v, final int position) {

                RelaxMethodsData data = mAdapter.getItem(position);
                if (data != null) {
                    if (data.type == 0) {
                        // binaural play
                    }
                    if (data.type == 1) {
                        showFragment(ColorFragment.newInstance(position), true, true);
                    }
                }
            }
        });
    }

    @Override
    public String getName() {
        return Constants.Fragments.RELAXATION;
    }
}
