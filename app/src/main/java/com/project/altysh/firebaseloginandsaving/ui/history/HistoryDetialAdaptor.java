package com.project.altysh.firebaseloginandsaving.ui.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.altysh.firebaseloginandsaving.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Altysh on 3/19/2018.
 */

public class HistoryDetialAdaptor extends RecyclerView.Adapter<HistoryDetialAdaptor.ViewHolder> {
    ArrayList<String> names = new ArrayList<>();
    Context context;


    public HistoryDetialAdaptor(ArrayList<String> strings, Context context) {
        this.names = strings;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public void setHistoryDtoArrayList(ArrayList<String> historyDtoArrayList) {
        this.names = historyDtoArrayList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_distnation_detail, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = names.get(position);
        if (position == 0)
            holder.getType().setText(R.string.frompoint);
        else holder.getType().setText(R.string.topoint);
        holder.getName().setText(name);


    }


    @Override
    public int getItemCount() {
        return this.names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            name = itemView.findViewById(R.id.name);
        }

        public TextView getType() {
            return type;
        }

        public void setType(TextView type) {
            this.type = type;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }
    }
}
