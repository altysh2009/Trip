package com.project.altysh.firebaseloginandsaving.ui.history;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;
import com.project.altysh.firebaseloginandsaving.ui.floatingWidgit.MySimpleArrayAdapter;
import com.project.altysh.firebaseloginandsaving.ui.floatingWidgit.NoteObj;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetails extends Fragment {
    RecyclerView recyclerView;
    ListView listView;
    TextView startTime;
    TextView endTime;
    TextView distance;
    // GraphView graph;
    FireBaseConnection fireBaseConnection;
    HistoryDto historyDto;
    ImageView imageView;

    public FragmentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_details, container, false);
        listView = view.findViewById(R.id.listview);
        startTime = view.findViewById(R.id.starttime);
        endTime = view.findViewById(R.id.endtime);
        distance = view.findViewById(R.id.distance);
        fireBaseConnection = FireBaseConnection.getInstance(getContext());
        recyclerView = view.findViewById(R.id.recyclev);
        // graph = view.findViewById(R.id.graph);
        imageView = view.findViewById(R.id.imageView2);




        return view;
    }

    public void setid(int id) {
        historyDto = fireBaseConnection.getHistoryId(id);
        List<String> note = historyDto.getTrip_dto().getTripNotes();
        List<Boolean> check = historyDto.getChecked();
        ArrayList<NoteObj> noteChecked = new ArrayList<>();

        for (int i = 0; i < note.size(); i++)
            if (check != null)
                noteChecked.add(new NoteObj(check.get(i), note.get(i)));
            else noteChecked.add(new NoteObj(false, note.get(i)));
        ArrayList<String> names = new ArrayList<>();
        names.add(historyDto.getTrip_dto().getStartPoint());
        names.addAll(historyDto.getTrip_dto().getEndPoint());
        MySimpleArrayAdapter listAdpter = new MySimpleArrayAdapter(getContext(), noteChecked);
        listView.setAdapter(listAdpter);
        HistoryDetialAdaptor historyDetialAdaptor = new HistoryDetialAdaptor(names, getContext());
        recyclerView.setAdapter(historyDetialAdaptor);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        String strat;//= String.format("HH:MM", new Date(historyDto.getStartTime()));
        String end = String.format("HH:MM", new Date(historyDto.getStartTime()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        if (historyDto.getEndTime() != 0)
            strat = simpleDateFormat.format(new Date(historyDto.getStartTime()));
        else strat = "Never Started";
        startTime.setText(strat);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
        if (historyDto.getEndTime() != 0)
            end = simpleDateFormat2.format(new Date(historyDto.getEndTime()));
        else end = "Never Started";
        endTime.setText(end);
        StringBuilder stringBuilder;
        if (historyDto.getDistance() > 0)
            stringBuilder = new StringBuilder().append(String.format("%.4f", historyDto.getDistance() / 1000)).append("KM");
        else stringBuilder = new StringBuilder().append("Never Started");
        distance.setText(stringBuilder.toString());

        Picasso.get().load(historyDto.getTrip_dto().getImageWithRoute()).resize(800, 600).into(imageView);


    }

    public String fmt(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

  /* DataPoint[] getArray(List<PointDara> list) {
        List<DataPoint> dataPointList = new ArrayList<>();
        long last = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            PointDara obj = list.get(i);
            double speed = distance_on_geoid(obj.getLatitue(), obj.getLongtute(), list.get(i + 1).getLatitue(), list.get(i + 1).getLongtute());
            dataPointList.add(new DataPoint((double) (last + (list.get(i + 1).getTime() - obj.getTime())) / 1000, speed));
            last = last + (list.get(i + 1).getTime() - obj.getTime());
            Log.i("TAG", "getArray: " + last);
        }
        DataPoint[] retArray = new DataPoint[dataPointList.size()];
        return dataPointList.toArray(retArray);
    }*/

    double distance_on_geoid(double lat1, double lon1, double lat2, double lon2) {
        float M_PI = 3.14f;
        // Convert degrees to radians
        lat1 = lat1 * M_PI / 180.0;
        lon1 = lon1 * M_PI / 180.0;

        lat2 = lat2 * M_PI / 180.0;
        lon2 = lon2 * M_PI / 180.0;

        // radius of earth in metres
        double r = 6378100;

        // P
        double rho1 = r * cos(lat1);
        double z1 = r * sin(lat1);
        double x1 = rho1 * cos(lon1);
        double y1 = rho1 * sin(lon1);

        // Q
        double rho2 = r * cos(lat2);
        double z2 = r * sin(lat2);
        double x2 = rho2 * cos(lon2);
        double y2 = rho2 * sin(lon2);

        // Dot product
        double dot = (x1 * x2 + y1 * y2 + z1 * z2);
        double cos_theta = dot / (r * r);

        double theta = acos(cos_theta);

        // Distance in Metres
        return r * theta;
    }

}
