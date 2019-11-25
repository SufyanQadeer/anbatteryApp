package com.serialmmf.Anbattery.memory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.model.Task;

import java.util.ArrayList;

/**
 * Created by juancarlos on 25/2/16.
 */
public class MemoryListAdapter extends RecyclerView.Adapter implements OnClickListener {
    private ArrayList<Task> mTasks;

    public MemoryListAdapter(ArrayList<Task> items) {
        mTasks = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        TaskViewHolder appViewHolder = new TaskViewHolder(view);

        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTasks.get(position);
        TaskViewHolder taskHolder = (TaskViewHolder)holder;

        taskHolder.name.setText(task.getTitle());
        taskHolder.icon.setImageDrawable(task.getIconDrawable());
        taskHolder.memUsage.setText("MEM: " + task.getMemUsage());
        taskHolder.cpuUsage.setText("CPU: " + task.getCPUUsage());
        taskHolder.check.setChecked(task.isChecked());
        taskHolder.check.setTag(task);
        taskHolder.check.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public void onClick(View v) {
        CheckBox checkBox = (CheckBox)v;
        Task task = (Task)checkBox.getTag();
        task.setChecked(checkBox.isChecked());
    }

    private static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;
        TextView memUsage;
        TextView cpuUsage;
        CheckBox check;

        public TaskViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.app_name_textView);
            icon = (ImageView)itemView.findViewById(R.id.icon_imageView);
            memUsage = (TextView)itemView.findViewById(R.id.mem_usage_textView);
            cpuUsage = (TextView)itemView.findViewById(R.id.cpu_usage_textView);
            check = (CheckBox)itemView.findViewById(R.id.task_checkbox);
        }
    }

    public void updateData(ArrayList<Task> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }
}
