package com.serialmmf.Anbattery.memory;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.dialogs.KillDialog;
import com.serialmmf.Anbattery.model.MemoryDataManager;
import com.serialmmf.Anbattery.model.MemoryObserver;
import com.serialmmf.Anbattery.model.Task;

import java.util.ArrayList;

/**
 * Created by juancarlos on 25/2/16.
 */
public class MemoryFragment extends Fragment implements MemoryView, MemoryObserver, OnClickListener {

    private static final String TAG = "MemoryFragment";
    private MemoryPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private ArrayList<Task> mData;
    private Button mCleanButton;
    private View mLoadingOverlay;
    private ItemTouchHelper mItemTouchHelper;
    private AdRequest mAdRequest;

    ItemTouchHelper.SimpleCallback itemTouchCallback = new SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mPresenter.itemSwiped(mData.get(position), position);
        }
    };

    public MemoryFragment() {

    }

    public static Fragment newInstance() {
        MemoryFragment fragment = new MemoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdRequest = new AdRequest.Builder().build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        initAdView(view);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.listView);

        mCleanButton = (Button)view.findViewById(R.id.clean_button);
        mCleanButton.setVisibility(View.VISIBLE);
        mCleanButton.setOnClickListener(this);

        mData = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new MemoryListAdapter(mData));

        mItemTouchHelper =  new ItemTouchHelper(itemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mLoadingOverlay = view.findViewById(R.id.loading_overlay_view);

        mPresenter = new MemoryPresenter(this, getActivity());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        MemoryDataManager memoryManager = MemoryDataManager.getInstance();
        memoryManager.addObserver(this);
        memoryManager.getMemoryData(getActivity());
    }

    @Override
    public void showKillDialog(String taskName) {
        DialogFragment dialog = KillDialog.newInstance(mPresenter,taskName);
        dialog.show(getFragmentManager(), "killDialog");
    }

    @Override
    public void removeItem(Task task, int position) {
        mData.remove(task);
        ((MemoryListAdapter)mRecyclerView.getAdapter()).updateData(mData);
    }

    @Override
    public void restoreItem() {
        ((MemoryListAdapter)mRecyclerView.getAdapter()).updateData(mData);
    }

    @Override
    public void onMemoryDataUpdated(Integer... values) {

    }

    @Override
    public void onMemoryScanFinished(ArrayList<Task> tasksList, long memory) {
        mData = new ArrayList<>(tasksList);
        ((MemoryListAdapter)mRecyclerView.getAdapter()).updateData(mData);

        mLoadingOverlay.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean_button:
                mLoadingOverlay.setVisibility(View.VISIBLE);
                MemoryDataManager.getInstance().cleanMemory(getActivity(), mData);
        }
    }

    private void initAdView(View rootView) {
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        mAdView.loadAd(mAdRequest);
    }
}
