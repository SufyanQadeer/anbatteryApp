package com.serialmmf.Anbattery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.model.Appinfo;

import java.util.ArrayList;
import java.util.List;

public class InstallAppAdapter extends RecyclerView.Adapter<InstallAppAdapter.InstallAppViewHolder> {
    Fragment fragment;
    public List<Appinfo> list=new ArrayList<>();

    public InstallAppAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public InstallAppAdapter.InstallAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(fragment.getActivity()).inflate(R.layout.app_icon_item_design,parent,false);
        return new InstallAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InstallAppAdapter.InstallAppViewHolder holder, int position) {
        holder.img_app_icon.setImageDrawable(list.get(position).icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Appinfo model) {
        list.add(model);
    }



    public class InstallAppViewHolder extends RecyclerView.ViewHolder {
        ImageView img_app_icon;
        public InstallAppViewHolder(View itemView) {
            super(itemView);
            img_app_icon=(ImageView)itemView.findViewById(R.id.img_app_icon);
        }
    }
}
