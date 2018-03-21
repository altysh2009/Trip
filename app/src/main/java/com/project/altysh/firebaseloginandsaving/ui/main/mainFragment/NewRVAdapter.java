package com.project.altysh.firebaseloginandsaving.ui.main.mainFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.mapUtil.MaPUtil;
import com.project.altysh.firebaseloginandsaving.ui.EditTrip.EditorActivity;
import com.project.altysh.firebaseloginandsaving.ui.floatingWidgit.FloatingWidgetService;
import com.project.altysh.firebaseloginandsaving.ui.floatingWidgit.NoteObj;
import com.project.altysh.firebaseloginandsaving.ui.history.HistoryDetialAdaptor;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nouran on 3/9/2018.
 */

public class NewRVAdapter extends RecyclerView.Adapter<NewRVAdapter.TripViewHolder> {

    private List<Trip_DTO> trips;
    private Activity activity;

    NewRVAdapter(List<Trip_DTO> trips, Activity activity) {
        this.trips = trips;
        this.activity = activity;
    }

    public List<Trip_DTO> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip_DTO> trips) {
        this.trips = trips;
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_card_layout, parent, false);
        TripViewHolder tripViewHolder = new TripViewHolder(v);
        return tripViewHolder;
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        Trip_DTO trip_dto = trips.get(position);
        holder.tripName.setText(trips.get(position).getTripName());
        holder.tripDate.setText(holder.dateFormat.format(trip_dto.getDateTime()));
        holder.tripTime.setText(holder.timeFormat.format(trip_dto.getDateTime()));
        holder.notes = trips.get(position).getTripNotes();
        ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, holder.notes);
        //  holder.tripNotes.setAdapter(adapter);
        ArrayList<String> names = new ArrayList<>();
        names.add(trip_dto.getStartPoint());
        names.addAll(trip_dto.getEndPoint());
        HistoryDetialAdaptor historyDetialAdaptor = new HistoryDetialAdaptor(names, activity);
        holder.getRecyclerView().setAdapter(historyDetialAdaptor);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        holder.holderTrip = trip_dto;
        ArrayList<NoteObj> noteChecked = new ArrayList<>();
        LinearLayout linearLayout = holder.getLinearLayout();
        linearLayout.removeAllViewsInLayout();
        for (String node : trip_dto.getTripNotes()) {

            TextView textView = new TextView(holder.getContext());
            textView.setText(node);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(24);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 8, 16, 8);

            textView.setLayoutParams(params);
            textView.setBackground(holder.getContext().getDrawable(R.drawable.rounded));
            linearLayout.addView(textView);
        }


        // holder.getTripNotes().setAdapter(listAdpter);
        Picasso.get().load(trip_dto.getImageWithoutRoute()).into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout tripDetails;
        CardView cardView;
        TextView tripName;
        TextView tripDate;
        TextView tripTime;
        ImageView imageView;
        Button start;
        boolean flag = false;
        ImageButton editButton;
        TextView editDate;
        Trip_DTO holderTrip;
        java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.text.DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        LinearLayout linearLayout;
        Context context;
        //ListView tripNotes;
        RecyclerView recyclerView;
        ArrayList<String> notes;
        TripViewHolder(View itemView) {

            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tripName = itemView.findViewById(R.id.trip_name);
            tripDate = itemView.findViewById(R.id.trip_date);
            tripTime = itemView.findViewById(R.id.trip_time);
            start = itemView.findViewById(R.id.startbutton);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent widgit = new Intent(itemView.getContext(), FloatingWidgetService.class);
                    widgit.putExtra("activity_background", true);
                    widgit.putExtra("trip", holderTrip.getId());
                    itemView.getContext().startService(widgit);
                    itemView.getContext().startActivity(MaPUtil.getIntentDir(holderTrip));
                }
            });
            editButton = itemView.findViewById(R.id.editBtn);
            linearLayout = itemView.findViewById(R.id.noteslay);

            context = itemView.getContext();
            // tripNotes = itemView.findViewById(R.id.notesTxt);
            recyclerView = itemView.findViewById(R.id.form_to);
            tripDetails = itemView.findViewById(R.id.tripDetails);
            imageView = itemView.findViewById(R.id.imageView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        tripDetails.setVisibility(View.GONE);
                        flag = false;
                    } else {
                        tripDetails.setVisibility(View.VISIBLE);
                        flag = true;
                    }
                }
            });

            ////////////////////Edit////////////////////////


            editButton = itemView.findViewById(R.id.editBtn);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), EditorActivity.class);
                    intent.putExtra("id", holderTrip.getId());
                    itemView.getContext().startActivity(intent);
                }
            });


        }

        public Context getContext() {
            return context;
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

//        public ListView getTripNotes() {
//            return tripNotes;
//        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        private void showDate(int year, int month, int day) {
            editDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
            tripDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }

    }
}
