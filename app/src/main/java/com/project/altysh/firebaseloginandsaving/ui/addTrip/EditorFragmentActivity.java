package com.project.altysh.firebaseloginandsaving.ui.addTrip;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.dto.PointL;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;
import com.project.altysh.firebaseloginandsaving.mapUtil.MaPUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Nehal on 3/5/2018.
 */

public class EditorFragmentActivity extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int UserSelectedYear;
    int UserSelectedMonth;
    int UserSelectedDay;
    int userSelectedHour;
    int userSelectedMinute;
    double StartLatitude;
    double StartLongitude;
    double EndLatitude;
    double EndLongitude;
    TextView txt;
    EditText edt;
    ArrayList<String> NotesListString;
    ArrayList<EditText> NotesListEdt;
    ArrayList<String> EndPointListString;
    ArrayList<Double> EndLatitudeList;
    ArrayList<Double> EndLongitudeList;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE2 = 2;
    AutocompleteFilter autocompleteFilter;
    View v;
    private EditText TripNameEditText;
    private TextView StartPoint;
    private TextView EndPoint;
    private EditText NotesEditText;
    private TextView DateText;
    private TextView TimeText;
    private Button StartPointSearch;
    private Button AddEndPoint;
    private Button AddNotes;
    private Switch TripType;
    /**
     * Boolean flag that keeps track of whether the Trip has been edited (true) or not (false)
     */
    private boolean HasChanged = false;
    private boolean firstEnd = false;
    private int AddPoint = 3;
    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the HasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            HasChanged = true;

            return false;
        }
    };

    public boolean isHasChanged() {
        return HasChanged;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.editor_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        TripNameEditText = v.findViewById(R.id.TripNameInput);
        NotesEditText = v.findViewById(R.id.TripNotes);
        StartPoint = v.findViewById(R.id.StartPointInput);
        EndPoint = v.findViewById(R.id.EndPointInput);
        DateText = v.findViewById(R.id.DateText);
        TimeText = v.findViewById(R.id.TimeText);
        StartPointSearch = v.findViewById(R.id.StartPointSearch);
        AddEndPoint = v.findViewById(R.id.AddEndPoint);
        TripType = v.findViewById(R.id.RoundTrip);
        AddNotes = v.findViewById(R.id.AddBtn);

        NotesListString = new ArrayList<>();
        NotesListEdt = new ArrayList<>();
        EndPointListString = new ArrayList<>();
        EndLatitudeList = new ArrayList<>();
        EndLongitudeList = new ArrayList<>();


        AddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(TextUtils.isEmpty(NotesEditText.getText().toString().trim()))) {
                    edt = new EditText(getActivity());
                    NotesListEdt.add(edt);
                    LinearLayout NotesLayout = v.findViewById(R.id.NotesLayout);
                    NotesLayout.addView(edt);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.insert_Note), Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Restrict autocomplete results
        autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_SUBLOCALITY_LEVEL_5)
                .setCountry("EG")
                .build();

        //Place Autocomplete for end point edit text
        StartPointSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent;

                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(autocompleteFilter)
                            .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        //Place Autocomplete for end point edit text
        EndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent;

                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(autocompleteFilter)
                            .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE2);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });

        AddEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstEnd) {
                    Toast.makeText(getActivity(), getString(R.string.insert_First_End), Toast.LENGTH_SHORT).show();
                } else if (AddPoint == 1) {
                    Toast.makeText(getActivity(), getString(R.string.insert_End), Toast.LENGTH_SHORT).show();
                } else if (AddPoint == 2 || AddPoint == 3) {
                    AddPoint = 1;
                    //add anther end point
                    txt = new TextView(getActivity());
                    LinearLayout EndPointLayout = v.findViewById(R.id.EndPointLayout);
                    EndPointLayout.addView(txt);
                    txt.setTextColor(Color.BLACK);
                    txt.setTextSize(18);
                    txt.setText(R.string.endPointHint);

                    txt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Intent intent;

                                intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                        .setFilter(autocompleteFilter)
                                        .build(getActivity());
                                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE2);
                            } catch (GooglePlayServicesRepairableException e) {
                                // TODO: Handle the error.
                            } catch (GooglePlayServicesNotAvailableException e) {
                                // TODO: Handle the error.
                            }
                        }
                    });

                }
            }
        });

        //check for any changes of inputs
        NotesEditText.setOnTouchListener(mTouchListener);

        TripNameEditText.setOnTouchListener(mTouchListener);


        // Log.i("fraaa back:     ",isHasChanged()+"" );

        //Date Picker Dialog from clicking + button
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), EditorFragmentActivity.this, 2018, 1, 1);

        v.findViewById(R.id.SetDate)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });

        //Time Picker Dialog  from clicking + button
        final TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(), EditorFragmentActivity.this, 12, 00, false);

        v.findViewById(R.id.setTime)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String DateTextString = DateText.getText().toString().trim();

                        if (TextUtils.isEmpty(DateTextString)) {
                            Toast.makeText(getActivity(), getString(R.string.insert_Date_First), Toast.LENGTH_SHORT).show();
                        } else {
                            timePickerDialog.show();
                        }
                    }
                });


    }


    //Result from Place Autocomplete of start point and end point
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //start point
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                StartLatitude = place.getLatLng().latitude;
                StartLongitude = place.getLatLng().longitude;


                StartPoint.setText(place.getName());
                HasChanged = true;
                Log.i("TAG", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        //end point
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE2) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                if (!firstEnd) {
                    EndPoint.setText(place.getName());
                    EndPointListString.add(EndPoint.getText().toString().trim());
                    EndLatitude = place.getLatLng().latitude;
                    EndLongitude = place.getLatLng().longitude;
                    EndLongitudeList.add(EndLongitude);
                    EndLatitudeList.add(EndLatitude);

                    HasChanged = true;
                    firstEnd = true;
                } else {
                    AddPoint = 2;
                    HasChanged = true;

                    txt.setText(place.getName());
                    EndPointListString.add(txt.getText().toString().trim());
                    EndLatitude = place.getLatLng().latitude;
                    EndLongitude = place.getLatLng().longitude;
                    EndLongitudeList.add(EndLongitude);
                    EndLatitudeList.add(EndLatitude);
                }
                Log.i("TAG", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }

    //Result from Date Picker Dialog
    @Override
    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
        String year1 = String.valueOf(selectedYear);
        String month1 = String.valueOf(selectedMonth + 1);
        String day1 = String.valueOf(selectedDay);
        UserSelectedYear = selectedYear;
        UserSelectedMonth = selectedMonth;
        UserSelectedDay = selectedDay;
        DateText.setText(DateFormat.format("EEEE", new Date(selectedYear - 1900, selectedMonth, selectedDay)).toString() + "  " + day1 + "/" + month1 + "/" + year1);
        HasChanged = true;
    }

    //Result from Time Picker Dialog
    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

        userSelectedHour = selectedHour;
        userSelectedMinute = selectedMinute;


        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");


        try {
            Date EndTime = dateFormat.parse(DateFormat.format("hh:mm a", new Date(UserSelectedYear - 1900, UserSelectedMonth, UserSelectedDay, selectedHour, selectedMinute)).toString());
            Date CurrentTime = dateFormat.parse(dateFormat.format(new Date()));

            if (CurrentTime.after(EndTime)) {
                Toast.makeText(getActivity(), "time passed", Toast.LENGTH_SHORT).show();
            } else {
                TimeText.setText(DateFormat.format("hh:mm a", new Date(UserSelectedYear - 1900, UserSelectedMonth, UserSelectedDay, selectedHour, selectedMinute)).toString());
                HasChanged = true;

                Log.i("ddddddd", DateFormat.format("dd-MM-yyyy hh:mm a",
                        new Date(UserSelectedYear - 1900, UserSelectedMonth, UserSelectedDay,
                                userSelectedHour, userSelectedMinute)).toString());

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get user input from editor_fragment
     */
    public void saveTrip() {

        String NotesString = NotesEditText.getText().toString().trim();
        String TripNameString = TripNameEditText.getText().toString().trim();
        String StartPointString = StartPoint.getText().toString().trim();
        String EndPointString = EndPoint.getText().toString().trim();
        String DateTextString = DateText.getText().toString().trim();
        String TimeTextString = TimeText.getText().toString().trim();

        Log.i("taggg", NotesString + TripNameString + StartPointString + EndPointString +
                DateTextString + TimeTextString);

        // validate all the required information
        if (!(TextUtils.isEmpty(NotesString)) && !(TextUtils.isEmpty(StartPointString))
                && !(TextUtils.isEmpty(TripNameString)) && !(TextUtils.isEmpty(EndPointString))
                && !(TextUtils.isEmpty(DateTextString)) && !(TextUtils.isEmpty(TimeTextString))) {

            Long DateTime = new Date(UserSelectedYear - 1900, UserSelectedMonth, UserSelectedDay,
                    userSelectedHour, userSelectedMinute).getTime();

            NotesListString.add(NotesEditText.getText().toString().trim());
            for (EditText noteEdt : NotesListEdt) {
                String noteStr = noteEdt.getText().toString().trim();
                if (!(TextUtils.isEmpty(noteStr))) {
                    NotesListString.add(noteStr);
                }
            }

            Trip_DTO TripInf = new Trip_DTO();
            TripInf.setDateTime(DateTime);
            TripInf.setStartPoint(StartPointString);
            TripInf.setTripName(TripNameString);
            TripInf.setTripNotes(NotesListString);
            TripInf.setTripType(TripType.isChecked());
            TripInf.setStartLatitude(StartLatitude);
            TripInf.setStartLongitude(StartLongitude);
            TripInf.setEndPoint(EndPointListString);
            TripInf.setEndLatitude(EndLatitudeList);
            TripInf.setEndLongitude(EndLongitudeList);
            String s = MaPUtil.getStaticMapNoRoad(TripInf);
            List<PointL> pointLS = new ArrayList<>();
            pointLS.add(new PointL(TripInf.getStartLatitude(), TripInf.getStartLongitude()));
            for (int i = 0; i < EndLatitudeList.size(); i++)
                pointLS.add(new PointL(EndLatitudeList.get(i), EndLongitudeList.get(i)));
            TripInf.setImageWithoutRoute(s);
            TripInf.setImageWithRoute(MaPUtil.getStaticMapRoad(s, pointLS));
            HistoryDto historyDto = new HistoryDto();
            historyDto.setTrip_dto(TripInf);
            historyDto.setAvgSpeed(70);
            historyDto.setDistance(70);
            historyDto.setDurtation(111111);
            FireBaseConnection.getInstance(getActivity().getApplicationContext()).addHistoryTrip(historyDto);
            FireBaseConnection.getInstance(getActivity().getApplicationContext()).addTrip(TripInf);


            //  Log.i("MAP", "saveTrip: " + s);
            //Log.i("MAP", "saveTrip: " + MaPUtil.getStaticMapRoad(s, pointLS));
            Toast.makeText(getActivity(), getString(R.string.editor_insert_Trip_successful), Toast.LENGTH_LONG).show();
            // Exit activity
            // Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
            //resultIntent.putExtra("some_key", "String data");
            // getActivity().setResult(MainActivity.RESULT_OK, resultIntent);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), getString(R.string.insert_Trip_failed), Toast.LENGTH_SHORT).show();
        }
    }

}
