package com.project.altysh.firebaseloginandsaving.ui.history;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.dto.UserProfile;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.Controls;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;

import java.util.ArrayList;
import java.util.List;

import static com.twitter.sdk.android.core.Twitter.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements Controls, FireBaseConnection.connectToUiMain {
    RecyclerView recyclerView;
    HistoryAdaptor adaptor;
    List<HistoryDto> historyDtos;
    FireBaseConnection fireBaseConnection;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: " + "fragment");

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recyclehistory);
        adaptor = new HistoryAdaptor(getContext());
        recyclerView.setAdapter(adaptor);
        historyDtos = new ArrayList<>();

        fireBaseConnection = FireBaseConnection.getInstance(getContext(), this);
        fireBaseConnection.rejesterLisner();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void setData(View view) {

    }

    @Override
    public void onPause() {
        super.onPause();
        fireBaseConnection.removeLisner();
    }

    @Override
    public void setTrip(View view) {

    }

    @Override
    public void setHistory(View view) {

    }


    @Override
    public void updateUi(List<Trip_DTO> trip) {
        // Toast.makeText(getContext(), "updateUi", Toast.LENGTH_LONG);
    }

    @Override
    public void updateUser(UserProfile userProfile) {
        //  Toast.makeText(getContext(), "updateUser", Toast.LENGTH_LONG);
    }

    @Override
    public void updateHistory(List<HistoryDto> history) {
        //  Toast.makeText(getContext(), "historyFragment", Toast.LENGTH_LONG);
        Toast.makeText(getContext(), "historyFragment", Toast.LENGTH_LONG).show();
        if (history != null)
        adaptor.setHistoryDtoArrayList((ArrayList<HistoryDto>) history);
    }
}
