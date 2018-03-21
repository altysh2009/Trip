package com.project.altysh.firebaseloginandsaving.ui.main.mainFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nouran on 3/9/2018.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TripViewHolder> {

    private static Activity activity;
    private List<Trip_DTO> trips;

    RVAdapter(List<Trip_DTO> trips, Activity activity) {
        this.trips = trips;
        RVAdapter.activity = activity;
    }

    public List<Trip_DTO> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip_DTO> trips) {
        this.trips = trips;
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        TripViewHolder tripViewHolder = new TripViewHolder(v);
        return tripViewHolder;
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        holder.tripName.setText(trips.get(position).getTripName());
        holder.tripDate.setText(holder.dateFormat.format(trips.get(position).getDateTime()));
        holder.tripTime.setText(holder.timeFormat.format(trips.get(position).getDateTime()));
        holder.notes = trips.get(position).getTripNotes();
        ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, holder.notes);
        holder.tripNotes.setAdapter(adapter);
        Trip_DTO trip = trips.get(position);
        holder.holderTrip = trip;
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
        CardView cardView;
        TextView tripName;
        TextView tripDate;
        TextView tripTime;
        boolean flag = false;
        ImageButton editButton;
        EditText editTripName;
        TextView editStartPoint;
        TextView editEndPoint;
        TextView editDate;
        TextView editTime;
        EditText editAddNotes;
        ImageView imageTimeEd;
        ImageView imageDateEd;
        Trip_DTO holderTrip;
        java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.text.DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        Date date;
        int userSelectedYear;
        int userSelectedMonth;
        int userSelectedDay;
        int userSelectedHour;
        int userSelectedMinute;
        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        int PLACE_AUTOCOMPLETE_REQUEST_CODE2 = 2;
        AutocompleteFilter autocompleteFilter;
        ImageView imageStartPointEd;
        ImageView imageEndPointEd;
        ListView tripNotes;
        ArrayList<String> notes;

        TripViewHolder(View itemView) {

            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tripName = itemView.findViewById(R.id.trip_name);
            tripDate = itemView.findViewById(R.id.trip_date);
            tripTime = itemView.findViewById(R.id.trip_time);
            editButton = itemView.findViewById(R.id.editBtn);
            tripNotes = itemView.findViewById(R.id.notesTxt);
            final Context myContext = itemView.getContext();

            editButton.setBackgroundDrawable(null);
            final RelativeLayout tripDetails = itemView.findViewById(R.id.tripDetails);

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
                    // inflate alert dialog xml
                    LayoutInflater li = LayoutInflater.from(myContext);
                    final View dialogView = li.inflate(R.layout.edit_trip, null);
                    editTripName = dialogView.findViewById(R.id.editTripName);
                    editStartPoint = dialogView.findViewById(R.id.editStartPoint);
                    editEndPoint = dialogView.findViewById(R.id.editEndPoint);
                    editDate = dialogView.findViewById(R.id.editDate);
                    editTime = dialogView.findViewById(R.id.editTime);
                    imageDateEd = dialogView.findViewById(R.id.imageDateEd);
                    imageTimeEd = dialogView.findViewById(R.id.imageTimeEd);
                    editAddNotes = dialogView.findViewById(R.id.editAddNotes);
                    imageStartPointEd = dialogView.findViewById(R.id.imageStartPointEd);
                    imageEndPointEd = dialogView.findViewById(R.id.imageEndPointEd);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            myContext);
                    // set title
                    alertDialogBuilder.setTitle("Edit Trip");
                    // set custom dialog icon
                    alertDialogBuilder.setIcon(R.mipmap.edittrip);
                    // set custom_dialog.xml to alertdialog builder
                    alertDialogBuilder.setView(dialogView);
                    editTripName.setText(holderTrip.getTripName());

                    editDate.setText(dateFormat.format(holderTrip.getDateTime()));
                    editTime.setText(timeFormat.format(holderTrip.getDateTime()));


                    imageDateEd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                                    myContext, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String year1 = String.valueOf(year);
                                    String month1 = String.valueOf(month + 1);
                                    String day1 = String.valueOf(dayOfMonth);
                                    userSelectedYear = year;
                                    userSelectedMonth = month;
                                    userSelectedDay = dayOfMonth;
                                    showDate(year, month + 1, dayOfMonth);
                                }
                            }, 2018, 1, 1);
                            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                            datePickerDialog.show();
                        }
                    });


//                    final EditText userInput = (EditText) dialogView
//                            .findViewById(R.id.et_input);
                    // seot dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Save",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            // get user input and set it to etOutput
                                            // edit text
//                                            etOutput.setText(userInput.getText());
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }

            });


            //Restrict autocomplete results
            autocompleteFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_SUBLOCALITY_LEVEL_5)
                    .setCountry("EG")
                    .build();

//            imageStartPointEd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        Intent intent;
//
//                        intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                .setFilter(autocompleteFilter)
//                                .build(activity);
//                        activity.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                    } catch (GooglePlayServicesRepairableException e) {
//                        e.printStackTrace();
//                    } catch (GooglePlayServicesNotAvailableException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });

//            imageEndPointEd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view)  {
//                    try {
//                        Intent intent;
//
//                        intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                .setFilter(autocompleteFilter)
//                                .build(activity);
//                       activity.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE2);
//                    } catch (GooglePlayServicesRepairableException e) {
//                        e.printStackTrace();
//                    } catch (GooglePlayServicesNotAvailableException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });

            ////////////////////Edit////////////////////////

        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        }

        private void showDate(int year, int month, int day) {
            editDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
            tripDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }

    }
}
