package com.twodwarfs.frienxiety.ui.fragments;


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
import com.twodwarfs.frienxiety.ui.DividerItemDecoration;
import com.twodwarfs.frienxiety.ui.adapters.RelaxationAdapter;
import com.twodwarfs.frienxiety.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RelaxationFragment extends BaseFragment {

    @InjectView(R.id.recyclerView_users)
    RecyclerView mRecyclerView;

    private RelaxationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static RelaxationFragment newInstance() {
        RelaxationFragment fragment = new RelaxationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RelaxationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        setHasOptionsMenu(true);
        setHasBackButton(true);
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
        View rootView = inflater.inflate(R.layout.fragment_relaxation, container, false);
        ButterKnife.inject(this, rootView);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RelaxationAdapter(getActivity());
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
        mAdapter.setOnItemClickListener(new RelaxationAdapter.IOnItemClick() {
            @Override
            public void onItemClick(View v, int position) {

                RelaxationAdapter.RelaxData data = mAdapter.getItem(position);

                if (data != null) {
                    showFragment(RelaxationMethodFragment.newInstance(position), true, true);
                }
            }
        });
    }

    @Override
    public String getName() {
        return Constants.Fragments.RELAXATION;
    }

}
