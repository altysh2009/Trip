package com.project.altysh.firebaseloginandsaving.ui.history;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Altysh on 3/17/2018.
 */

public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.ViewHolder> {
    ArrayList<HistoryDto> historyDtoArrayList = new ArrayList<>();
    Context context;

    public HistoryAdaptor(Context context) {
        this.historyDtoArrayList = historyDtoArrayList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public void setHistoryDtoArrayList(ArrayList<HistoryDto> historyDtoArrayList) {
        this.historyDtoArrayList = historyDtoArrayList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryDto historyDto = historyDtoArrayList.get(position);
        holder.getName().setText(historyDto.getTrip_dto().getTripName());
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dateText = df2.format(historyDto.getTrip_dto().getDateTime());
        holder.getDate().setText(dateText);
        holder.getDistance().setText(historyDto.getDistance() + "");
        holder.getDurtaiion().setText(historyDto.getDurtation() + "");
        holder.getStatus().setText("done");
        holder.setHistoryDto(historyDto);


        Picasso.get().load(historyDto.getTrip_dto().getImageWithRoute())
                .placeholder(R.drawable.ic_done)
                .error(R.drawable.ic_done).fit().centerCrop()

                .into(holder.getRoute());


    }

    @Override
    public int getItemCount() {
        return this.historyDtoArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView durtaiion;
        private TextView distance;
        private TextView status;
        private ImageView route;
        private HistoryDto historyDto = null;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            durtaiion = itemView.findViewById(R.id.durtation);
            distance = itemView.findViewById(R.id.distance);
            status = itemView.findViewById(R.id.status);
            route = itemView.findViewById(R.id.route);
            cardView = itemView.findViewById(R.id.historycard);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
                }
            });

        }

        public void setHistoryDto(HistoryDto historyDto) {
            this.historyDto = historyDto;
        }

        public TextView getName() {
            return name;
        }


        public TextView getDate() {
            return date;
        }


        public TextView getDurtaiion() {
            return durtaiion;
        }


        public TextView getDistance() {
            return distance;
        }


        public TextView getStatus() {
            return status;
        }


        public ImageView getRoute() {
            return route;
        }


        public CardView getCardView() {
            return cardView;
        }


    }
}
